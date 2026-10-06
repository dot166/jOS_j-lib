package io.github.dot166.jlib.compose

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.scheme.DynamicScheme

@Composable
fun JLibAppTheme(
    content: @Composable () -> Unit,
) {
    val darkTheme = isSystemInDarkTheme()

    val colors = neutralDynamicColorScheme(
        primary = getSystemAccent(),
        isDark = darkTheme,
    ).run {
        copy(surface = surfaceContainer)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

fun neutralDynamicColorScheme(
    primary: Color,
    isDark: Boolean,
    isAmoled: Boolean = false,
    secondary: Color? = null,
    tertiary: Color? = null,
    neutral: Color? = null,
    neutralVariant: Color? = null,
    error: Color? = null,
    style: PaletteStyle = PaletteStyle.Expressive,
    contrastLevel: Double = Contrast.Default.value,
    specVersion: ColorSpec.SpecVersion = ColorSpec.SpecVersion.SPEC_2025,
    platform: DynamicScheme.Platform = DynamicScheme.Platform.Default,
    modifyColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): ColorScheme =
    dynamicColorScheme(
        seedColor = primary,
        isDark = isDark,
        isAmoled = isAmoled,
        primary = primary,
        secondary = secondary,
        tertiary = tertiary,
        neutral = neutral,
        neutralVariant = neutralVariant,
        error = error,
        style = style,
        contrastLevel = contrastLevel,
        specVersion = specVersion,
        platform = platform,
        modifyColorScheme = modifyColorScheme,
    )

@Composable
fun getSystemAccent(): Color {
    val context = LocalContext.current
    val resources = LocalResources.current

    return Color(resources.getColor(android.R.color.system_accent1_500, context.theme))
}