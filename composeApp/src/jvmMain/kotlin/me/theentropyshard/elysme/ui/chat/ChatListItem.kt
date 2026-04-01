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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.*
import me.theentropyshard.elysme.deltachat.model.ChatType
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.components.FreshMessageCounter
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.components.TimeText
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource
import java.awt.Point
import java.awt.event.MouseEvent
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    chat: DcChatListItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    var menuVisible by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }

    MessageContextMenu(
        visible = menuVisible,
        position = menuOffset,
        onDismissRequest = { menuVisible = false },
        items = {
            listOf(
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.pin24dp,
                    text = "Pin chat",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.notificationoff24dp,
                    text = "Disable notifications",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.archive24dp,
                    text = "Archive chat",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.Separator(),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.account24dp,
                    text = "View profile",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.encrypted24dp,
                    text = "View encryption info",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.leave24dp,
                    text = "Leave",
                    description = "",
                    onClick = {}
                ),
                MessageMenuItem.ActionMenuItem(
                    icon = Res.drawable.delete24dp,
                    text = "Delete chat",
                    description = "",
                    onClick = {}
                ),
            )
        }
    )

    Row(
        modifier = modifier
            .pointerHoverIcon(icon = PointerIcon.Hand)
            .background(color = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Unspecified)
            .clickable { onClick() }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        if (event.type == PointerEventType.Release && event.button?.isSecondary ?: false) {
                            val point = (event.nativeEvent as MouseEvent).point
                            menuOffset = Offset(point.x.toFloat(), point.y.toFloat())
                            menuVisible = true
                        }
                    }
                }
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ProfileImage(
            profileImage = chat.avatarPath,
            contentDescription = "Chat profile image - ${chat.name}",
            size = 48.dp,
            displayName = chat.name,
            color = chat.color.toColor()
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                when (chat.chatType) {
                    ChatType.Group -> Icon(
                        modifier = Modifier.padding(end = 4.dp).size(16.dp),
                        painter = painterResource(Res.drawable.group24dp),
                        contentDescription = null
                    )

                    ChatType.InBroadcast, ChatType.OutBroadcast -> Icon(
                        modifier = Modifier.padding(end = 4.dp).size(16.dp),
                        painter = painterResource(Res.drawable.campaign24dp),
                        contentDescription = null
                    )

                    else -> {}
                }

                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.googleSans(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                when (chat.summaryStatus) {
                    26 -> Icon(
                        modifier = Modifier.padding(end = 4.dp).size(16.dp),
                        painter = painterResource(Res.drawable.unread24dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )

                    28 -> Icon(
                        modifier = Modifier.padding(end = 4.dp).size(16.dp),
                        painter = painterResource(Res.drawable.read24dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }

                TimeText(
                    timestamp = chat.lastUpdated,
                    timeUnit = ChronoUnit.MILLIS,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                val primary = MaterialTheme.colorScheme.primary

                val summary = remember(chat.summaryText1, chat.summaryText2) {
                    buildAnnotatedString {
                        if (!chat.summaryText1.isNullOrEmpty()) {
                            withStyle(style = SpanStyle(color = primary)) {
                                append("${chat.summaryText1}: ")
                            }
                        }

                        append(chat.summaryText2)
                    }
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = summary,
                    fontFamily = Fonts.googleSans(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                if (chat.freshMessageCounter > 0) {
                    Spacer(modifier = Modifier.width(8.dp))

                    FreshMessageCounter(number = chat.freshMessageCounter)
                }
            }
        }
    }
}