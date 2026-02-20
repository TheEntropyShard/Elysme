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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.chat24dp
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    onClick: (DcChatListItem) -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(model.chats) {
                ChatListItem(
                    chat = it,
                    selected = model.currentChat != null && model.currentChat!!.id == it.id,
                    onClick = { onClick(it) }
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onClick = {}
        ) {
            Icon(
                painter = painterResource(Res.drawable.chat24dp),
                contentDescription = ""
            )
        }
    }
}