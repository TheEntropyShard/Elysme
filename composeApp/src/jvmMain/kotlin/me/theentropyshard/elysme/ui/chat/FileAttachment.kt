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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.file24dp
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource

const val MIDDLE_DOT = "•"

@Composable
fun FileAttachment(
    modifier: Modifier = Modifier,
    message: DcMessage,
    onClick: () -> Unit = {},
) = FileAttachment(
    modifier = modifier,
    name = message.fileName,
    mime = message.fileMime,
    size = message.fileBytes,
    onClick = onClick
)

@Composable
fun FileAttachment(
    modifier: Modifier = Modifier,
    name: String,
    mime: String? = null,
    size: Long? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .clickable { onClick() }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(Res.drawable.file24dp),
            contentDescription = "File: $name"
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = Fonts.googleSans(),
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = buildString {
                    mime?.let { append(mime) }

                    if (mime != null && size != null) append(" $MIDDLE_DOT ")

                    size?.let { append("$size B") }
                },
                fontFamily = Fonts.googleSans()
            )
        }
    }
}