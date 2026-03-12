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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theentropyshard.elysme.ui.ChooseFileDialog
import me.theentropyshard.elysme.ui.theme.Fonts

@Composable
fun ImportBackupView(
    modifier: Modifier = Modifier,
    onFileSelected: (String) -> Unit
) {
    var dialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You don't have any accounts set up",
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontFamily = Fonts.googleSans(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { dialogVisible = true },
        ) {
            Text(
                text = "Click to import a backup",
                fontFamily = Fonts.googleSans(),
            )
        }
    }

    if (dialogVisible) {
        ChooseFileDialog(filter = { _, name -> name.endsWith(".tar") || name.endsWith(".bak") }) {
            onFileSelected(it[0].absolutePath)
        }
    }
}
