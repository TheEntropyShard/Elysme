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

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.theentropyshard.elysme.ui.theme.ElysmeTheme
import java.awt.Frame

lateinit var parent: Frame

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1280.dp, 720.dp)),
        title = "Elysme"
    ) {
        parent = window

        ElysmeTheme {
            App()
        }
    }
}

/*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.theentropyshard.elysme.ui.theme.ElysmeTheme
import java.awt.Frame
import kotlin.system.exitProcess

lateinit var parent: Frame

fun main() = application {
    ElysmeTheme {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(1280.dp, 720.dp)),
            undecorated = true,
            title = "Elysme"
        ) {
            parent = window

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                WindowDraggableArea {
                    TitleBar(title = "Elysme")
                }

                App(modifier = Modifier.padding(8.dp).clip(RoundedCornerShape(16.dp)))
            }
        }
    }
}

@Composable
fun WindowScope.TitleBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.DarkGray)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { /* Minimize logic */ }) { Text("—") }
            Button(onClick = { if (window is Frame) (window as Frame).extendedState = Frame.ICONIFIED }) { Text("◻") }
            Button(onClick = { exitProcess(0) }) { Text("✕") }
        }
    }
}

// see https://stackoverflow.com/questions/51008461/javafx-minimizing-maximing-undecorated-stage-with-animations

*/