package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RestrictTo
import androidx.core.view.WindowCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.dot166.jlib.LIBAboutActivity
import io.github.dot166.jlib.R
import io.github.dot166.jlib.utils.VersionUtils.libVersion

/**
 * jLib Settings activity.
 */
open class jConfigActivity : jActivity() {
    open fun preferenceFragment(): PreferenceFragmentCompat {
        return ExamplePrefFragment()
    }

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.github.dot166.jlib.R.layout.settings_activity)
        setSupportActionBar(findViewById(io.github.dot166.jlib.R.id.actionbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            supportFragmentManager.beginTransaction()
                .replace(io.github.dot166.jlib.R.id.content_frame, preferenceFragment()).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    class ExamplePrefFragment: PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.jlib_example_preference_screen, rootKey)
            val screen = preferenceScreen
            val disclaimer = Preference(requireContext())
            disclaimer.title = "This is an example preference screen, please override the preferenceFragment() function to use your own preference fragment"
            screen.addPreference(disclaimer)
            val libPref = Preference(requireContext())
            libPref.setIcon(R.mipmap.ic_launcher_j)
            libPref.setTitle(io.github.dot166.jlib.R.string.jlib_version)
            libPref.setSummary(libVersion)
            libPref.onPreferenceClickListener =
                Preference.OnPreferenceClickListener { preference: Preference? ->
                    startActivity(Intent(preference!!.context, LIBAboutActivity::class.java))
                    true
                }
            screen.addPreference(libPref)
        }
    }
}
