package io.github.dot166.jlib.view

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources.Theme
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View

open class ContextThemeBubble(base: Context, val initialThemeResId: Int) : ContextWrapper(base) {
    var themeResId: Int = initialThemeResId
        private set
    private var theme: Theme? = null
    private var inflater: LayoutInflater? = null

    fun clearTheme() {
        if (theme != null) {
            theme = null
        } else {
            Log.i(javaClass.simpleName, "Theme has already been cleared")
        }
    }

    fun resetTheme() {
        clearTheme()
        themeResId = initialThemeResId
    }

    override fun setTheme(resid: Int) {
        if (themeResId != resid) {
            themeResId = resid
            initializeTheme()
        }
    }

    override fun getTheme(): Theme? {
        if (theme != null) {
            return theme
        }
        initializeTheme()

        return theme
    }

    override fun getSystemService(name: String): Any? {
        if (LAYOUT_INFLATER_SERVICE == name) {
            if (inflater == null) {
                inflater = LayoutInflater.from(baseContext).cloneInContext(this)
                inflater!!.setFactory2(ViewFactory())
            }
            return inflater
        }
        return baseContext.getSystemService(name)
    }

    private fun initializeTheme() {
        val first = theme == null
        if (first) {
            theme = resources.newTheme()
        }
        theme!!.applyStyle(themeResId, true)
    }

    private class ViewFactory : Factory2 {
        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            return null // force default inflater
        }

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            return onCreateView(null, name, context, attrs)
        }
    }
}