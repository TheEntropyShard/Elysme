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
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.attach24dp
import elysme.composeapp.generated.resources.send24dp
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    onSendMessage: (String) -> Unit
) {
    val text = rememberTextFieldState()

    Column(modifier = modifier) {
        if (model.currentChatId == -1) {
            NoChatView(modifier = Modifier.fillMaxSize())
        } else {
            Column {
                val messages = model.messages[model.currentChatId]

                ChatHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    title = model.currentChatTitle,
                    memberCount = model.currentChatMembers
                )

                ChatBody(
                    modifier = Modifier.weight(1f),
                    messages = messages!!
                ) {
                    model.replyTo(it)
                }

                ChatInput(
                    modifier = Modifier.fillMaxWidth().padding(
                        start = 8.dp, end = 8.dp, bottom = 8.dp
                    ),
                    state = text,
                    model = model,
                    quoteColor = model.currentReplyTo?.sender?.color,
                    quoteName = model.currentReplyTo?.sender?.displayName,
                    quoteText = model.currentReplyTo?.text,
                    placeholder = { Text(text = "Write a message...") },
                    leadingIcon = {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                modifier = Modifier.graphicsLayer { rotationZ = 45.0f },
                                painter = painterResource(Res.drawable.attach24dp),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (text.text.trim().isNotBlank()) {
                                    onSendMessage(text.text.toString())
                                }

                                text.edit { delete(start = 0, end = length) }
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.send24dp),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
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
            fontWeight = FontWeight.SemiBold,
            color = Color.LightGray,
        )
    }
}