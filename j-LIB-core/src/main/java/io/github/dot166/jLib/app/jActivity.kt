package io.github.dot166.jLib.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.currentTheme
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.getSystemTheme
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.getThemeFromDB
import io.github.dot166.jLib.utils.VersionUtils

open class jActivity : AppCompatActivity() {
    protected var mLIBApp: jLIBCoreApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getSystemTheme(this))
        super.onCreate(savedInstanceState)
        mLIBApp = this.applicationContext as jLIBCoreApp
        mLIBApp!!.setCurrentActivity(this)

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
