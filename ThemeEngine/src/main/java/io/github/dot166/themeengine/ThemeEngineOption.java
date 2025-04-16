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
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;

import com.android.wallpaper.util.ResourceUtils;

import com.android.customization.model.CustomizationManager;
import com.android.customization.model.CustomizationOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ThemeEngineOption implements CustomizationOption<ThemeEngineOption> {

    public static final int THUMBNAIL_ICON_POSITION = 0;
    private static int[] mIconIds = {
            R.id.preview_icon_0, R.id.preview_icon_1, R.id.preview_icon_2, R.id.preview_icon_3,
            R.id.preview_icon_4, R.id.preview_icon_5
    };

    private Drawable mIcon;
    private String mTitle;
    private String mThemeId;
    private Context mContext;

    public ThemeEngineOption(String title, String themeId, Context context) {
        mTitle = title;
        mThemeId = themeId;
        mContext = context;
    }

    @Override
    public void bindThumbnailTile(View view) {
        Resources res = view.getContext().getResources();
        Drawable icon = new BitmapDrawable(res, BitmapFactory.decodeResource(res, io.github.dot166.jlib.R.mipmap.ic_themeengine));
        int resId = R.id.icon_section_tile;
        if (view.findViewById(R.id.option_icon) != null) {
            resId = R.id.option_icon;
            if (mIcon != null) {
                icon = mIcon;
            } else {
                Log.i("ThemeEngineOption", mTitle + " - no icon is set");
            }
        }
        ((ImageView) view.findViewById(resId)).setImageDrawable(icon);
        view.setContentDescription(mTitle);
    }

    @Override
    public boolean isActive(CustomizationManager<ThemeEngineOption> manager) {
        return Objects.equals(mThemeId, (new ThemeProvider()).getTheme(mContext));
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
//        ViewGroup cardBody = container.findViewById(R.id.theme_preview_card_body_container);
//        if (cardBody.getChildCount() == 0) {
//            LayoutInflater.from(container.getContext()).inflate(
//                    R.layout.preview_card_te_content, cardBody, true);
//        }
//        for (int i = 0; i < mIconIds.length && i < mIcons.size(); i++) {
//            ((ImageView) container.findViewById(mIconIds[i])).setImageDrawable(
//                    mIcons.get(i));
//        }
    }

    public void setIcon(Drawable previewIcon) {
        mIcon = previewIcon;
    }
}