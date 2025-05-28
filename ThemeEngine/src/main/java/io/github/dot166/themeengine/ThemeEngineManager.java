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

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.android.customization.model.CustomizationManager;

public class ThemeEngineManager implements CustomizationManager<ThemeEngineOption> {

    private static ThemeEngineManager sThemeEngineOptionManager;
    private FragmentActivity mActivtiy;
    private ThemeEngineOptionProvider mProvider;
    private static final String TAG = "ThemeEngineManager";
    private static final String KEY_STATE_CURRENT_SELECTION = "ThemeEngineManager.currentSelection";

    ThemeEngineManager(FragmentActivity activity, ThemeEngineOptionProvider provider) {
        mActivtiy = activity;
        mProvider = provider;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void apply(ThemeEngineOption option, @Nullable Callback callback) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivtiy);
        String themeId = option.getThemeId();
        prefs.edit().putString("pref_theme", themeId).apply();
        Log.i(TAG, "Theme changed, recreating activity.");
        Toast.makeText(mActivtiy, TAG + ": Theme changed, recreating activity.", LENGTH_LONG).show();
        mActivtiy.recreate();
    }

    @Override
    public void fetchOptions(OptionsFetchedListener<ThemeEngineOption> callback, boolean reload) {
        callback.onOptionsLoaded(mProvider.getOptions());
    }

    public static ThemeEngineManager getInstance(FragmentActivity activity) {
        if (sThemeEngineOptionManager == null) {
            Context applicationContext = activity.getApplicationContext();
            sThemeEngineOptionManager = new ThemeEngineManager(activity, new ThemeEngineOptionProvider(applicationContext));
        }
        return sThemeEngineOptionManager;
    }

}