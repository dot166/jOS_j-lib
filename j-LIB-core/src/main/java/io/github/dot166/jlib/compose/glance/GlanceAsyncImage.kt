package io.github.dot166.jlib.compose.glance

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import coil.imageLoader
import coil.request.ImageRequest
import io.github.dot166.jlib.R

@Composable
fun GlanceAsyncImage(model: Any?, contentDescription: String?, modifier: GlanceModifier = GlanceModifier) {
    val context = LocalContext.current
    var bitmapState by remember(model) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(model) {
        if (model == null) return@LaunchedEffect
        try {
            val result = context.imageLoader.execute(
                ImageRequest.Builder(context)
                    .data(model)
                    .allowHardware(false)
                    .build()
            )
            bitmapState = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
        } catch (e: Exception) {}
    }

    if (bitmapState != null) {
        Image(ImageProvider(bitmapState!!), contentDescription, modifier)
    } else {
        Image(ImageProvider(R.drawable.def_art), contentDescription, modifier)
    }
}
