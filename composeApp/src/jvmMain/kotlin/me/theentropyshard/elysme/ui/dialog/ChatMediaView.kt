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

package me.theentropyshard.elysme.ui.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elysme.composeapp.generated.resources.*
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.deltachat.request.GetMessagesRequest
import me.theentropyshard.elysme.deltachat.request.GetSingleMessageRequest
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.ui.chat.FileAttachment
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class MediaViewTab(
    val icon: DrawableResource,
    val text: String,
    val typeName: String
)

@Composable
fun ChatMediaView(model: MainViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = remember {
        listOf(
            MediaViewTab(icon = Res.drawable.photo24dp, text = "Images", typeName = "Image"),
            MediaViewTab(icon = Res.drawable.video24dp, text = "Videos", typeName = "Video"),
            MediaViewTab(icon = Res.drawable.headphones24dp, text = "Audios", typeName = "Audio"),
            MediaViewTab(icon = Res.drawable.file24dp, text = "Files", typeName = "File"),
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(tab.icon), contentDescription = "")

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(text = tab.text, fontFamily = Fonts.googleSans())
                            }
                        },
                    )
                }
            }

            AnimatedContent(targetState = selectedTabIndex) {
                val typeName = tabs[selectedTabIndex].typeName

                val mediaIdsRequest = RpcMethod.get_chat_media.makeRequest()
                mediaIdsRequest.addParam(model.currentAccountId)
                mediaIdsRequest.addParam(model.currentChat!!.id)
                mediaIdsRequest.addParam(typeName)
                mediaIdsRequest.addParam(null)
                mediaIdsRequest.addParam(null)

                val mediaIdsResult = Gson().fromJson(model.rpc.send(mediaIdsRequest).result, IntArray::class.java)

                when (typeName) {
                    "Image" -> {
                        ImageGrid(model = model, messageIds = mediaIdsResult)
                    }

                    "Video" -> {
                        Text(text = "TODO: Videos")
                    }

                    "Audio" -> {
                        Text(text = "TODO: Audios")
                    }

                    "File" -> {
                        FileList(model = model, messageIds = mediaIdsResult)
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageGrid(model: MainViewModel, messageIds: IntArray) {
    val gson = Gson()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        for (i in (messageIds.size - 1) downTo 0) {
            val id = messageIds[i]

            item(key = id) {
                val request = GetSingleMessageRequest().apply {
                    setAccountId(model.currentAccountId)
                    setMsgId(id)
                }

                val msg = gson.fromJson(model.rpc.send(request).result, DcMessage::class.java)

                if (msg != null && msg.file != null) {
                    KamelImage(
                        modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(8.dp)),
                        resource = { asyncPainterResource(data = File(msg.file)) },
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun FileList(model: MainViewModel, messageIds: IntArray) {
    val messagesRequest = GetMessagesRequest().apply {
        setAccountId(model.currentAccountId)
        setMessageIds(messageIds)
    }

    val messages = ArrayList(
        Gson().fromJson(
            model.rpc.send(messagesRequest).result,
            object : TypeToken<Map<String, DcMessage>>() {}).values
    )

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages, key = { it.id }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                FileAttachment(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    message = it
                )
            }
        }
    }
}
