package io.github.dot166.jLib.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dot166.jLib.R
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.currentTheme
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.getSystemTheme
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.getThemeFromDB
import io.github.dot166.jLib.utils.VersionUtils

open class jActivity : AppCompatActivity() {
    var actionbar: Boolean = false
    var layout: View? = null
    var layoutId: Int = 0
    var configured: Boolean = false
    protected var mLIBApp: jLIBCoreApp? = null

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layoutView [View], app layout. (if you are using java and you have set the layoutID parameter to a valid layout file, set this to null)
     * @param layoutId int, app layout. commonly R.layout.activitymain (if you are using java and you have set the layoutView parameter to a valid [View], set this to 0)
     * @param actionbar boolean, tells system if you would like to show the ActionBar
     */
    protected fun configure(layoutView: View? = null, layoutId: Int = 0, actionbar: Boolean = false) {
        this.layout = layoutView
        this.layoutId = layoutId
        this.actionbar = actionbar
        this.configured = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        check(configured) { "configure() not called prior to onCreate()" }

        setTheme(getSystemTheme(this))
        super.onCreate(savedInstanceState)
        mLIBApp = this.applicationContext as jLIBCoreApp
        mLIBApp!!.setCurrentActivity(this)
        if (layout == null && layoutId != 0) {
            layout = LayoutInflater.from(this).inflate(layoutId, null)
        }
        if (layout != null) {
            setContentView(layout)
        }

        if (VersionUtils.isAtLeastV()) {
            // Fix A15 EdgeToEdge
            ViewCompat.setOnApplyWindowInsetsListener(
                findViewById<View?>(android.R.id.content),
                OnApplyWindowInsetsListener { v: View?, windowInsets: WindowInsetsCompat? ->
                    val insets = windowInsets!!.getInsets(
                        (WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
                                or WindowInsetsCompat.Type.displayCutout())
                    )
                    val statusBarHeight = window.decorView.getRootWindowInsets()
                        .getInsets(WindowInsetsCompat.Type.statusBars()).top
                    // Apply the insets paddings to the view.
                    v!!.setPadding(insets.left, statusBarHeight, insets.right, insets.bottom)
                    WindowInsetsCompat.CONSUMED
                })
        }

        if (actionbar) {
            setSupportActionBar(findViewById<Toolbar?>(R.id.actionbar))
        }
    }

    override fun onResume() {
        super.onResume()
        mLIBApp!!.setCurrentActivity(this)
        if (currentTheme != getThemeFromDB(this)) {
            val intent = getIntent()
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
        val currActivity = mLIBApp!!.currentActivity
        if (this == currActivity) mLIBApp!!.setCurrentActivity(null)
    }
}
