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

package me.theentropyshard.elysme.utils

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.Resource
import io.kamel.image.KamelImageBox

// Copied from io/kamel/image/KamelImage.kt and removed Modifier.fillMaxSize() from Image (why was it there???)
@Composable
fun NoMaxSizeImage(
    resource: @Composable (BoxWithConstraintsScope.() -> Resource<Painter>),
    contentDescription: String?,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: (@Composable BoxScope.(Float) -> Unit)? = null,
    onFailure: (@Composable BoxScope.(Throwable) -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    animationSpec: FiniteAnimationSpec<Float>? = null,
) {
    val onSuccess: @Composable (BoxScope.(Painter) -> Unit) = { painter ->
        Image(
            painter,
            contentDescription,
            imageModifier,
            alignment,
            contentScale,
            alpha,
            colorFilter
        )
    }
    KamelImageBox(
        resource,
        modifier,
        contentAlignment,
        animationSpec,
        onLoading,
        onFailure,
        onSuccess,
    )
}