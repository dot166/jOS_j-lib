package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Display
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

    override fun startActivity(intent: Intent) {
        val options = ActivityOptions.makeBasic()
        options.setLaunchDisplayId(Display.DEFAULT_DISPLAY) // Forces it to the main screen
        startActivity(intent, options.toBundle())
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        val options = ActivityOptions.makeBasic()
        options.setLaunchDisplayId(Display.DEFAULT_DISPLAY) // Forces it to the main screen
        startActivityForResult(intent, requestCode, options.toBundle())
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
