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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachReversed
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.forward24dp
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.deltachat.model.DcMessageListItem
import me.theentropyshard.elysme.deltachat.request.GetSingleMessageRequest
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatBody(
    modifier: Modifier = Modifier,
    messages: List<DcMessageListItem>,
    model: MainViewModel,
    onReply: (DcMessage) -> Unit,
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        state.animateScrollToItem(0)
    }

    val reachedTop by remember {
        derivedStateOf {
            state.reachedTop()
        }
    }

    LaunchedEffect(reachedTop) {
        if (reachedTop && messages.isNotEmpty()) {
            println("reached top")
        }
    }

    Box(modifier = modifier) {
        Row {
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = state,
                reverseLayout = true,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            ) {
                messages.fastForEachReversed { message ->
                    val key = when (message.kind) {
                        "message" -> message.msgId
                        "dayMarker" -> "dayMarker-${message.timestamp}"
                        else -> throw RuntimeException("Unexpected kind of message: ${message.kind}")
                    }

                    item(key = key) {
                        when (message.kind) {
                            "message" -> {
                                val request = GetSingleMessageRequest().apply {
                                    setAccountId(model.currentAccountId)
                                    setMsgId(message.msgId)
                                }

                                val msg = model.gson.fromJson(model.rpc.send(request).result, DcMessage::class.java)

                                if (msg.isIsInfo) {
                                    ChatInfoMessage(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = msg.text,
                                        foreground = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                } else {
                                    ChatMessage(message = msg, onReply = onReply) { id ->
                                        scope.launch {
                                            val index =
                                                messages.size - messages.indexOfFirst { msg -> msg.msgId == id } - 1

                                            if (index >= 0 && index < messages.size) {
                                                state.animateScrollToItem(index)
                                            }
                                        }
                                    }
                                }
                            }

                            "dayMarker" -> {
                                ChatDayMarker(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    timestamp = message.timestamp
                                )
                            }
                        }
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                adapter = rememberScrollbarAdapter(state),
                reverseLayout = true
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            visible = state.firstVisibleItemIndex > 3,
        ) {
            FloatingActionButton(
                modifier = Modifier.scale(0.75f),
                shape = CircleShape,
                // elevation (shadow specifically) is buggy with transitions. not sure which looks better: with or without elevation
                elevation = FloatingActionButtonDefaults.elevation(
                    2.dp, 2.dp, 2.dp, 2.dp
                ),
                onClick = {
                    scope.launch {
                        state.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.graphicsLayer { rotationZ = 90.0f },
                    painter = painterResource(Res.drawable.forward24dp),
                    contentDescription = "Scroll to bottom"
                )
            }
        }
    }
}

fun LazyListState.reachedTop(): Boolean {
    return if (layoutInfo.totalItemsCount == 0) {
        false
    } else {
        val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
        val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

        // Check if the last visible item is the last item in the list and fully visible
        // This indicates that the user has scrolled to the top
        (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                lastVisibleItem.offset - lastVisibleItem.size <= viewportHeight)
    }
}