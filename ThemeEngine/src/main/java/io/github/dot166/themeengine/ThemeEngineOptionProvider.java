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
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class ThemeEngineOptionProvider {

    private Drawable[] icons;

    private static final String TAG = "ThemeEngineOptionProvider";

    private Context mContext;
    private final List<ThemeEngineOption> mOptions = new ArrayList<>();

    public ThemeEngineOptionProvider(Context context) {
        mContext = context;
        String[] themeIds = mContext.getResources().getStringArray(R.array.themesConfig);
        icons = new Drawable[themeIds.length];
        for (int i = 0; i < themeIds.length; i++) {
            switch (themeIds[i]) {
                case "Disabled" ->
                        icons[i] = mContext.getResources().getDrawable(android.R.mipmap.sym_def_app_icon);
                case "jLib" ->
                        icons[i] = mContext.getResources().getDrawable(io.github.dot166.jlib.R.mipmap.ic_launcher_j);
                case "M3" ->
                        icons[i] = mContext.getResources().getDrawable(android.R.mipmap.sym_def_app_icon); // TODO: find an icon for M3
            }
        }
    }

    public List<ThemeEngineOption> getOptions() {
        if (mOptions.isEmpty()) loadOptions();
        return mOptions;
    }

    private void loadOptions() {
        String[] themeIds = mContext.getResources().getStringArray(R.array.themesConfig);
        String[] themeNames = mContext.getResources().getStringArray(R.array.themes);

        for (int i = 0; i < themeIds.length; i++) {
            mOptions.add(new ThemeEngineOption(themeNames[i], themeIds[i], mContext, icons[i]));
        }
    }

}