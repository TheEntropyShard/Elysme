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

package me.theentropyshard.elysme.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.extensions.lighter
import me.theentropyshard.elysme.ui.theme.Fonts

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    profileImage: String?,
    size: Dp,
    displayName: String,
    contentDescription: String?,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val theModifier = Modifier
        .clip(CircleShape)
        .size(size)
        .then(modifier)

    if (profileImage != null) {
        KamelImage(
            modifier = theModifier,
            resource = { asyncPainterResource(data = File(profileImage), filterQuality = FilterQuality.High) },
            contentDescription = contentDescription,
        )
    } else {
        NoProfileImage(
            modifier = theModifier,
            fontSize = (size.value / 1.5).sp,
            color = color,
            displayName = displayName,
            fontFamily = Fonts.googleSans()
        )
    }
}

@Composable
private fun NoProfileImage(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    color: Color,
    displayName: String,
    fontFamily: FontFamily
) {
    val text = "${displayName[0]}"
    val measurer = rememberTextMeasurer()

    val measuredText = measurer.measure(
        text = text,
        style = LocalTextStyle.current.copy(fontSize = fontSize, lineHeight = fontSize)
    )

    Canvas(modifier = modifier) {
        drawCircle(
            brush = Brush.linearGradient(
                0.3f to color.lighter(),
                0.7f to color
            )
        )

        val canvasWidth = size.width
        val canvasHeight = size.height
        val textWidth = measuredText.size.width.toFloat()
        val textHeight = measuredText.size.height.toFloat()

        val topLeftOffset = Offset(
            x = (canvasWidth - textWidth) / 2f,
            y = (canvasHeight - textHeight) / 2f
        )

        drawText(
            text = text,
            textMeasurer = measurer,
            topLeft = topLeftOffset,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = fontFamily,
                fontSize = fontSize,
                drawStyle = Fill
            )
        )

        drawText(
            text = text,
            textMeasurer = measurer,
            topLeft = topLeftOffset,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = fontFamily,
                fontSize = fontSize,
                drawStyle = Stroke(width = 1.0f, miter = 1.0f)
            )
        )
    }
}