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

package me.theentropyshard.elysme.ui.emoji

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import me.theentropyshard.elysme.getEmojiInfo
import me.theentropyshard.elysme.sheet

@Composable
fun Emoji(
    modifier: Modifier = Modifier,
    emoji: String
) {
    val emojiInfo = remember { getEmojiInfo(emoji) }

    if (emojiInfo != null) {
        Canvas(modifier = modifier) {
            val srcOffset = IntOffset(x = emojiInfo.first + 1, y = emojiInfo.second + 1)
            val srcSize = IntSize(width = 64, height = 64)

            val dstOffset = IntOffset(x = 0, y = 0)
            val dstSize = IntSize(width = 20, height = 20)

            drawImage(
                image = sheet,
                srcOffset = srcOffset,
                srcSize = srcSize,
                dstOffset = dstOffset,
                dstSize = dstSize,
                filterQuality = FilterQuality.High
            )
        }
    }
}