package jOS.Core

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import jOS.Core.ThemeEngine.currentTheme
import jOS.Core.ThemeEngine.getSystemTheme
import jOS.Core.ThemeEngine.getThemeFromDB1

open class jActivity : AppCompatActivity() {
    var app_name: Boolean = false
    var actionbar: Boolean = false
    var layout: Int = 0
    var icon: Boolean = false
    var home: Boolean = false
    var configured: Boolean = false
    protected var mSDKApp: jSDKCoreApp? = null

    protected fun configure(
        layout: Int,
        home: Boolean,
        actionbar: Boolean = true,
        app_name: Boolean = true,
        icon: Boolean = true
    ) {
        this.app_name = app_name
        this.layout = layout
        this.actionbar = actionbar
        this.icon = icon
        this.home = home
        this.configured = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        check(configured) { "configure() not called prior to onCreate()" }

        setTheme(getSystemTheme(this))
        super.onCreate(savedInstanceState)
        mSDKApp = this.applicationContext as jSDKCoreApp
        mSDKApp!!.currentActivity = this
        setContentView(layout)
        if (actionbar) {
            setSupportActionBar(findViewById(R.id.actionbar))
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(!home)
            supportActionBar!!.setDisplayShowTitleEnabled(app_name)
            actionBar2(toolbar as ActionBar2?)
        } else {
            Log.e("ActionBar2", "no actionbar found")
        }
    }

    /**
     * ActionBar2 exclusive features
     * @param toolbar ActionBar2
     */
    private fun actionBar2(toolbar: ActionBar2?) {
        toolbar!!.fixLogo(icon)
        toolbar.fixParameters()
        toolbar.requestLayout()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        mSDKApp!!.currentActivity = this
        if (currentTheme != getThemeFromDB1(this)) {
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        val currActivity = mSDKApp!!.currentActivity
        if (this == currActivity) mSDKApp!!.currentActivity = null
    }
}