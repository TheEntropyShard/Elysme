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

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.AwtWindow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

lateinit var parent: Frame

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1280.dp, 720.dp)),
        title = "Elysme",
    ) {
        parent = window

        App()
    }
}

@Composable
fun ChooseFileDialog(
    filter: FilenameFilter? = null,
    multipleMode: Boolean = false,
    onCloseRequest: (result: List<File>) -> Unit
) {
    AwtWindow(
        create = {
            object : FileDialog(parent, "Choose a file", LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)

                    if (value) {
                        onCloseRequest(files.map { file -> file.absoluteFile })
                    }
                }
            }.apply {
                isMultipleMode = multipleMode

                if (filter != null) {
                    filenameFilter = filter
                }
            }
        },
        dispose = FileDialog::dispose
    )
}