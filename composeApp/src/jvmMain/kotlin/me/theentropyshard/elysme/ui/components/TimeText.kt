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

package me.theentropyshard.elysme.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import me.theentropyshard.elysme.ui.theme.Fonts
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun TimeText(
    modifier: Modifier = Modifier,
    timestamp: Long,
    timeUnit: ChronoUnit,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily? = Fonts.googleSans(),
) {
    val text = remember(timestamp) {
        val instant = when (timeUnit) {
            ChronoUnit.MILLIS -> Instant.ofEpochMilli(timestamp)
            ChronoUnit.SECONDS -> Instant.ofEpochSecond(timestamp)
            else -> throw RuntimeException("Unsupported time unit: $timeUnit")
        }

        FORMATTER.format(instant.atZone(ZoneId.systemDefault()))
    }

    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontFamily = fontFamily,
        maxLines = 1,
    )
}