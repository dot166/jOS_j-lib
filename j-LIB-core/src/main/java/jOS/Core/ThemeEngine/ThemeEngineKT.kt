package jOS.Core.ThemeEngine

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import jOS.Core.ThemeEngine.ThemeEngine.themeClass
import jOS.Core.utils.VersionUtils

/**
 * Internal AndroidX Compose Extension for ThemeEngine
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class ThemeEngineKT {
    companion object {

        @JvmStatic
        fun getLightColourScheme(context: Context): ColorScheme {
            return if (VersionUtils.Android.isAtLeastS()) {
                dynamicLightColorScheme(context)
            } else {
                if (themeClass != null && themeClass.LColourScheme() != null) {
                    themeClass.LColourScheme();
                }
                lightColorScheme()
            }
        }

        @JvmStatic
        fun getDarkColourScheme(context: Context): ColorScheme {
            return if (VersionUtils.Android.isAtLeastS()) {
                dynamicDarkColorScheme(context)
            } else {
                if (themeClass != null && themeClass.DColourScheme() != null) {
                    themeClass.DColourScheme();
                }
                darkColorScheme()
            }
        }
    }
}