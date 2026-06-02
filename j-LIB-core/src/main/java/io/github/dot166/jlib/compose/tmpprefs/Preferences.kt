package io.github.dot166.jlib.compose.tmpprefs

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.fragment.compose.AndroidFragment
import androidx.preference.PreferenceScreen
import com.android.settingslib.preference.PreferenceFragment
import io.github.dot166.jlib.app.SettingsLibComposeTheme

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun PreferencePreview() {
    SettingsLibComposeTheme {
        Preference("A", "B")
    }
}

@Composable
fun Preference(title: String, subtitle: String = "", painter: Painter? = null) {
    Surface(
        shape = RoundedCornerShape(16.dp), // or RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, ...)
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // image goes here
                if (painter != null) {
                    Image(painter, null, modifier = Modifier
                        .size(48.dp)) // TODO: Support ContentDescriptions
                } else {
                    Spacer(Modifier.size(48.dp))
                }
                Column {
                    Text(text = title, style = MaterialTheme.typography.headlineMedium)
                    if (subtitle.isNotBlank()) {
                        Text(text = subtitle, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Deprecated("android is now compose first")
@Composable
fun LegacyPreference(
    preferenceProvider: (PreferenceScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidFragment(
        clazz = PrefFragment::class.java,
        modifier = modifier.fillMaxWidth()
    ) { fragment ->
        fragment.setSetupCallback(preferenceProvider)
    }
}

internal class PrefFragment : PreferenceFragment() {
    private var setupCallback: ((PreferenceScreen) -> Unit)? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)
        preferenceScreen = screen
        setupCallback?.invoke(screen)
    }

    fun setSetupCallback(callback: (PreferenceScreen) -> Unit) {
        setupCallback = callback
        preferenceScreen?.let { callback(it) }
    }
}