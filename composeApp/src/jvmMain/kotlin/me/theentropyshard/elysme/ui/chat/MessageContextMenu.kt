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

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class MessageMenuItem(
    val icon: DrawableResource,
    val text: String,
    val description: String,
    val onClick: () -> Unit
)

@Composable
fun MessageContextMenu(
    modifier: Modifier = Modifier,
    visible: Boolean,
    position: Offset,
    onDismissRequest: () -> Unit,
    items: () -> List<MessageMenuItem>
) {
    var menuHeight by remember { mutableStateOf(0.0f) }

    DropdownMenu(
        modifier = modifier.onSizeChanged { menuHeight = it.height.toFloat() },
        expanded = visible,
        shape = RoundedCornerShape(16.dp),
        onDismissRequest = onDismissRequest,
        offset = with(LocalDensity.current) { DpOffset(position.x.toDp(), (position.y + menuHeight).toDp()) }
    ) {
        val collection = items()

        for (item in collection) {
            DropdownMenuItem(
                modifier = Modifier.height(32.dp),
                enabled = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.description
                    )
                },
                text = {
                    Text(
                        text = item.text,
                        fontFamily = Fonts.googleSans(),
                        fontWeight = FontWeight.Normal
                    )
                },
                onClick = {
                    onDismissRequest()
                    item.onClick()
                }
            )
        }
    }
}