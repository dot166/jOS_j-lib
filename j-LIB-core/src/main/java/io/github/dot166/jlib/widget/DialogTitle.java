/*
 * imported from androidx appcompat v1.7.0 on 13.04.2025 by ._______166
 *
 * Copyright (C) 2015 Google Inc.
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

package io.github.dot166.jlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Objects;

import io.github.dot166.jlib.themeengine.ThemeEngine;

/**
 * Used by dialogs to change the font size and number of lines to try to fit
 * the text to the available space.
 *
 * 13.04.2025 ._______166 - added {@link io.github.dot166.jlib.themeengine.ThemeEngine} dynamic theming, is here to prevent j.Theme attributes from appearing when M3 or another theme is selected
 *
 */
public class DialogTitle extends AppCompatTextView {

    public DialogTitle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (Objects.equals(ThemeEngine.getTmpCurrentTheme(), "jLib")) {
            // sets text colour to the colour secondary attribute, if that fails it would set to
            setTextColor(getContext().getTheme().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSecondary}).getColor(0, getResources().getColor(android.R.color.holo_blue_dark, getContext().getTheme())));
        }
    }

    public DialogTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DialogTitle(@NonNull Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    setSingleLine(false);
                    setMaxLines(2);

                    final TypedArray a = getContext().obtainStyledAttributes(null,
                            R.styleable.TextAppearance,
                            android.R.attr.textAppearanceMedium,
                            android.R.style.TextAppearance_Medium);
                    final int textSize = a.getDimensionPixelSize(
                            R.styleable.TextAppearance_android_textSize, 0);
                    if (textSize != 0) {
                        // textSize is already expressed in pixels
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    a.recycle();

                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }
}