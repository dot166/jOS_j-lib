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

package jOS.Core.utils;

import static java.util.Collections.max;
import static java.util.Collections.min;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Utility methods for {@link Toolbar}s.
 *
 * @hide
 */
@RestrictTo(Scope.LIBRARY)
public class ToolbarUtils {

    private static final Comparator<View> VIEW_TOP_COMPARATOR =
            new Comparator<View>() {
                @Override
                public int compare(View view1, View view2) {
                    return view1.getTop() - view2.getTop();
                }
            };

    private ToolbarUtils() {
        // Private constructor to prevent unwanted construction.
    }

    @Nullable
    public static TextView getTitleTextView(@NonNull Toolbar toolbar) {
        List<TextView> textViews = getTextViewsWithText(toolbar, toolbar.getTitle());
        return textViews.isEmpty() ? null : min(textViews, VIEW_TOP_COMPARATOR);
    }

    @Nullable
    public static TextView getSubtitleTextView(@NonNull Toolbar toolbar) {
        List<TextView> textViews = getTextViewsWithText(toolbar, toolbar.getSubtitle());
        return textViews.isEmpty() ? null : max(textViews, VIEW_TOP_COMPARATOR);
    }

    private static List<TextView> getTextViewsWithText(@NonNull Toolbar toolbar, CharSequence text) {
        List<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (TextUtils.equals(textView.getText(), text)) {
                    textViews.add(textView);
                }
            }
        }
        return textViews;
    }

    @Nullable
    public static ImageView getLogoImageView(@NonNull Toolbar toolbar) {
        return getImageView(toolbar, toolbar.getLogo());
    }

    @Nullable
    public static ImageButton getNavImageView(@NonNull Toolbar toolbar) {
        return getImageButton(toolbar, toolbar.getNavigationIcon());
    }

    @Nullable
    private static ImageView getImageView(@NonNull Toolbar toolbar, @Nullable Drawable content) {
        if (content == null) {
            return null;
        }
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                Drawable drawable = imageView.getDrawable();
                if (drawable != null
                        && drawable.getConstantState() != null
                        && drawable.getConstantState().equals(content.getConstantState())) {
                    return imageView;
                }
            }
        }
        return null;
    }

    @Nullable
    private static ImageButton getImageButton(@NonNull Toolbar toolbar, @Nullable Drawable content) {
        if (content == null) {
            return null;
        }
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof ImageButton) {
                ImageButton ImageButton = (ImageButton) child;
                Drawable drawable = ImageButton.getDrawable();
                if (drawable != null
                        && drawable.getConstantState() != null
                        && drawable.getConstantState().equals(content.getConstantState())) {
                    return ImageButton;
                }
            }
        }
        return null;
    }

}