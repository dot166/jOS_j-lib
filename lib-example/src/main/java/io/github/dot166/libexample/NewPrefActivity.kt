package io.github.dot166.libexample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import io.github.dot166.jlib.app.SettingsLibComposeTheme
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.compose.tmpprefs.LegacyPreference
import io.github.dot166.jlib.compose.tmpprefs.Preference

class NewPrefActivity: jActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsLibComposeTheme {
                Column {
                    Preference("test1")
                    LegacyPreference(preferenceProvider = { preferenceScreen ->
                        val context = preferenceScreen.context
                        val pref = androidx.preference.Preference(context).apply {
                            title = "test2"
                        }
                        preferenceScreen.addPreference(pref)
                    })
                    Preference("test3")
                }
            }
        }
    }
}