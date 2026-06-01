package io.github.dot166.jlib.compose.tmpprefs

import android.os.Bundle
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.compose.AndroidFragment
import androidx.preference.PreferenceScreen
import com.android.settingslib.preference.PreferenceFragment

@Composable
fun Preference(title: String) {
    Row {
        Text(title)
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
        modifier = modifier
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