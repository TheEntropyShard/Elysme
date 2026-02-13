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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuoteView(
    modifier: Modifier = Modifier,
    color: String? = null,
    name: String? = null,
    text: String,
    onClick: () -> Unit
) {
    val quoteColor = if (color != null) {
        Color(0xFF000000 or color.substring(1).toLong(16))
    } else {
        Color.Unspecified
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = quoteColor.copy(alpha = 0.15f))
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = quoteColor,
                    size = Size(4.dp.toPx(), size.height),
                )
            }
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 12.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (name != null) {
                Text(
                    text = name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = quoteColor
                )
            }

            Text(
                text = text,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
