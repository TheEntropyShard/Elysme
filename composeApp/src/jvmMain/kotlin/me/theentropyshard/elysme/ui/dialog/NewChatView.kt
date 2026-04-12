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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.gson.reflect.TypeToken
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.add24dp
import elysme.composeapp.generated.resources.addbig24dp
import elysme.composeapp.generated.resources.qrcodetwo24dp
import me.theentropyshard.elysme.deltachat.model.DcContact
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.parent
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.ElysmeDialog
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewChatView(model: MainViewModel) {
    val request = RpcMethod.get_contacts.makeRequest()
    request.addParam(model.currentAccount!!.id)
    request.addParam(2) // listFlags: DC_GCL_ADD_SELF
    request.addParam(null) // query

    val contacts = model.gson.fromJson(model.rpc.send(request).result, object : TypeToken<Array<DcContact>>() {})

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
            LazyColumn {
                item(key = "scan_qr") {
                    SpecialItem(
                        icon = Res.drawable.qrcodetwo24dp,
                        text = "New contact",
                        onClick = {}
                    )
                }

                item(key = "new_group") {
                    SpecialItem(
                        icon = Res.drawable.add24dp,
                        text = "New group",
                        onClick = {}
                    )
                }

                item(key = "divider") {
                    SpecialDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 3.dp,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                for (contact in contacts) {
                    item(key = contact.id) {
                        ContactListItem(
                            contact = contact,
                            onClick = {

                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpecialDivider(
    modifier: Modifier = Modifier,
    color: Color,
    thickness: Dp,
) {
    Canvas(modifier.fillMaxWidth().height(thickness)) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            start = Offset(0f, thickness.toPx() / 2),
            end = Offset(size.width, thickness.toPx() / 2),
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun SpecialItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .pointerHoverIcon(icon = PointerIcon.Hand)
            .fillMaxWidth()
            .clickable { onClick() }
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(4.dp),
            painter = painterResource(icon),
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.googleSans()
        )
    }
}
