/*
 * Copyright (C) 2024 ._______166
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jOS.Core

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import jOS.Core.utils.ThemeEnforcement.obtainStyledAttributes
import jOS.Core.utils.ToolbarUtils.getLogoImageView
import jOS.Core.utils.ToolbarUtils.getNavImageView
import jOS.Core.utils.ToolbarUtils.getSubtitleTextView
import jOS.Core.utils.ToolbarUtils.getTitleTextView

/**
 * `ActionBar2` is a [MaterialToolbar] that implements certain features and fixes.
 *
 *
 *
 * To get started with the `ActionBar2` component, use `jOS.Core.ActionBar2` in your layout XML instead of `androidx.appcompat.widget.Toolbar` or `Toolbar` or `com.google.android.material.appbar.MaterialToolbar` or `MaterialToolbar`. E.g.,:
 *
 * <pre>
 * &lt;jOS.Core.ActionBar2
 * android:layout_width=&quot;match_parent&quot;
 * android:layout_height=&quot;wrap_content&quot;/&gt;
</pre> *
 */
class ActionBar2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : MaterialToolbar(
    MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, DEF_STYLE_RES),
    attrs,
    defStyleAttr
) {
    init {
        var context = context
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext()

        val a =
            obtainStyledAttributes(
                context, attrs!!, R.styleable.jToolbar, defStyleAttr, DEF_STYLE_RES
            )

        //titleCentered = a.getBoolean(R.styleable.jToolbar_jtitleCentered, false)
        //subtitleCentered = a.getBoolean(R.styleable.jToolbar_jsubtitleCentered, false)
        a.recycle()

        Log.i(TAG, "init complete!!")
    }

    fun fixLogo(icon: Boolean) {
        if (icon) {
            logo = activityIcon
        } else {
            Log.i(TAG, "icon disabled in config")
        }
    }

    private val activityIcon: Drawable
        get() {
            val pm = context.packageManager

            val intent =
                (context.applicationContext as jSDKCoreApp).currentActivity!!.intent
            val resolveInfo = pm.resolveActivity(intent, 0)

            return resolveInfo!!.loadIcon(pm)
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val titleTextView = getTitleTextView(this)
        titleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17.50f)
        val subtitleTextView = getSubtitleTextView(this)
        subtitleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
    }

    fun fixParameters() {
        val nav = getNavImageView(this)
        val logoImageView = getLogoImageView(this)
        val titleTextView = getTitleTextView(this)
        val subtitleTextView = getSubtitleTextView(this)
        val r = context.resources
        val pxl = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            0f,
            r.displayMetrics
        ).toInt()
        val pxr = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            r.displayMetrics
        ).toInt()
        if (logoImageView != null) {
            val params = logoImageView.layoutParams as LayoutParams
            params.setMargins(
                pxl,
                r.getDimension(R.dimen.j_action_bar_icon_vertical_padding).toInt(),
                pxr,
                r.getDimension(R.dimen.j_action_bar_icon_vertical_padding).toInt()
            )
            logoImageView.layoutParams = params
        } else {
            Log.e(TAG, "logoImageView is disabled!!!")
        }
        if (titleTextView != null) {
            val params = titleTextView.layoutParams as LayoutParams
            params.setMargins(pxl, pxl, pxl, pxl)
            titleTextView.layoutParams = params
        } else {
            Log.e(TAG, "titleTextView is disabled!!!")
        }
        if (subtitleTextView != null) {
            val params = subtitleTextView.layoutParams as LayoutParams
            params.setMargins(pxl, pxl, pxl, pxl)
            subtitleTextView.layoutParams = params
        } else {
            Log.e(TAG, "subtitleTextView is disabled!!!")
        }
        if (nav != null) {
            val params = nav.layoutParams as LayoutParams
            params.setMargins(pxl, pxl, pxl, pxl)
            nav.layoutParams = params
        } else {
            Log.e(TAG, "nav is disabled!!!")
        }
    }

    companion object {
        private val DEF_STYLE_RES = R.style.j_ActionBar
        private const val TAG = "ActionBar2"
    }
}