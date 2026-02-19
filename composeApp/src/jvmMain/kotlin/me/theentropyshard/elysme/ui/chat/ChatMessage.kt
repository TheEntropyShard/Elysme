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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.awtClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.reflect.TypeToken
import elysme.composeapp.generated.resources.*
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.deltachat.model.DcContact
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.deltachat.model.DcReactions
import me.theentropyshard.elysme.deltachat.request.GetContactsByIdsRequest
import me.theentropyshard.elysme.ui.emoji.Emoji
import me.theentropyshard.elysme.extensions.noRippleClickable
import me.theentropyshard.elysme.ui.theme.Fonts
import me.theentropyshard.elysme.viewmodel.ElysmeDialog
import me.theentropyshard.elysme.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import java.awt.Desktop
import java.awt.datatransfer.StringSelection
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.io.File as JavaFile

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: DcMessage,
    model: MainViewModel,
    onReply: (DcMessage) -> Unit,
    onQuoteClick: (Int) -> Unit
) {
    val displayName = message.sender?.displayName ?: "<unknown user>"
    val profileImage = message.sender?.profileImage
    val myself = displayName == "Me"
    val read = message.state == 28

    var hovered by remember { mutableStateOf(false) }

    var menuVisible by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }

    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        hovered = when (event.type) {
                            PointerEventType.Enter -> true
                            PointerEventType.Exit -> false
                            else -> hovered
                        }
                    }
                }
            }
            .fillMaxWidth(),
        horizontalArrangement = if (myself) Arrangement.End else Arrangement.Start
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (!myself) {
                if (profileImage != null) {
                    KamelImage(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .clickable { model.showProfileDialog(message.sender) },
                        resource = { asyncPainterResource(data = File(profileImage)) },
                        contentDescription = "Avatar of user $displayName",
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .pointerHoverIcon(icon = PointerIcon.Hand)
                            .clickable { model.showProfileDialog(message.sender) },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {

                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            val clipboard = LocalClipboard.current

            BoxWithConstraints {
                MessageContextMenu(
                    position = menuOffset,
                    visible = menuVisible,
                    onDismissRequest = { menuVisible = false },
                    items = {
                        listOf(
                            MessageMenuItem(
                                icon = Res.drawable.reply24dp,
                                text = "Reply",
                                description = "Reply to the message",
                                onClick = { onReply(message) }
                            ),
                            MessageMenuItem(
                                icon = Res.drawable.copy24dp,
                                text = "Copy text",
                                description = "Copy text of the message",
                                onClick = { clipboard.awtClipboard?.setContents(StringSelection(message.text), null) }
                            ),
                            MessageMenuItem(
                                icon = Res.drawable.forwardmsg24dp,
                                text = "Forward",
                                description = "Forward the message",
                                onClick = {}
                            ),
                            MessageMenuItem(
                                icon = Res.drawable.delete24dp,
                                text = "Delete",
                                description = "Delete the message",
                                onClick = {}
                            ),
                        )
                    }
                )

                Card(
                    modifier = Modifier
                        .widthIn(min = 200.dp, max = maxWidth * 0.85f)
                        .width(IntrinsicSize.Max)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                matcher = PointerMatcher.pointer(
                                    pointerType = PointerType.Mouse,
                                    button = PointerButton.Secondary
                                )
                            ) { offs ->
                                menuOffset = offs
                                menuVisible = true
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (myself) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Column(
                        modifier = if (myself) {
                            Modifier.padding(start = 8.dp, end = 8.dp, bottom = 0.dp, top = 8.dp)
                        } else {
                            Modifier.padding(start = 8.dp, end = 8.dp, bottom = 0.dp, top = 4.dp)
                        }
                    ) {
                        if (!myself) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier
                                        .pointerHoverIcon(icon = PointerIcon.Hand)
                                        .noRippleClickable { model.showProfileDialog(message.sender) },
                                    text = displayName,
                                    fontFamily = Fonts.googleSans(),
                                    fontWeight = FontWeight.Medium,
                                    color = if (message.sender != null) {
                                        Color(0xFF000000 or message.sender.color.substring(1).toLong(16))
                                    } else {
                                        Color.Unspecified
                                    },
                                )

                                if (hovered) {
                                    var replyHovered by remember { mutableStateOf(false) }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        modifier = Modifier
                                            .pointerInput(Unit) {
                                                awaitPointerEventScope {
                                                    while (true) {
                                                        val event = awaitPointerEvent()

                                                        replyHovered = when (event.type) {
                                                            PointerEventType.Enter -> true
                                                            PointerEventType.Exit -> false
                                                            else -> replyHovered
                                                        }
                                                    }
                                                }
                                            }
                                            .pointerHoverIcon(icon = PointerIcon.Hand)
                                            .noRippleClickable { onReply(message) },
                                        text = "Reply",
                                        textDecoration = if (replyHovered) TextDecoration.Underline else null,
                                        fontFamily = Fonts.googleSans()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        val hasQuote = message.hasQuote()
                        val hasText = message.hasText()
                        val hasAttachment = message.hasAttachment()

                        if (hasQuote) {
                            QuoteView(
                                modifier = Modifier.fillMaxWidth(),
                                color = message.quote.authorDisplayColor,
                                name = message.quote.authorDisplayName,
                                text = message.quote.text
                            ) { onQuoteClick(message.quote.messageId) }
                        }

                        if (hasQuote && hasAttachment) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (hasAttachment) {
                            if (message.fileMime.startsWith("image/")) {
                                ImageAttachment(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    message = message
                                ) {
                                    scope.launch(Dispatchers.IO) {
                                        if (Desktop.isDesktopSupported()) {
                                            Desktop.getDesktop().open(JavaFile(message.file))
                                        }
                                    }
                                }
                            } else {
                                FileAttachment(message = message) {
                                    scope.launch(Dispatchers.IO) {
                                        if (Desktop.isDesktopSupported()) {
                                            Desktop.getDesktop().browse(JavaFile(message.file).parentFile.toURI())
                                        }
                                    }
                                }
                            }
                        }

                        if (hasText) {
                            Text(
                                text = message.text,
                                fontFamily = Fonts.googleSans()
                            )
                        } else if (hasAttachment) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(verticalAlignment = Alignment.Bottom) {
                            ReactionsView(
                                modifier = Modifier.padding(bottom = 8.dp),
                                reactions = message.reactions,
                                model = model
                            )

                            Spacer(modifier = Modifier.weight(1f).width(16.dp))

                            Row(
                                modifier = Modifier.padding(bottom = 6.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (message.isIsEdited) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(Res.drawable.edit24dp),
                                        contentDescription = "Message was edited",
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))
                                }

                                Text(
                                    text = FORMATTER.format(
                                        Instant.ofEpochSecond(message.timestamp).atZone(ZoneId.systemDefault())
                                    ),
                                    fontFamily = Fonts.googleSans(),
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp
                                )

                                if (myself) {
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        painter = painterResource(if (read) Res.drawable.read24dp else Res.drawable.unread24dp),
                                        contentDescription = "Message status indicator",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

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
private fun ReactionsView(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    reactions: DcReactions?
) {
    if (reactions == null || reactions.reactionsByContact == null) return

    val transformed = transformReactions(reactions.reactionsByContact)

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (entry in transformed) {
            ReactionItem(
                model = model,
                emoji = entry.key,
                contacts = entry.value
            )
        }
    }
}

@Composable
private fun ReactionItem(
    modifier: Modifier = Modifier,
    model: MainViewModel,
    emoji: String,
    contacts: Set<String>,
) {
    val request = GetContactsByIdsRequest().apply {
        setAccountId(model.currentAccountId)
        setContactIds(contacts.map { it.toInt() })
    }

    val realContacts =
        model.gson.fromJson(model.rpc.send(request).result, object : TypeToken<Map<String, DcContact>>() {})

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Emoji(
            modifier = Modifier.size(20.dp),
            emoji = emoji
        )

        Spacer(modifier = Modifier.width(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-5).dp)
        ) {
            for (string in contacts) {
                val contact = realContacts[string]

                if (contact != null) {
                    KamelImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp),
                        resource = { asyncPainterResource(data = File(contact.profileImage)) },
                        contentDescription = ""
                    )
                }
            }
        }
    }
}
