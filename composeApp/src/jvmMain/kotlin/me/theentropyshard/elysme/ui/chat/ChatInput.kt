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

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.attach24dp
import elysme.composeapp.generated.resources.send24dp
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    onAttachClick: () -> Unit,
) {
    val state = rememberTextFieldState()
    val requester = remember { FocusRequester() }

    val sendMessage: () -> Unit = {
        if (state.text.trim().isNotBlank()) {
            model.sendMessage(state.text.toString())
        }

        state.edit { delete(start = 0, end = length) }
        requester.requestFocus()
    }

    LaunchedEffect(model.currentReplyTo) {
        if (model.currentReplyTo != null) {
            requester.requestFocus()
        }
    }

    ChatInputBase(
        modifier = modifier,
        state = state,
        focusRequester = requester,
        quoteColor = model.currentReplyTo?.sender?.color,
        quoteName = model.currentReplyTo?.sender?.displayName,
        quoteText = model.currentReplyTo?.text,
        onSend = sendMessage,
        onCancelReply = { model.cancelReply() },
        placeholder = {
            Text(
                text = "Write a message...",
                fontFamily = Fonts.googleSans(),
            )
        },
        leadingIcon = {
            IconButton(onClick = onAttachClick) {
                Icon(
                    modifier = Modifier.graphicsLayer { rotationZ = 45.0f },
                    painter = painterResource(Res.drawable.attach24dp),
                    contentDescription = "Add an attachment",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = sendMessage) {
                Icon(
                    painter = painterResource(Res.drawable.send24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun ChatInputBase(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    focusRequester: FocusRequester,
    quoteName: String? = null,
    quoteText: String? = null,
    quoteColor: String? = null,
    onSend: () -> Unit,
    onCancelReply: () -> Unit,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (quoteText != null) {
            QuoteView(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                color = quoteColor,
                name = quoteName,
                text = quoteText,
                onClick = onCancelReply
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon()

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = state.text.isEmpty(),
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it }),
                    ) {
                        placeholder()
                    }
                }

                BasicTextField(
                    modifier = Modifier
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
                                if (!event.isShiftPressed) {
                                    onSend()
                                } else {
                                    state.edit { append('\n') }
                                }

                                true
                            } else {
                                false
                            }
                        }
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 8),
                    state = state,
                    textStyle = TextStyle(
                        fontFamily = Fonts.googleSans(),
                    )
                )
            }

            trailingIcon()
        }
    }
}