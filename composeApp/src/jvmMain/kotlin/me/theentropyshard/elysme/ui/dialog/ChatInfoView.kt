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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.close24dp
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.deltachat.model.DcContact
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChatInfoView(model: MainViewModel) {
    val chat = model.currentChat!!

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
            if (chat.profileImage != null) {
                KamelImage(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    resource = {
                        asyncPainterResource(
                            data = File(chat.profileImage),
                            filterQuality = FilterQuality.High
                        )
                    },
                    contentDescription = "Chat profile image - ${chat.name}",
                )
            } else {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = chat.name,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                fontFamily = Fonts.googleSans()
            )

            if (chat.chatType == "Group" || chat.chatType == "OutBroadcast") {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${chat.contactIds.size} members",
                    fontFamily = Fonts.googleSans()
                )

                Spacer(modifier = Modifier.height(8.dp))

                val request = RpcMethod.get_contacts_by_ids.makeRequest()
                request.addParam(model.currentAccountId)
                request.addParam(chat.contactIds)

                val contactsObject =
                    Gson().fromJson(
                        model.rpc.send(request).result,
                        object : TypeToken<Map<String, DcContact>>() {})

                LazyColumn {
                    items(ArrayList(contactsObject.values)) {
                        ContactListItem(contact = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: DcContact
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .clickable {}
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        if (contact.profileImage != null) {
            KamelImage(
                modifier = Modifier.size(32.dp).clip(CircleShape),
                resource = {
                    asyncPainterResource(
                        data = File(contact.profileImage),
                        filterQuality = FilterQuality.High
                    )
                },
                contentDescription = "Contact profile image - ${contact.displayName}",
            )
        } else {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {

            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = contact.displayName,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.googleSans()
        )

        if (contact.displayName != "Me") {
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(Res.drawable.close24dp),
                    contentDescription = "Remove contact"
                )
            }
        }
    }
}
