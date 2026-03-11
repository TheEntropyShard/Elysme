package me.theentropyshard.elysme.workarounds

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastRoundToInt
import coil3.BitmapImage
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.asPainter
import coil3.request.ImageRequest
import coil3.toBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.FilterMipmap
import org.jetbrains.skia.FilterMode
import org.jetbrains.skia.MipmapMode
import org.jetbrains.skia.Image as SkiaImage

// Copied from https://github.com/coil-kt/coil/issues/2883#issuecomment-3726278317 and added ContentScale

@Composable
fun PlatformImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalPlatformContext.current
    val imageState = remember { mutableStateOf<Painter?>(null) }

    LaunchedEffect(model) {
        val request = ImageRequest.Builder(context).data(model).build()
        val loader = SingletonImageLoader.get(context)
        val painter = when (val image = loader.execute(request).image) {
            is BitmapImage -> ScaledBitmapPainter(image)
            else -> image?.asPainter(context)
        }
        imageState.value = painter
    }

    Image(
        imageState.value ?: ColorPainter(Color.Unspecified),
        contentDescription,
        modifier,
        contentScale = contentScale
    )
}


class ScaledBitmapPainter(
    val image: coil3.Image,
    val filterQuality: FilterQuality = FilterQuality.Low
) : Painter() {
    override val intrinsicSize: Size
        get() = Size(image.width.toFloat(), image.height.toFloat())

    override fun DrawScope.onDraw() {
        val size = IntSize(
            size.width.fastRoundToInt(),
            size.height.fastRoundToInt(),
        )
        val bitmap = SkiaImage.makeFromBitmap(
            image.toBitmap().asComposeImageBitmap().asSkiaBitmap()
        ).scale(size.width, size.height)

        drawImage(
            bitmap,
            IntOffset.Zero,
            size,
            dstSize = size,
            alpha = 1.0f,
            colorFilter = null,
            filterQuality = filterQuality,
        )
    }

    fun SkiaImage.scale(width: Int, height: Int): ImageBitmap {
        val bitmap = Bitmap()
        bitmap.allocN32Pixels(width, height)
        scalePixels(bitmap.peekPixels()!!, FilterMipmap(FilterMode.LINEAR, MipmapMode.LINEAR), false)
        return SkiaImage.makeFromBitmap(bitmap).toComposeImageBitmap()
    }
}