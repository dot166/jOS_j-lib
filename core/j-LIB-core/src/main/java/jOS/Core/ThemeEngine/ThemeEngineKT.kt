package jOS.Core.ThemeEngine

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import jOS.Core.utils.VersionUtils

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ThemeEngineKT {
    companion object {

        @JvmStatic
        fun getLightColourScheme(context: Context): ColorScheme {
            return if (VersionUtils.Android.isAtLeastS()) {
                dynamicLightColorScheme(context)
            } else {
                lightColorScheme()
            }
        }

        @JvmStatic
        fun getDarkColourScheme(context: Context): ColorScheme {
            return if (VersionUtils.Android.isAtLeastS()) {
                dynamicDarkColorScheme(context)
            } else {
                darkColorScheme()
            }
        }
    }
}