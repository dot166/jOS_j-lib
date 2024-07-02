/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RestrictTo
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import com.google.android.material.R

/**
 * Utility methods to check Theme compatibility with components.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object ThemeEnforcement {
    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(R.attr.colorPrimary)
    private const val APPCOMPAT_THEME_NAME = "Theme.AppCompat"

    private val MATERIAL_CHECK_ATTRS = intArrayOf(R.attr.colorPrimaryVariant)
    private const val MATERIAL_THEME_NAME = "Theme.MaterialComponents"

    /**
     * Safely retrieve styled attribute information in this Context's theme, after checking whether
     * the theme is compatible with the component's given style.
     *
     *
     * Set a component's `enforceMaterialTheme` attribute to `true` to ensure that
     * the Context's theme inherits from `Theme.MaterialComponents`. For example, you'll want to
     * do this if the component uses a new attribute defined in `
     * Theme.MaterialComponents` like `colorSecondary`.
     *
     *
     * If the `enforceTextAppearance` attribute is set to `true` and
     * textAppearanceResIndices parameter is specified and has non-negative values, this will also
     * check that a valid TextAppearance is set on this component for the text appearance resources
     * passed in.
     */
    @JvmStatic
    fun obtainStyledAttributes(
        context: Context,
        set: AttributeSet,
        @StyleableRes attrs: IntArray,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int,
        @StyleableRes vararg textAppearanceResIndices: Int
    ): TypedArray {
        // First, check for a compatible theme.

        checkCompatibleTheme(context, set, defStyleAttr, defStyleRes)

        // Then, check that a textAppearance is set if enforceTextAppearance attribute is true
        checkTextAppearance(
            context,
            set,
            attrs,
            defStyleAttr,
            defStyleRes,
            *textAppearanceResIndices
        )

        // Then, safely retrieve the styled attribute information.
        return context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes)
    }

    private fun checkCompatibleTheme(
        context: Context,
        set: AttributeSet,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) {
        val a =
            context.obtainStyledAttributes(
                set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes
            )
        val enforceMaterialTheme =
            a.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false)
        a.recycle()

        if (enforceMaterialTheme) {
            val isMaterialTheme = TypedValue()
            val resolvedValue =
                context.theme.resolveAttribute(R.attr.isMaterialTheme, isMaterialTheme, true)

            if (!resolvedValue
                || (isMaterialTheme.type == TypedValue.TYPE_INT_BOOLEAN && isMaterialTheme.data == 0)
            ) {
                // If we were unable to resolve isMaterialTheme boolean attribute, or isMaterialTheme is
                // false, check for Material Theme color attributes
                checkMaterialTheme(context)
            }
        }
        checkAppCompatTheme(context)
    }

    private fun checkTextAppearance(
        context: Context,
        set: AttributeSet,
        @StyleableRes attrs: IntArray,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int,
        @StyleableRes vararg textAppearanceResIndices: Int
    ) {
        val themeEnforcementAttrs =
            context.obtainStyledAttributes(
                set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes
            )
        val enforceTextAppearance =
            themeEnforcementAttrs.getBoolean(
                R.styleable.ThemeEnforcement_enforceTextAppearance,
                false
            )

        if (!enforceTextAppearance) {
            themeEnforcementAttrs.recycle()
            return
        }

        val validTextAppearance =
            if (textAppearanceResIndices == null || textAppearanceResIndices.size == 0) {
                // No custom TextAppearance attributes passed in, check android:textAppearance
                (
                        themeEnforcementAttrs.getResourceId(
                            R.styleable.ThemeEnforcement_android_textAppearance, -1
                        )
                                != -1)
            } else {
                // Check custom TextAppearances are valid
                isCustomTextAppearanceValid(
                    context, set, attrs, defStyleAttr, defStyleRes, *textAppearanceResIndices
                )
            }

        themeEnforcementAttrs.recycle()

        require(validTextAppearance) {
            ("This component requires that you specify a valid TextAppearance attribute. Update your "
                    + "app theme to inherit from Theme.MaterialComponents (or a descendant).")
        }
    }

    private fun isCustomTextAppearanceValid(
        context: Context,
        set: AttributeSet,
        @StyleableRes attrs: IntArray,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int,
        @StyleableRes vararg textAppearanceResIndices: Int
    ): Boolean {
        val componentAttrs =
            context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes)
        for (customTextAppearanceIndex in textAppearanceResIndices) {
            if (componentAttrs.getResourceId(customTextAppearanceIndex, -1) == -1) {
                componentAttrs.recycle()
                return false
            }
        }
        componentAttrs.recycle()
        return true
    }

    fun checkAppCompatTheme(context: Context) {
        checkTheme(context, APPCOMPAT_CHECK_ATTRS, APPCOMPAT_THEME_NAME)
    }

    fun checkMaterialTheme(context: Context) {
        checkTheme(context, MATERIAL_CHECK_ATTRS, MATERIAL_THEME_NAME)
    }

    private fun isTheme(context: Context, themeAttributes: IntArray): Boolean {
        val a = context.obtainStyledAttributes(themeAttributes)
        for (i in themeAttributes.indices) {
            if (!a.hasValue(i)) {
                a.recycle()
                return false
            }
        }
        a.recycle()
        return true
    }

    private fun checkTheme(
        context: Context, themeAttributes: IntArray, themeName: String
    ) {
        require(isTheme(context, themeAttributes)) {
            ("The style on this component requires your app theme to be "
                    + themeName
                    + " (or a descendant).")
        }
    }
}