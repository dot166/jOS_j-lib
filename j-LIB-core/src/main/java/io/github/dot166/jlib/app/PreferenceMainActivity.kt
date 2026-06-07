package io.github.dot166.jlib.app

import android.app.ActivityOptions
import android.content.Intent
import android.view.Display
import com.android.settingslib.spa.framework.BrowseActivity

class PreferenceMainActivity: BrowseActivity() {
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
}
