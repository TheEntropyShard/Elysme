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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.ui.theme.Fonts

@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    profileImagePath: String?,
    chatName: String,
    messageTime: String = "00:00",
    lastEventText: String = "Lorem ipsum dolor sit amet",
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (profileImagePath != null) {
            KamelImage(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                resource = { asyncPainterResource(data = File(profileImagePath)) },
                contentDescription = "Chat profile image - $chatName",
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
                    text = chatName,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.googleSans(),
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = messageTime,
                    fontFamily = Fonts.googleSans(),
                )
            }

            Text(
                text = lastEventText,
                fontFamily = Fonts.googleSans(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}