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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.reflect.TypeToken
import me.theentropyshard.elysme.deltachat.model.DcContact
import me.theentropyshard.elysme.deltachat.model.DcReactions
import me.theentropyshard.elysme.deltachat.request.GetContactsByIdsRequest
import me.theentropyshard.elysme.extensions.toColor
import me.theentropyshard.elysme.ui.components.ProfileImage
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.MainViewModel

/**
 * Accepts a map where keys are contact IDs and values are lists of reactions
 *
 * Returns a map where keys are reactions and values are sets of contact IDs
 */
private fun transformReactions(reactions: Map<String, List<String>>): Map<String, Set<String>> {
    val ret = mutableMapOf<String, MutableSet<String>>()

    for (reactionSet in reactions.values) {
        for (reaction in reactionSet) {
            ret.computeIfAbsent(reaction) { mutableSetOf() }
        }
    }

    for (r in ret.keys) {
        for (entry in reactions) {
            if (r in entry.value) {
                ret[r]!! += entry.key
            }
        }
    }

    return ret
}

@Composable
fun ReactionsView(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    reactions: DcReactions?
) {
    if (reactions == null || reactions.reactionsByContact == null) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (reactions.reactionsByContact.isNotEmpty()) {
            for (entry in transformReactions(reactions.reactionsByContact)) {
                ContactReactionItem(
                    model = model,
                    emoji = entry.key,
                    contacts = entry.value
                )
            }
        } else {
            for (reaction in reactions.reactions) {
                ChannelReactionItem(count = reaction.count, emoji = reaction.emoji)
            }
        }
    }
}

@Composable
fun ChannelReactionItem(
    modifier: Modifier = Modifier,
    count: Int,
    emoji: String
) {
    ReactionContainer(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = emoji,
            lineHeight = 20.sp,
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            modifier = Modifier.padding(end = 2.dp),
            text = "$count",
            fontFamily = Fonts.googleSans(),
            lineHeight = 20.sp,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun ContactReactionItem(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    emoji: String,
    contacts: Set<String>,
) {
    val request = GetContactsByIdsRequest().apply {
        setAccountId(model.currentAccount!!.id)
        setContactIds(contacts.map { it.toInt() })
    }

    val realContacts =
        model.gson.fromJson(model.rpc.send(request).result, object : TypeToken<Map<String, DcContact>>() {})

    ReactionContainer(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = emoji,
            lineHeight = 20.sp,
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.width(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-5).dp)
        ) {
            for (string in contacts) {
                val contact = realContacts[string]

                if (contact != null) {
                    ProfileImage(
                        profileImage = contact.profileImage,
                        size = 20.dp,
                        displayName = contact.displayName,
                        contentDescription = null,
                        color = contact.color.toColor()
                    )
                }
            }
        }
    }
}

@Composable
private fun ReactionContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(start = 6.dp, top = 2.dp, bottom = 2.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}
