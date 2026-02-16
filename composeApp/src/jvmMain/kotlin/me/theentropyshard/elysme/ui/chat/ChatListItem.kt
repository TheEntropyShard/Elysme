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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.ui.theme.Fonts
import java.time.Instant
import java.time.ZoneId

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
        if (chat.avatarPath != null) {
            KamelImage(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                resource = { asyncPainterResource(data = File(chat.avatarPath)) },
                contentDescription = "Chat profile image - ${chat.name}",
            )
        } else {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {

            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.googleSans(),
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = FORMATTER.format(Instant.ofEpochMilli(chat.lastUpdated).atZone(ZoneId.systemDefault())),
                    fontFamily = Fonts.googleSans(),
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = buildString {
                        if (chat.summaryText1 != null && chat.summaryText1.isNotEmpty()) {
                            append(chat.summaryText1).append(": ")
                        }

                        append(chat.summaryText2)
                    },
                    fontFamily = Fonts.googleSans(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                if (chat.freshMessageCounter > 0) {
                    var heightPx by remember { mutableStateOf(0) }
                    val density = LocalDensity.current
                    val minWidthDp = with(density) { (if (heightPx == 0) 0 else heightPx).toDp() }

                    Box(
                        modifier = modifier
                            .onSizeChanged { heightPx = it.height }
                            .clip(RoundedCornerShape(percent = 50))
                            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                            .requiredWidthIn(min = minWidthDp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 1.dp, horizontal = 4.dp),
                            text = "${chat.freshMessageCounter}",
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            fontFamily = Fonts.googleSans(),
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}