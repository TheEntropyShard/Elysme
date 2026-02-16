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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val FORMATTER_WITH_YEAR = DateTimeFormatter.ofPattern("d MMMM, uuuu")
private val FORMATTER_WITHOUT_YEAR = DateTimeFormatter.ofPattern("d MMMM")

@Composable
fun ChatDayMarker(
    modifier: Modifier = Modifier,
    timestamp: Long
) {
    val formatted = remember {
        val time = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault())
        val currentYear = LocalDate.now().year
        val formatter = if (currentYear == time.year) FORMATTER_WITHOUT_YEAR else FORMATTER_WITH_YEAR
        formatter.format(time)
    }

    ChatInfoMessage(modifier = modifier, text = formatted)
}