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

package me.theentropyshard.elysme

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import elysme.composeapp.generated.resources.Res
import elysme.composeapp.generated.resources.sheet_google_64
import kotlinx.coroutines.launch
import me.theentropyshard.elysme.ui.theme.ElysmeTheme
import net.fellbaum.jemoji.EmojiManager
import org.jetbrains.compose.resources.imageResource
import java.awt.Frame
import java.lang.String.join
import java.util.regex.Pattern
import kotlin.text.Charsets.UTF_8

lateinit var parent: Frame
lateinit var sheet: ImageBitmap
val emojis: MutableMap<String?, JsonObject?> = HashMap()
val emojiPattern = Pattern.compile("&#x(\\w+);")

fun getEmojiInfo(emoji: String): Pair<Int, Int>? {
    val code = EmojiManager.getEmoji(emoji).get().htmlHexadecimalCode
    val matcher = emojiPattern.matcher(code)
    val s: MutableList<String?> = ArrayList()
    while (matcher.find()) {
        val m = matcher.group(1)
        val length = m.length
        if (length <= 4) {
            s.add("0000".substring(length) + m)
        } else {
            s.add(m)
        }
    }

    val obj = emojis[join("-", s)]

    if (obj != null) {
        val sheetX = obj.get("sheet_x").asInt
        val sheetY = obj.get("sheet_y").asInt
        val x = (sheetX * (64 + 2)) + 1
        val y = (sheetY * (64 + 2)) + 1

        return Pair(x, y)
    } else {
        return null
    }
}

fun main() {
    application {
        sheet = imageResource(Res.drawable.sheet_google_64)

        val scope = rememberCoroutineScope()

        scope.launch {
            val bytes = Res.readBytes("files/emoji.json").toString(UTF_8)

            val jsonArray = Gson().fromJson(bytes, JsonArray::class.java)

            for (element in jsonArray) {
                val obj: JsonObject = element.getAsJsonObject()

                val nonQualified = obj.get("non_qualified")

                if (!nonQualified.isJsonNull) {
                    emojis[nonQualified.asString] = obj
                }

                emojis[obj.get("unified").asString] = obj
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(1280.dp, 720.dp)),
            title = "Elysme"
        ) {
            parent = window

            ElysmeTheme {
                App()
            }
        }
    }
}

/*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.theentropyshard.elysme.ui.theme.ElysmeTheme
import java.awt.Frame
import kotlin.system.exitProcess

lateinit var parent: Frame

fun main() = application {
    ElysmeTheme {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(1280.dp, 720.dp)),
            undecorated = true,
            title = "Elysme"
        ) {
            parent = window

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                WindowDraggableArea {
                    TitleBar(title = "Elysme")
                }

                App(modifier = Modifier.padding(8.dp).clip(RoundedCornerShape(16.dp)))
            }
        }
    }
}

@Composable
fun WindowScope.TitleBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.DarkGray)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { /* Minimize logic */ }) { Text("—") }
            Button(onClick = { if (window is Frame) (window as Frame).extendedState = Frame.ICONIFIED }) { Text("◻") }
            Button(onClick = { exitProcess(0) }) { Text("✕") }
        }
    }
}

// see https://stackoverflow.com/questions/51008461/javafx-minimizing-maximing-undecorated-stage-with-animations

*/