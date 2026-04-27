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

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.close24dp
import elysme.composeapp.generated.resources.menu24dp
import me.theentropyshard.elysme.ui.theme.Fonts
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatListHeader(
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf("") }

    Row(
        modifier = modifier.padding(start = 6.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {

            }
        ) {
            Icon(
                painter = painterResource(Res.drawable.menu24dp),
                contentDescription = "Menu"
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        TextField(
            value = value,
            onValueChange = { value = it }
        )
    }
}

@Composable
private fun RowScope.TextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    background: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Row(
        modifier = modifier.weight(1f),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontFamily = Fonts.googleSans()),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(background)
                        .padding(start = 12.dp, end = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = value.isEmpty(),
                        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it }),
                    ) {
                        Text(
                            text = "Search",
                            fontFamily = Fonts.googleSans()
                        )
                    }

                    // Drawing "fog" behind clear button but over text field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawWithContent {
                                drawContent()

                                val fogWidth = 64.dp.toPx()
                                val fogOffset = Offset(size.width - fogWidth, 0.0f)
                                val fogStart = 0.5f

                                drawRect(
                                    brush = Brush.linearGradient(
                                        0.0f to Color.Transparent, fogStart to background,
                                        start = fogOffset
                                    ),
                                    topLeft = fogOffset,
                                    size = Size(fogWidth, size.height)
                                )
                            }
                    ) {
                        innerTextField()
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        visible = value.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onValueChange("") }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.close24dp),
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            }
        )
    }
}