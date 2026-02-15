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
import me.theentropyshard.elysme.deltachat.request.*
import me.theentropyshard.elysme.deltachat.rpc.Rpc
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod

sealed class Screen {
    object ImportBackupScreen : Screen()
    object MainScreen : Screen()
}

class MainViewModel : ViewModel() {
    private val rpc = Rpc(System.getenv("RPC_SERVER_PATH"), System.getenv("DC_ACCOUNTS_PATH"))

    private var currentAccountId = 0
    var currentChat by mutableStateOf<DcChat?>(null)

    var currentChatTitle by mutableStateOf("")
    var currentChatMembers by mutableStateOf(0)

    var currentReplyTo by mutableStateOf<DcMessage?>(null)

    val screen = mutableStateOf<Screen>(Screen.MainScreen)

    val chats = mutableStateListOf<DcChatListItem>()
    val messages = mutableStateMapOf<Int, SnapshotStateList<DcMessage>>()

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

        while (rpc.running.get()) {
            val event = rpc.waitForEvent(currentAccountId)
            val kind = event["kind"].asString

            when (kind) {
                "IncomingMsg" -> {
                    println("incoming msg")

                    val chatId = event.get("chatId").asInt
                    val msgId = event.get("msgId").asInt

                    val request = RpcMethod.get_message.makeRequest()
                    request.addParam(currentAccountId)
                    request.addParam(msgId)

                    val message = gson.fromJson(this.rpc.send(request).result, DcMessage::class.java)

                    viewModelScope.launch {
                        messages.getOrPut(chatId) { mutableStateListOf() } += message
                    }
                }

                "MsgRead" -> {
                    println("msg read")

                    val chatId = event.get("chatId").asInt
                    val msgId = event.get("msgId").asInt

                    viewModelScope.launch {
                        val messageList = messages.getOrPut(chatId) { mutableStateListOf() }
                        val index = messageList.indexOfFirst { it.id == msgId }

                        if (index != -1) {
                            messageList[index] = DcMessage(messageList[index].also { it.state = 28 })
                        }
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

            currentChatTitle = chat.name
            currentChatMembers = chat.contactIds.size

            if (messages.containsKey(id)) {
                currentChat = chat
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val messageIdsRequest = GetMessageIdsRequest().apply {
                        setAccountId(currentAccountId)
                        setChatId(id)
                        setInfoOnly(false)
                        setAddDaymarker(true)
                    }

                    val messageIds = Gson().fromJson(rpc.send(messageIdsRequest).result, IntArray::class.java)

                    val messagesRequest = GetMessagesRequest().apply {
                        setAccountId(currentAccountId)
                        setMessageIds(messageIds)
                    }
                    val messagesResponse = rpc.send(messagesRequest)

                    val sortedMessages = mutableListOf<DcMessage>()

                    val specialMessageIdLast = 9

                    val asMap =
                        Gson().fromJson(messagesResponse.result, object : TypeToken<Map<String, DcMessage>>() {})
                    messageIds.filterNot { id -> id <= specialMessageIdLast }
                        .forEach { id -> asMap[id.toString()]?.let { sortedMessages += it } }

                    viewModelScope.launch {
                        messages.getOrPut(id) { mutableStateListOf() }.addAll(sortedMessages)

                        currentChat = chat
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val sendMessageRequest = SendMessageRequest().apply {
                setAccountId(currentAccountId)
                setChatId(currentChat!!.id)
                setText(message)
                setQuotedMessageId(currentReplyTo?.id)
            }

            viewModelScope.launch { cancelReply() }

            val sentMessageId = rpc.send(sendMessageRequest).result.asInt

            val getMessageRequest = GetSingleMessageRequest().apply {
                setAccountId(currentAccountId)
                setMsgId(sentMessageId)
            }

            val message = Gson().fromJson(rpc.send(getMessageRequest).result.asJsonObject, DcMessage::class.java)

            messages.getOrPut(currentChat!!.id) { mutableStateListOf() } += message
        }
    }

    fun replyTo(message: DcMessage?) {
        currentReplyTo = message
    }

    fun cancelReply() {
        currentReplyTo = null
    }
}