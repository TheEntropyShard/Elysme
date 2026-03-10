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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theentropyshard.elysme.ui.theme.Fonts

@Composable
fun FreshMessageCounter(
    modifier: Modifier = Modifier,
    number: Int
) {
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
            text = "$number",
            color = MaterialTheme.colorScheme.secondaryContainer,
            fontFamily = Fonts.googleSans(),
            fontSize = 14.sp,
            lineHeight = 14.sp
        )
    }
}