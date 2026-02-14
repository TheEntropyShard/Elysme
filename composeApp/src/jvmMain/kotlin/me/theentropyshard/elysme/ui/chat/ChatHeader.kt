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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.morevert24dp
import elysme.composeapp.generated.resources.search24dp
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatHeader(
    modifier: Modifier = Modifier,
    title: String,
    memberCount: Int,
) {
    Row(
        modifier = modifier.padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.googleSans(),
            )

            Text(
                text = "$memberCount members",
                fontFamily = Fonts.googleSans(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(Res.drawable.search24dp),
                contentDescription = "Search in $title"
            )
        }

        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(Res.drawable.morevert24dp),
                contentDescription = "Open menu for chat $title"
            )
        }
    }
}