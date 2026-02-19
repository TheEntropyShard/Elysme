/*
 * Elysme - https://github.com/TheEntropyShard/Elysme
 * Copyright (C) 2026 TheEntropyShard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.elysme.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.deltachat.model.DcChat
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.deltachat.model.DcMessageListItem
import me.theentropyshard.elysme.deltachat.request.*
import me.theentropyshard.elysme.deltachat.rpc.Rpc
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.extensions.indexMap
import me.theentropyshard.elysme.extensions.toBufferedImage
import me.theentropyshard.elysme.ui.dialog.ChatInfoView
import me.theentropyshard.elysme.ui.dialog.ChatMediaView
import me.theentropyshard.elysme.ui.dialog.ProfileInfoView
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile

sealed class Screen {
    object ImportBackupScreen : Screen()
    object MainScreen : Screen()
}

sealed class ElysmeDialog(val content: @Composable (MainViewModel) -> Unit) {
    object ChatInfoDialog : ElysmeDialog(::ChatInfoView)
    object ChatMediaDialog : ElysmeDialog(::ChatMediaView)
    object ProfileInfoDialog : ElysmeDialog(::ProfileInfoView)
}

class MainViewModel : ViewModel() {
    val rpc = Rpc(System.getenv("RPC_SERVER_PATH"), System.getenv("DC_ACCOUNTS_PATH"))
    val gson = Gson()

    var currentAccountId = 0
    var currentChat by mutableStateOf<DcChat?>(null)

    var currentReplyTo by mutableStateOf<DcMessage?>(null)
    var currentFile by mutableStateOf<File?>(null)

    val screen = mutableStateOf<Screen>(Screen.MainScreen)

    val chats = mutableStateListOf<DcChatListItem>()
    val messages = mutableStateMapOf<Int, SnapshotStateList<DcMessageListItem>>()

    var dialogVisible by mutableStateOf(false)
    var dialog by mutableStateOf<ElysmeDialog>(ElysmeDialog.ChatInfoDialog)

    init {
        rpc.start()

        viewModelScope.launch(Dispatchers.IO) {
            currentAccountId = getSelectedAccountId()

            loadChats()
        }

        Thread(::handleEvents).apply {
            isDaemon = true
            name = "RPC Event Handler"

            start()
        }
    }

    private fun getSelectedAccountId(): Int {
        return rpc.send(RpcMethod.get_selected_account_id.makeRequest()).result.asInt
    }

    private fun loadChats() {
        val gson = GsonBuilder().disableJdkUnsafe().disableHtmlEscaping().create()

        val entriesRequest = GetChatListEntriesRequest().apply { setAccountId(currentAccountId) }
        val entries = gson.fromJson(rpc.send(entriesRequest).result, IntArray::class.java)

        val itemsRequest = GetChatListItemsByEntriesRequest().apply {
            setAccountId(currentAccountId)
            setEntries(entries)
        }

        val chatListItems =
            gson.fromJson(rpc.send(itemsRequest).result, object : TypeToken<Map<String, DcChatListItem>>() {})

        for (id in entries) {
            chats += chatListItems[id.toString()]!!
        }
    }

    private fun handleEvents() {
        val gson = Gson()

        val debugPrintEvents = System.getenv("DEBUG_PRINT_EVENTS")?.toBoolean() ?: false

        while (rpc.running.get()) {
            val event = rpc.waitForEvent(currentAccountId)
            val kind = event["kind"].asString

            if (debugPrintEvents && kind != "Info") println(event)

            when (kind) {
                "IncomingMsg" -> {
                    val chatId = event.get("chatId").asInt
                    val msgId = event.get("msgId").asInt

                    viewModelScope.launch {
                        if (messages.containsKey(chatId)) {
                            messages[chatId]!! += DcMessageListItem(msgId)
                        }
                    }
                }

                "MsgRead" -> {
                    val chatId = event.get("chatId").asInt
                    val msgId = event.get("msgId").asInt

                    viewModelScope.launch {
                        if (messages.containsKey(chatId)) {
                            val messageList = messages.getOrPut(chatId) { mutableStateListOf() }
                            val index = messageList.indexOfFirst { it.msgId == msgId }

                            if (index != -1) {
                                messageList[index] = DcMessageListItem(msgId)
                            }
                        }
                    }
                }

                "ChatlistItemChanged" -> {
                    val chatId = event.get("chatId").asInt

                    viewModelScope.launch(Dispatchers.IO) {
                        val itemsRequest = GetChatListItemsByEntriesRequest().apply {
                            setAccountId(currentAccountId)
                            setEntries(IntArray(1).apply { set(0, chatId) })
                        }

                        val chatListItems =
                            gson.fromJson(
                                rpc.send(itemsRequest).result,
                                object : TypeToken<Map<String, DcChatListItem>>() {})

                        val chat = chats.find { it.id == chatId }

                        if (chat != null) {
                            val newChat = chatListItems[chatId.toString()]

                            if (newChat != null) chats[chats.indexOf(chat)] = newChat
                        }
                    }
                }

                "ChatlistChanged" -> {
                    viewModelScope.launch {
                        val entriesRequest = GetChatListEntriesRequest().apply { setAccountId(currentAccountId) }
                        val entries = gson.fromJson(rpc.send(entriesRequest).result, object : TypeToken<List<Int>>() {})
                        val orderMap = entries.indexMap()

                        chats.sortBy { orderMap[it.id] }
                    }
                }
            }
        }
    }

    fun importBackup(backupFilePath: String) {
        screen.value = Screen.MainScreen
    }

    fun showChat(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatRequest = GetFullChatByIdRequest().apply {
                setAccountId(currentAccountId)
                setChatId(id)
            }

            val chat = Gson().fromJson(rpc.send(chatRequest).result, DcChat::class.java)

            if (messages.containsKey(id)) {
                currentChat = chat
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val messageListItemsRequest = GetMessageListItemsRequest().apply {
                        setAccountId(currentAccountId)
                        setChatId(id)
                        setInfoOnly(false)
                        setAddDaymarker(true)
                    }

                    val messageListItems = Gson().fromJson(
                        rpc.send(messageListItemsRequest).result,
                        object : TypeToken<List<DcMessageListItem>>() {})

                    viewModelScope.launch {
                        messages.getOrPut(id) { mutableStateListOf() }.addAll(messageListItems)

                        currentChat = chat
                    }
                }
            }
        }
    }

    fun sendMessage(message: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val sendMessageRequest = SendMessageRequest().apply {
                setAccountId(currentAccountId)
                setChatId(currentChat!!.id)
                setText(message)
                setQuotedMessageId(currentReplyTo?.id)
                setFile(currentFile?.absolutePath)
                setFilename(currentFile?.name)
            }

            viewModelScope.launch {
                cancelReply()
                currentFile = null
            }

            val sentMessageId = rpc.send(sendMessageRequest).result.asInt

            messages.getOrPut(currentChat!!.id) { mutableStateListOf() } += DcMessageListItem(sentMessageId)
        }
    }

    fun replyTo(message: DcMessage?) {
        currentReplyTo = message
    }

    fun cancelReply() {
        currentReplyTo = null
    }

    fun getImageFromClipboard(image: Image) {
        viewModelScope.launch(Dispatchers.IO) {
            val path = createTempFile(
                directory = null,
                prefix = "elysme_image",
                suffix = ".png"
            )

            val file = path.toFile()

            ImageIO.write(image.toBufferedImage(), "PNG", file)

            currentFile = file
        }
    }
}