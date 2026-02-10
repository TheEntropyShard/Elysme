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

package me.theentropyshard.elysme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.theentropyshard.elysme.ui.backup.ImportBackupView
import me.theentropyshard.elysme.ui.chat.ChatList
import me.theentropyshard.elysme.ui.chat.ChatView
import me.theentropyshard.elysme.ui.theme.ElysmeTheme
import me.theentropyshard.elysme.viewmodel.MainViewModel
import me.theentropyshard.elysme.viewmodel.Screen

@Composable
@Preview
fun App() {
    val model = viewModel { MainViewModel() }
    var currentScreen by model.screen

    ElysmeTheme() {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(durationMillis = 1000),
                label = "Screen Crossfade"
            ) { screen ->
                when (screen) {
                    is Screen.ImportBackupScreen -> ImportBackupView { path ->
                        model.importBackup(path)
                    }

                    is Screen.MainScreen -> Row {
                        ChatList(
                            modifier = Modifier.width(380.dp),
                            model = model,
                        ) { chat ->
                            model.showChat(chat)
                        }

                        if (model.currentChatId != -1) {
                            ChatView(
                                modifier = Modifier.fillMaxSize(),
                                model = model
                            ) { text ->
                                model.sendMessage(text)
                            }
                        }
                    }
                }
            }
        }
    }
}