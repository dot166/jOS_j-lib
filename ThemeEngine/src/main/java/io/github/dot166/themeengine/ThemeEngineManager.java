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

import androidx.annotation.Nullable;

import com.android.customization.model.CustomizationManager;

import java.util.List;

public class ThemeEngineManager implements CustomizationManager<ThemeEngineOption> {

    private static ThemeEngineManager sThemeEngineOptionManager;
    private Context mContext;
    private ThemeEngineOptionProvider mProvider;
    private static final String TAG = "ThemeEngineManager";
    private static final String KEY_STATE_CURRENT_SELECTION = "ThemeEngineManager.currentSelection";

    ThemeEngineManager(Context context, ThemeEngineOptionProvider provider) {
        mContext = context;
        mProvider = provider;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void apply(ThemeEngineOption option, @Nullable Callback callback) {
        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void fetchOptions(OptionsFetchedListener<ThemeEngineOption> callback, boolean reload) {
        callback.onOptionsLoaded(mProvider.getOptions());
    }

    public static ThemeEngineManager getInstance(Context context) {
        if (sThemeEngineOptionManager == null) {
            Context applicationContext = context.getApplicationContext();
            sThemeEngineOptionManager = new ThemeEngineManager(context, new ThemeEngineOptionProvider(applicationContext));
        }
        return sThemeEngineOptionManager;
    }

}