package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settingslib.preference.PreferenceFragment

/**
 * jLib Settings activity.
 */
abstract class jConfigActivity : CollapsingToolbarBaseActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    abstract fun preferenceFragment(): PreferenceFragment

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(
                com.android.settingslib.collapsingtoolbar.R.id.content_frame,
                preferenceFragment()
            ).addToBackStack(null).commit()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val args: Bundle = pref.getExtras()
        val fragment: Fragment = supportFragmentManager.getFragmentFactory().instantiate(
            classLoader, pref.fragment!!
        )
        fragment.setArguments(args)
        supportFragmentManager.beginTransaction().setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
            .replace(com.android.settingslib.collapsingtoolbar.R.id.content_frame, fragment)
            .addToBackStack(null).commit()
        return true
    }
}
