package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settingslib.preference.PreferenceFragment
import io.github.dot166.jlib.R
import io.github.dot166.jlib.utils.VersionUtils.libVersion

/**
 * jLib Settings activity.
 */
open class jConfigActivity : CollapsingToolbarBaseActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    open fun preferenceFragment(): PreferenceFragment {
        return ExamplePrefFragment()
    }

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, preferenceFragment()).commit()
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    class ExamplePrefFragment: PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val screen = preferenceManager.createPreferenceScreen(requireContext())
            val disclaimer = Preference(requireContext())
            disclaimer.title = "This is an example preference screen, please override the preferenceFragment() function to use your own preference fragment"
            screen.addPreference(disclaimer)
            val libPref = Preference(requireContext())
            libPref.setIcon(R.mipmap.ic_launcher_j)
            libPref.setTitle(R.string.jlib_version)
            libPref.setSummary(libVersion)
            libPref.fragment
            screen.addPreference(libPref)
            preferenceScreen = screen
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment!!
        ).apply {
            arguments = pref.extras
            setTargetFragment(caller, 0)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .addToBackStack(null)
            .commit()

        title = pref.title
        return true
    }
}
