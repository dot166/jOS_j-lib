package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.os.Bundle
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settingslib.preference.PreferenceFragment

/**
 * jLib Settings activity.
 */
abstract class jConfigActivity : CollapsingToolbarBaseActivity() {
    abstract fun preferenceFragment(): PreferenceFragment

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            supportFragmentManager.beginTransaction()
                .replace(com.android.settingslib.collapsingtoolbar.R.id.content_frame, preferenceFragment()).addToBackStack(null).commit()
        }
    }
}
