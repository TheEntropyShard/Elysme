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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import kotlin.math.max

private enum class SlotId {
    Attachment, Text, Top, Bottom, Quote, AttachmentRem, TextRem
}

@Composable
fun ChatMessageLayout(
    modifier: Modifier = Modifier,
    topRow: @Composable () -> Unit,
    quote: @Composable () -> Unit,
    attachment: @Composable () -> Unit,
    text: @Composable () -> Unit,
    bottomRow: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val minimum = constraints.copy(minWidth = 0, minHeight = 0)

        val attachmentPlaceables = subcompose(SlotId.Attachment, content = attachment).map { it.measure(minimum) }
        val attachmentWidth = attachmentPlaceables.maxOfOrNull { it.width } ?: 0

        val textPlaceables = subcompose(SlotId.Text, content = text).map { it.measure(minimum) }
        val textWidth = textPlaceables.maxOfOrNull { it.width } ?: 0

        val maxWidth = if (attachmentWidth > 0) attachmentWidth else textWidth
        val parentWidth = max(200, maxWidth.coerceAtMost(constraints.maxWidth))

        val exact = constraints.copy(minWidth = parentWidth, maxWidth = constraints.maxWidth, minHeight = 0)

        val topPlaceables = subcompose(
            SlotId.Top, content = { Box(modifier = Modifier.width(parentWidth.toDp())) { topRow() } }
        ).map { it.measure(exact) }

        val bottomPlaceables = subcompose(
            SlotId.Bottom, content = { Box(modifier = Modifier.width(parentWidth.toDp())) { bottomRow() } }
        ).map { it.measure(exact) }

        val quotePlaceables = subcompose(
            SlotId.Quote, content = { Box(modifier = Modifier.width(parentWidth.toDp())) { quote() } }
        ).map { it.measure(exact) }

        val attachmentRem = subcompose(
            SlotId.AttachmentRem, content = { Box(modifier = Modifier.width(parentWidth.toDp())) { attachment() } }
        ).map { it.measure(exact) }

        val textRem = subcompose(
            SlotId.TextRem, content = { Box(modifier = Modifier.width(parentWidth.toDp())) { text() } }
        ).map { it.measure(exact) }

        val allPlaceables = topPlaceables + quotePlaceables + attachmentRem + textRem + bottomPlaceables
        val totalHeight = allPlaceables.sumOf { it.height }.coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(parentWidth, totalHeight) {
            var y = 0

            allPlaceables.forEach {
                it.placeRelative(0, y)

                y += it.height
            }
        }
    }
}