package io.github.dot166.jlib.app

import androidx.compose.runtime.Composable
import com.android.settingslib.spa.framework.theme.SettingsTheme

@Deprecated(message = "Please use SettingsTheme directly", replaceWith = ReplaceWith("SettingsTheme", "com.android.settingslib.spa.framework.theme.SettingsTheme"))
@Composable
fun SettingsLibComposeTheme(
    content: @Composable () -> Unit,
) {
    SettingsTheme(
        content = content,
    )
}