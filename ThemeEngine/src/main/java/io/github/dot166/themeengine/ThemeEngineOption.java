/*
 * Copyright (C) 2019 The Android Open Source Project
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
package io.github.dot166.themeengine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.android.wallpaper.util.ResourceUtils;

import com.android.customization.model.CustomizationManager;
import com.android.customization.model.CustomizationOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.dot166.jlib.widget.ActionBar2;

public class ThemeEngineOption implements CustomizationOption<ThemeEngineOption> {

    private Drawable mIcon;
    private String mTitle;
    private String mThemeId;
    private Context mContext;

    public ThemeEngineOption(String title, String themeId, Context context, Drawable icon) {
        mTitle = title;
        mThemeId = themeId;
        mContext = context;
        mIcon = icon;
    }

    @Override
    public void bindThumbnailTile(View view) {
        int resId = R.id.icon_section_tile;
        if (view.findViewById(R.id.option_icon) != null) {
            resId = R.id.option_icon;
        }
        ((ImageView) view.findViewById(resId)).setImageDrawable(mIcon);
        view.setContentDescription(mTitle);
    }

    @Override
    public boolean isActive(CustomizationManager<ThemeEngineOption> manager) {
        return Objects.equals(mThemeId, ThemeProvider.getTheme(mContext));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.theme_te_option;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public String getThemeId() {
        return mThemeId;
    }

    public void bindPreview(ViewGroup container) {
        ViewGroup cardBody = container.findViewById(R.id.theme_preview_card_body_container);
        cardBody.removeAllViews();
        int layout = R.layout.te_preview_jlib;
        switch (getThemeId()) {
            case "Disabled" -> {
                layout = R.layout.te_preview_off;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) cardBody.findViewById(R.id.off_text)).setTextColor(cardBody.getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary}).getColor(0, cardBody.getContext().getResources().getColor(io.github.dot166.jlib.R.color.j_primary_text_holo_dark)));
                    }
                }, 1000);
            }
            case "jLib" -> layout = R.layout.te_preview_jlib;
            case "M3" -> layout = R.layout.te_preview_m3;
        }
        LayoutInflater.from(mContext).inflate(layout, cardBody, true);
    }
}