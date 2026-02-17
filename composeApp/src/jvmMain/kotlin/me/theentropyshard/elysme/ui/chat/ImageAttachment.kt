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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.utils.File
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.utils.NoMaxSizeImage
import java.awt.Desktop
import kotlin.math.max
import kotlin.math.min

@Composable
fun ImageAttachment(
    modifier: Modifier = Modifier,
    message: DcMessage,
    onClick: () -> Unit = {},
) {
    val aspectRatio = message.dimensionsWidth.toFloat() / message.dimensionsHeight.toFloat()

    val desiredWidth = if (aspectRatio > 1.0f) 640 else 360
    val desiredHeight = if (aspectRatio > 1.0f) 360 else 640

    val scaleX = desiredWidth.toFloat() / message.dimensionsWidth.toFloat()
    val scaleY = desiredHeight.toFloat() / message.dimensionsHeight.toFloat()
    var scale = min(scaleX, scaleY)

    var newWidth = (message.dimensionsWidth * scale).dp
    var newHeight = (message.dimensionsHeight * scale).dp

    var contentScale = ContentScale.Crop

    if (
        message.dimensionsWidth.dp < 640.dp &&
        message.dimensionsHeight.dp < 360.dp &&
        aspectRatio > 1.0f
    ) {
        newWidth = message.dimensionsWidth.dp
        newHeight = message.dimensionsHeight.dp
        scale = 1.0f
        contentScale = ContentScale.Fit
    } else if (
        message.dimensionsWidth.dp < 360.dp &&
        message.dimensionsHeight.dp < 640.dp &&
        aspectRatio < 1.0f
    ) {
        newHeight = message.dimensionsWidth.dp
        newWidth = message.dimensionsHeight.dp
        scale = 1.0f
        contentScale = ContentScale.Fit
    }

    Surface(
        modifier = modifier
            .size(newWidth, newHeight)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        NoMaxSizeImage(
            modifier = Modifier.scale(max(scale, 1.0f)),
            contentDescription = "Image: ${message.fileName}",
            contentScale = contentScale,
            resource = { asyncPainterResource(data = File(message.file)) },
            onLoading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
            onFailure = { throwable -> Text(text = "Error: cannot load the image: ${throwable.toString()}") }
        )
    }
}