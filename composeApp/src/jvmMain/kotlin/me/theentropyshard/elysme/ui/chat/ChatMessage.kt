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

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.edit24dp
import elysme.composeapp.generated.resources.read24dp
import elysme.composeapp.generated.resources.unread24dp
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.theentropyshard.elysme.deltachat.model.DcMessage
import me.theentropyshard.elysme.deltachat.model.DcQuote
import me.theentropyshard.elysme.deltachat.model.DcReactions
import me.theentropyshard.elysme.ui.theme.otherMessageColorDark
import me.theentropyshard.elysme.ui.theme.otherMessageColorLight
import me.theentropyshard.elysme.utils.NoMaxSizeImage
import org.jetbrains.compose.resources.painterResource
import kotlin.math.max
import kotlin.math.min

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: DcMessage,
    onReplyClick: (Int) -> Unit
) {
    val displayName = message.sender?.displayName ?: "<unknown user>"
    val profileImage = message.sender?.profileImage
    val myself = displayName == "Me"
    val read = message.state == 28

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (myself) Arrangement.End else Arrangement.Start
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (!myself) {
                if (profileImage != null) {
                    KamelImage(
                        modifier = Modifier.size(32.dp).clip(CircleShape),
                        resource = { asyncPainterResource(data = File(profileImage)) },
                        contentDescription = "Avatar of user $displayName",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        alpha = DefaultAlpha,
                        colorFilter = null,
                        onLoading = null,
                        onFailure = null,
                        contentAlignment = Alignment.Center,
                        animationSpec = tween(),
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
            }

            BoxWithConstraints {
                Card(
                    modifier = Modifier
                        .widthIn(min = 200.dp, max = maxWidth * 0.85f)
                        .width(IntrinsicSize.Max),
                    colors = CardDefaults.cardColors(
                        containerColor = if (myself) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            if (false) {
                                otherMessageColorDark
                            } else {
                                otherMessageColorLight
                            }
                        }
                    )
                ) {
                    Column(
                        modifier = if (myself) {
                            Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 8.dp)
                        } else {
                            Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 4.dp)
                        }
                    ) {
                        if (!myself) {
                            Text(
                                text = displayName,
                                fontWeight = FontWeight.SemiBold,
                                color = if (message.sender != null) {
                                    Color(0xFF000000 or message.sender.color.substring(1).toLong(16))
                                } else {
                                    Color.Unspecified
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        val hasQuote = message.hasQuote()
                        val hasText = message.hasText()
                        val hasAttachment = message.hasAttachment()

                        if (hasQuote) {
                            ReplyView(
                                modifier = Modifier.fillMaxWidth(),
                                reply = message.quote
                            ) { onReplyClick(message.quote.messageId) }
                        }

                        if (hasText) {
                            Text(
                                text = message.text
                            )
                        } else if (hasQuote && hasAttachment) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (hasAttachment) {
                            if (message.fileMime.startsWith("image/")) {
                                val aspectRatio = message.dimensionsWidth.toFloat() / message.dimensionsHeight.toFloat()

                                val desiredWidth = if (aspectRatio > 1.0f) 640 else 360
                                val desiredHeight = if (aspectRatio > 1.0f) 360 else 640

                                val scaleX = desiredWidth.toFloat() / message.dimensionsWidth.toFloat()
                                val scaleY = desiredHeight.toFloat() / message.dimensionsHeight.toFloat()
                                val scale = min(scaleX, scaleY)

                                val newWidth = (message.dimensionsWidth * scale).dp
                                val newHeight = (message.dimensionsHeight * scale).dp

                                ImageAttachment(
                                    modifier = Modifier.size(newWidth, newHeight),
                                    scale = max(scale, 1.0f),
                                    name = message.fileName,
                                    filePath = message.file,
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ReactionsView(reactions = message.reactions)

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (message.isIsEdited) {
                                    Icon(
                                        modifier = Modifier.size(16.dp).padding(top = 2.dp, end = 2.dp),
                                        painter = painterResource(Res.drawable.edit24dp),
                                        contentDescription = "Message was edited",
                                    )
                                }

                                Text(
                                    text = "14:48",
                                    fontSize = 12.sp
                                )

                                if (myself) {
                                    Icon(
                                        modifier = Modifier.size(18.dp).padding(top = 2.dp),
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

@Composable
private fun ImageAttachment(
    modifier: Modifier = Modifier,
    scale: Float,
    name: String,
    filePath: String,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {},
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        NoMaxSizeImage(
            modifier = Modifier.scale(scale),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            resource = {
                asyncPainterResource(data = File(filePath))
            },
            onLoading = {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            },
            onFailure = { throwable ->
                Text(text = "Error: cannot load the image: ${throwable.toString()}")
            }
        )
    }
}

private fun transformReactions(reactions: Map<String, List<String>>): Map<String, List<String>> {
    return mapOf()
}

@Composable
private fun ReactionsView(
    modifier: Modifier = Modifier,
    reactions: DcReactions?
) {
    if (reactions == null || reactions.reactionsByContact == null) return

    FlowRow(modifier = modifier) {
        for (entry in reactions.reactionsByContact) {

        }
    }
}

@Composable
private fun ReactionItem(
    modifier: Modifier = Modifier,
    emoji: String
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = emoji)
    }
}
