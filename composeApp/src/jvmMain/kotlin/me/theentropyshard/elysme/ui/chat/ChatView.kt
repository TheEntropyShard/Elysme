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

package me.theentropyshard.elysme.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.awtClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.ElysmeDialog
import me.theentropyshard.elysme.viewmodel.MainViewModel
import java.awt.Image
import java.awt.datatransfer.DataFlavor
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    model: MainViewModel,
) {
    Column(modifier = modifier) {
        if (model.currentChat == null) {
            NoChatView(modifier = Modifier.fillMaxSize())
        } else {
            Column {
                val currentChat = model.currentChat!!

                val messages = model.messages[currentChat.id]

                ChatHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    chat = currentChat,
                    onNameClick = {
                        model.dialog = ElysmeDialog.ChatInfoDialog
                        model.dialogVisible = true
                    },
                    onMediaClick = {
                        model.dialog = ElysmeDialog.ChatMediaDialog
                        model.dialogVisible = true
                    }
                )

                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth()
                        .height(1.dp),
                )

                ChatBody(
                    modifier = Modifier.weight(1f),
                    messages = messages!!,
                    model = model
                ) {
                    model.replyTo(it)
                }

                if (model.currentChat!!.isCanSend) {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth()
                            .height(1.dp),
                    )

                    val clipboard = LocalClipboard.current

                    ChatInput(
                        modifier = Modifier.fillMaxWidth(),
                        model = model,
                        onAttachClick = {},
                        onPaste = {
                            try {
                                val contents = clipboard.awtClipboard?.getContents(null)

                                if (contents != null) {
                                    for (flavor in contents.transferDataFlavors) {
                                        when {
                                            flavor.equals(DataFlavor.imageFlavor) -> {
                                                val image = contents.getTransferData(DataFlavor.imageFlavor) as Image

                                                model.getImageFromClipboard(image)

                                                break
                                            }

                                            flavor.isFlavorJavaFileListType -> {
                                                @Suppress("UNCHECKED_CAST")
                                                val files = contents.getTransferData(flavor) as List<File>

                                                model.currentFile = files[0]

                                                break
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoChatView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(color = Color.Black.copy(alpha = 0.75f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            text = "Select a chat to start messaging",
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.googleSans(),
            color = Color.LightGray,
        )
    }
}