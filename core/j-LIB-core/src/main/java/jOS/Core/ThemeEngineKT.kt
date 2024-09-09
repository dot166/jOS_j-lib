package jOS.Core

import android.content.Context
import android.os.Build
import androidx.annotation.RestrictTo
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ThemeEngineKT {
    companion object {

        @JvmStatic
        fun getLightColourScheme(context: Context): ColorScheme {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(context)
            } else {
                lightColorScheme()
            }
        }

        @JvmStatic
        fun getDarkColourScheme(context: Context): ColorScheme {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicDarkColorScheme(context)
            } else {
                darkColorScheme()
            }
        }
    }
}