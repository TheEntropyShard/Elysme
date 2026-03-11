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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.close24dp
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuoteView(
    modifier: Modifier = Modifier,
    color: String? = null,
    name: String? = null,
    text: String,
    onCancel: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val quoteColor = color?.toColor() ?: Color.Unspecified

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
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    fontFamily = Fonts.googleSans(),
                    color = quoteColor
                )
            }

            Text(
                text = text,
                fontSize = 14.sp,
                fontFamily = Fonts.googleSans(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (onCancel != null) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onCancel
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close24dp),
                    contentDescription = "Cancel",
                    tint = quoteColor
                )
            }
        }
    }
}
