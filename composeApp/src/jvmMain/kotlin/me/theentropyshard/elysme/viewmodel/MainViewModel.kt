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
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.deltachat.model.*
import me.theentropyshard.elysme.deltachat.request.*
import me.theentropyshard.elysme.deltachat.rpc.Rpc
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.extensions.toBufferedImage
import me.theentropyshard.elysme.ui.backup.ImportProgressView
import me.theentropyshard.elysme.ui.dialog.ChatInfoView
import me.theentropyshard.elysme.ui.dialog.ChatMediaView
import me.theentropyshard.elysme.ui.dialog.NewChatView
import me.theentropyshard.elysme.ui.dialog.ProfileInfoView
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile

sealed class Screen {
    object None : Screen()
    object ImportBackupScreen : Screen()
    object MainScreen : Screen()
}

sealed class ElysmeDialog(val content: @Composable (MainViewModel) -> Unit) {
    object ChatInfoDialog : ElysmeDialog(::ChatInfoView)
    object ChatMediaDialog : ElysmeDialog(::ChatMediaView)
    object ProfileInfoDialog : ElysmeDialog(::ProfileInfoView)
    object BackupImportDialog : ElysmeDialog(::ImportProgressView)
    object NewChatDialog : ElysmeDialog(::NewChatView)
}

data class AccountImportState(
    val progress: Float = 0f,
    val isImporting: Boolean = false,
    val error: Boolean = false,
    val started: Boolean = false
)

class MainViewModel : ViewModel() {
    val rpc = Rpc(System.getenv("RPC_SERVER_PATH"), System.getenv("DC_ACCOUNTS_PATH"))

    val gson = GsonBuilder()
        .disableJdkUnsafe()
        .disableHtmlEscaping()
        .create()

    var currentAccount by mutableStateOf<DcAccount?>(null)
    var currentChat by mutableStateOf<DcChat?>(null)
    var currentContact by mutableStateOf<DcContact?>(null)

    private val _accounts = MutableStateFlow<List<DcAccount>>(listOf())
    val accounts = _accounts.asStateFlow()

    private val _importState = MutableStateFlow(AccountImportState())
    val importState = _importState.asStateFlow()

    var editing by mutableStateOf(false)
    var currentReplyTo by mutableStateOf<DcMessage?>(null)
    var currentFile by mutableStateOf<File?>(null)

    val screen = mutableStateOf<Screen>(Screen.None)

    private val _chats = MutableStateFlow<List<DcChatListItem>>(listOf())
    val chats = _chats.asStateFlow()

    val messages = mutableStateMapOf<Int, SnapshotStateList<DcMessageListItem>>()

    var dialogVisible by mutableStateOf(false)
    var dialog by mutableStateOf<ElysmeDialog>(ElysmeDialog.ChatInfoDialog)

    init {
        rpc.start()

        viewModelScope.launch(Dispatchers.IO) {
            tryStart()
        }

        rpc.addListener(::handleEvent)
    }

    private fun tryStart() {
        if (loadAccounts()) {
            val id = getSelectedAccountId()
            currentAccount = accounts.value.first { it.id == id }

            loadChats()

            rpc.send(RpcMethod.start_io_for_all_accounts.makeRequest())

            viewModelScope.launch {
                screen.value = Screen.MainScreen
            }
        } else {
            viewModelScope.launch {
                screen.value = Screen.ImportBackupScreen
            }
        }
    }

    private fun loadAccounts(): Boolean {
        val accs = gson.fromJson(
            rpc.send(RpcMethod.get_all_accounts.makeRequest()).result,
            object : TypeToken<List<DcAccount>>() {}
        )

        if (accs.isEmpty()) {
            return false
        }

        _accounts.update { accs }

        return true
    }

    private fun getSelectedAccountId(): Int {
        return rpc.send(RpcMethod.get_selected_account_id.makeRequest()).result.asInt
    }

    private fun loadChats() {
        val entriesRequest = GetChatListEntriesRequest().apply { setAccountId(currentAccount!!.id) }
        val entries = gson.fromJson(rpc.send(entriesRequest).result, IntArray::class.java)

        val itemsRequest = GetChatListItemsByEntriesRequest().apply {
            setAccountId(currentAccount!!.id)
            setEntries(entries)
        }

        val chatListItems =
            gson.fromJson(rpc.send(itemsRequest).result, object : TypeToken<Map<String, DcChatListItem>>() {})

        val items = mutableListOf<DcChatListItem>()

        for (id in entries) {
            items += chatListItems[id.toString()]!!
        }

        _chats.update { items }
    }

    private fun handleEvent(accountId: Int, event: JsonObject) {
        val debugPrintEvents = System.getenv("DEBUG_PRINT_EVENTS")?.toBoolean() ?: false
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

            "MsgRead", "MsgsChanged", "IncomingReaction", "ReactionsChanged" -> {
                val chatId = event.get("chatId").asInt
                val msgId = event.get("msgId").asInt

                viewModelScope.launch {
                    if (messages.containsKey(chatId)) {
                        val messageList = messages[chatId]!!
                        val index = messageList.indexOfFirst { it.msgId == msgId }

                        if (index != -1) {
                            messageList[index] = DcMessageListItem(msgId)
                        }
                    }
                }
            }

            "MsgDeleted" -> {
                val chatId = event.get("chatId").asInt
                val msgId = event.get("msgId").asInt

                viewModelScope.launch {
                    if (messages.containsKey(chatId)) {
                        val messageList = messages[chatId]!!
                        val index = messageList.indexOfFirst { it.msgId == msgId }

                        if (index != -1) {
                            messageList.removeAt(index)
                        }
                    }
                }
            }

            "ChatlistChanged", "ChatlistItemChanged" -> {
                loadChats()
            }

            "ChatModified" -> {
                val chatId = event.get("chatId").asInt

                if (currentChat != null && currentChat!!.id == chatId) {
                    val chatRequest = GetFullChatByIdRequest().apply {
                        setAccountId(currentAccount!!.id)
                        setChatId(chatId)
                    }

                    currentChat = gson.fromJson(rpc.send(chatRequest).result, DcChat::class.java)
                }
            }

            "ImexProgress" -> {
                val progress = event.get("progress").asInt

                _importState.update {
                    AccountImportState(
                        progress = progress / 999f,
                        isImporting = progress in 1..<1000,
                        error = progress == 0,
                        started = true
                    )
                }

                if (progress == 1000) {
                    tryStart()
                }
            }

            "AccountsChanged" -> {

            }

            "AccountsItemChanged" -> {

            }
        }
    }

    fun importBackup(backupFilePath: String) {
        dialog = ElysmeDialog.BackupImportDialog
        dialogVisible = true

        viewModelScope.launch(Dispatchers.IO) {
            val newAccountId = rpc.send(RpcMethod.add_account.makeRequest()).result.asInt

            val importBackupRequest = RpcMethod.import_backup.makeRequest()
            importBackupRequest.addParam(newAccountId)
            importBackupRequest.addParam(backupFilePath)
            importBackupRequest.addParam(null)

            rpc.send(importBackupRequest)
        }
    }

    fun showChat(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatRequest = GetFullChatByIdRequest().apply {
                setAccountId(currentAccount!!.id)
                setChatId(id)
            }

            val chat = gson.fromJson(rpc.send(chatRequest).result, DcChat::class.java)

            if (messages.containsKey(id)) {
                currentChat = chat
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val messageListItemsRequest = GetMessageListItemsRequest().apply {
                        setAccountId(currentAccount!!.id)
                        setChatId(id)
                        setInfoOnly(false)
                        setAddDaymarker(true)
                    }

                    val messageListItems = gson.fromJson(
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
        if (editing) {
            editMessage(message!!)

            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val sendMessageRequest = SendMessageRequest().apply {
                setAccountId(currentAccount!!.id)
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

            viewModelScope.launch {
                messages.getOrPut(currentChat!!.id) { mutableStateListOf() } += DcMessageListItem(sentMessageId)
            }
        }
    }

    fun editMessage(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RpcMethod.send_edit_request.makeRequest()
            request.addParam(currentAccount!!.id)
            request.addParam(currentReplyTo!!.id)
            request.addParam(newText)

            rpc.send(request)

            viewModelScope.launch {
                cancelReply()
                editing = false
            }
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

    fun showProfileDialog(contact: DcContact) {
        currentContact = contact
        dialog = ElysmeDialog.ProfileInfoDialog
        dialogVisible = true
    }

    fun acceptChat(chatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RpcMethod.accept_chat.makeRequest()
            request.addParam(currentAccount!!.id)
            request.addParam(chatId)

            rpc.send(request)
        }
    }

    fun blockChat(chatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RpcMethod.block_chat.makeRequest()
            request.addParam(currentAccount!!.id)
            request.addParam(chatId)

            rpc.send(request)
        }
    }
}