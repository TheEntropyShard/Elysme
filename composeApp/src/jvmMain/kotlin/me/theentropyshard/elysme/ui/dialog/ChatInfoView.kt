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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.close24dp
import me.theentropyshard.elysme.deltachat.model.DcContact
import me.theentropyshard.elysme.deltachat.rpc.RpcMethod
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.ElysmeDialog
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
            ProfileImage(
                profileImage = chat.profileImage,
                size = 64.dp,
                color = chat.color.toColor(),
                contentDescription = "Chat profile image - ${chat.name}",
                displayName = chat.name
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = chat.name,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                fontFamily = Fonts.googleSans()
            )

            val request = RpcMethod.get_chat_description.makeRequest()
            request.addParam(model.currentAccountId)
            request.addParam(chat.id)

            val description = model.rpc.send(request).result.asString

            if (description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                SelectionContainer(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = description,
                        fontFamily = Fonts.googleSans(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

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
                    model.gson.fromJson(
                        model.rpc.send(request).result,
                        object : TypeToken<Map<String, DcContact>>() {})

                LazyColumn {
                    items(ArrayList(contactsObject.values)) {
                        ContactListItem(
                            contact = it,
                            onClick = {
                                model.currentContact = it
                                model.dialog = ElysmeDialog.ProfileInfoDialog
                                model.dialogVisible = true
                            },
                            trailingIcon = {
                                if (it.displayName != "Me") {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            painter = painterResource(Res.drawable.close24dp),
                                            contentDescription = "Remove contact"
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: DcContact,
    trailingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit,
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

        ProfileImage(
            profileImage = contact.profileImage,
            size = 32.dp,
            color = contact.color.toColor(),
            contentDescription = "Contact profile image - ${contact.name}",
            displayName = contact.displayName
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = contact.displayName,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.googleSans()
        )

        Spacer(modifier = Modifier.weight(1f))

        trailingIcon()
    }
}
