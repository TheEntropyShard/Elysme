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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.theentropyshard.elysme.viewmodel.MainViewModel

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    state: TextFieldState,
    quoteName: String? = null,
    quoteText: String? = null,
    quoteColor: String? = null,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {}
) {
    val shape = if (quoteText != null) {
        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp, topStart = 16.dp, topEnd = 16.dp)
    } else {
        RoundedCornerShape(24.dp)
    }

    Column(
        modifier = modifier
            .clip(shape)
            .background(color = TextFieldDefaults.colors().containerColor(enabled = true, isError = false, focused = true))
            .animateContentSize(),
    ) {
        if (quoteText != null) {
            QuoteView(
                modifier = Modifier.padding(8.dp),
                color = quoteColor,
                name = quoteName,
                text = quoteText
            ) { model.cancelReply() }
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
            decorator = TextFieldDefaults.decorator(
                state = state,
                enabled = true,
                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                interactionSource = remember { MutableInteractionSource() },
                outputTransformation = null,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                contentPadding = PaddingValues(top = 12.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        )
    }
}