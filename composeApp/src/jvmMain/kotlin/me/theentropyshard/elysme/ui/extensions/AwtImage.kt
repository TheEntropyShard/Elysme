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

package me.theentropyshard.elysme.ui.extensions

import java.awt.Image
import java.awt.image.BufferedImage

fun Image.toBufferedImage(): BufferedImage {
    val image = BufferedImage(
        this.getWidth(null),
        this.getHeight(null),
        BufferedImage.TYPE_INT_ARGB
    )

    val g2d = image.createGraphics()
    g2d.drawImage(this, 0, 0, null)
    g2d.dispose()

    return image
}