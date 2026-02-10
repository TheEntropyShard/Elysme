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

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        BasicTextField(
            modifier = Modifier.minimumInteractiveComponentSize().fillMaxWidth(),
            state = state,
            decorator = TextFieldDefaults.decorator(
                state = state,
                enabled = true,
                lineLimits = TextFieldLineLimits.MultiLine(),
                interactionSource = remember { MutableInteractionSource() },
                outputTransformation = null,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        )
    }
}