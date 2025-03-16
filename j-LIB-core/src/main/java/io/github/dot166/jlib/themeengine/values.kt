package io.github.dot166.jlib.themeengine

import android.content.Context
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

interface values {
    fun jLibTheme(): Int

    fun M3(): Int

    @Composable
    fun composeTypography(context: Context?): Typography?

    @Composable
    fun composeShapes(context: Context?): Shapes?
}
