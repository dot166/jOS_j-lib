package io.github.dot166.jlib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

object ThemeUtils {
    @SuppressLint("ComposableNaming")
    @Composable
    @JvmStatic
    fun jLibComposeTheme(context: Context,
                        content: @Composable () -> Unit) {
        MaterialTheme(colorScheme = getColourScheme(context), shapes = MaterialTheme.shapes, typography = MaterialTheme.typography, content = content)
    }

    private fun getColourScheme(context: Context): ColorScheme {
        val nightModeFlags: Int =
            context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
        if (VersionUtils.isAtLeastS) {
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> return dynamicDarkColorScheme(context)
                Configuration.UI_MODE_NIGHT_NO -> return dynamicLightColorScheme(context)
            }
            return dynamicDarkColorScheme(context) // default dark
        } else {
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> return darkColorScheme()
                Configuration.UI_MODE_NIGHT_NO -> return lightColorScheme()
            }
            return darkColorScheme() // default dark
        }
    }
}