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

package me.theentropyshard.elysme.extensions

import androidx.compose.ui.graphics.Color

fun Color.toHSV(): FloatArray {
    val hsv = FloatArray(3)

    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val diff = max - min

    hsv[2] = max

    if (max != 0f) {
        hsv[1] = diff / max
    } else {
        hsv[1] = 0f
    }

    if (diff == 0f) {
        hsv[0] = 0f
    } else {
        var hue = when (max) {
            red -> (green - blue) / diff + (if (green < blue) 6f else 0f)
            green -> (blue - red) / diff + 2f
            blue -> (red - green) / diff + 4f
            else -> 0f
        }
        hue /= 6f
        hsv[0] = hue * 360f
    }

    return hsv
}

fun Color.lighter(percent: Float = 0.2f): Color {
    val hsv = toHSV()

    hsv[2] = (hsv[2] + percent).coerceIn(0f, 1f)

    return Color.hsv(hsv[0], hsv[1], hsv[2])
}