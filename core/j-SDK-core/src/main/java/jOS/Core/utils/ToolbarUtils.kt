/*
 * Copyright (C) 2020 The Android Open Source Project
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
package jOS.Core.utils

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RestrictTo
import androidx.appcompat.widget.Toolbar
import java.util.Collections

/**
 * Utility methods for [Toolbar]s.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object ToolbarUtils {
    private val VIEW_TOP_COMPARATOR =
        java.util.Comparator<View> { view1, view2 -> view1.top - view2.top }

    @JvmStatic
    fun getTitleTextView(toolbar: Toolbar): TextView? {
        val textViews = getTextViewsWithText(toolbar, toolbar.title)
        return if (textViews.isEmpty()) null else Collections.min(textViews, VIEW_TOP_COMPARATOR)
    }

    @JvmStatic
    fun getSubtitleTextView(toolbar: Toolbar): TextView? {
        val textViews = getTextViewsWithText(toolbar, toolbar.subtitle)
        return if (textViews.isEmpty()) null else Collections.max(textViews, VIEW_TOP_COMPARATOR)
    }

    private fun getTextViewsWithText(toolbar: Toolbar, text: CharSequence): List<TextView> {
        val textViews: MutableList<TextView> = ArrayList()
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is TextView) {
                val textView = child
                if (TextUtils.equals(textView.text, text)) {
                    textViews.add(textView)
                }
            }
        }
        return textViews
    }

    @JvmStatic
    fun getLogoImageView(toolbar: Toolbar): ImageView? {
        return getImageView(toolbar, toolbar.logo)
    }

    @JvmStatic
    fun getNavImageView(toolbar: Toolbar): ImageButton? {
        return getImageButton(toolbar, toolbar.navigationIcon)
    }

    private fun getImageView(toolbar: Toolbar, content: Drawable?): ImageView? {
        if (content == null) {
            return null
        }
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is ImageView) {
                val imageView = child
                val drawable = imageView.drawable
                if (drawable != null && drawable.constantState != null && drawable.constantState == content.constantState) {
                    return imageView
                }
            }
        }
        return null
    }

    private fun getImageButton(toolbar: Toolbar, content: Drawable?): ImageButton? {
        if (content == null) {
            return null
        }
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is ImageButton) {
                val ImageButton = child
                val drawable = ImageButton.drawable
                if (drawable != null && drawable.constantState != null && drawable.constantState == content.constantState) {
                    return ImageButton
                }
            }
        }
        return null
    }
}