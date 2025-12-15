package io.github.dot166.jlib.app

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources.Theme
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatViewInflater
import com.android.settingslib.widget.SettingsThemeHelper.isExpressiveTheme
import com.android.settingslib.widget.theme.R

/**
 * An extension of [AlertDialog.Builder] that forces a SettingsLib theme on the dialog (e.g.,
 * Theme.SubSettingsBase).
 *
 *
 * The type of dialog returned is still an [AlertDialog]; there is no specific SettingsLib
 * or jLib implementation of [AlertDialog].
 */
class SettingsLibAlertDialogBuilder(context: Context) : AlertDialog.Builder(
    ContextThemeBubble(context)
) {
    private class ContextThemeBubble(base: Context) : ContextWrapper(base) {
        var themeResId: Int = 0
            private set
        private var mTheme: Theme? = null
        private var mInflater: LayoutInflater? = null

        init {
            themeResId = if (isExpressiveTheme(base)) {
                R.style.Theme_SubSettingsBase_Expressive
            } else {
                R.style.Theme_SubSettingsBase
            }
        }

        override fun setTheme(resid: Int) {
            if (themeResId != resid) {
                themeResId = resid
                initializeTheme()
            }
        }

        override fun getTheme(): Theme? {
            if (mTheme != null) {
                return mTheme
            }

            if (themeResId == 0) {
                themeResId = if (isExpressiveTheme(this)) {
                    R.style.Theme_SubSettingsBase_Expressive
                } else {
                    R.style.Theme_SubSettingsBase
                }
            }
            initializeTheme()

            return mTheme
        }

        override fun getSystemService(name: String): Any? {
            if (LAYOUT_INFLATER_SERVICE == name) {
                if (mInflater == null) {
                    mInflater = LayoutInflater.from(baseContext).cloneInContext(this)
                    mInflater!!.setFactory2(ViewFactory())
                }
                return mInflater
            }
            return baseContext.getSystemService(name)
        }

        private fun initializeTheme() {
            val first = mTheme == null
            if (first) {
                mTheme = resources.newTheme()
            }
            mTheme!!.applyStyle(themeResId, true)
        }

        private class ViewFactory : Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                return AppCompatViewInflater().createView(
                    parent, name, context, attrs, false,
                    false,
                    true,
                    false
                )
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return onCreateView(null, name, context, attrs)
            }
        }
    }
}
