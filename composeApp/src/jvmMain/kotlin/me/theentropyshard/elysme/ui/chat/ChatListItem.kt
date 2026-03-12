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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.campaign24dp
import elysme.composeapp.generated.resources.group24dp
import me.theentropyshard.elysme.deltachat.model.ChatType
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.components.FreshMessageCounter
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.components.TimeText
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource
import java.time.temporal.ChronoUnit

@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    chat: DcChatListItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Unspecified)
            .clickable { onClick() }
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
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.group24dp),
                        contentDescription = null
                    )

                    ChatType.InBroadcast, ChatType.OutBroadcast -> Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.campaign24dp),
                        contentDescription = null
                    )

                    else -> {}
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.googleSans(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

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