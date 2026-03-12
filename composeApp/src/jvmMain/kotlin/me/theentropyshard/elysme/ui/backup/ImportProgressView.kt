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

package me.theentropyshard.elysme.ui.backup

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel

@Composable
fun ImportProgressView(model: MainViewModel) {
    val state by model.importState.collectAsState()

    Surface(
        modifier = Modifier
            .padding(vertical = 48.dp)
            .width(384.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fontFamily = Fonts.googleSans()

            if (state.isImporting) {
                Text(
                    text = "Importing a backup, please wait",
                    fontFamily = fontFamily
                )
            } else if (state.error) {
                Text(
                    text = "Error while importing backup",
                    fontFamily = fontFamily
                )
            } else if (state.started) {
                Text(
                    text = "Importing finished successfully",
                    fontFamily = fontFamily
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = if (state.error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                trackColor = if (state.error) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                progress = { state.progress },
            )

            if (!state.isImporting && !state.error && state.started) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { model.dialogVisible = false }) {
                    Text(
                        text = "Finish",
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}