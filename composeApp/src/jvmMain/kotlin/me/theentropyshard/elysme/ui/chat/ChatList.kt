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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.theentropyshard.elysme.deltachat.model.DcChatListItem
import me.theentropyshard.elysme.viewmodel.MainViewModel

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    onClick: (DcChatListItem) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(model.chats) {
            ChatListItem(
                chat = it,
                selected = model.currentChat != null && model.currentChat!!.id == it.id,
                onClick = { onClick(it) }
            )
        }
    }
}