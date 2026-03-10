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

package me.theentropyshard.elysme.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel

@Composable
fun ProfileInfoView(model: MainViewModel) {
    val contact = model.currentContact!!

    Surface(
        modifier = Modifier
            .padding(vertical = 48.dp)
            .width(384.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(
                profileImage = contact.profileImage,
                size = 64.dp,
                color = contact.color.toColor(),
                contentDescription = "User profile image - ${contact.name}",
                displayName = contact.displayName
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contact.displayName,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                fontFamily = Fonts.googleSans()
            )

            Text(
                text = contact.status,
                fontFamily = Fonts.googleSans()
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Text(
                    text = "Send message",
                    fontFamily = Fonts.googleSans(),
                )
            }
        }
    }
}