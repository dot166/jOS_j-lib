/*
 * Copyright (C) 2021 The Android Open Source Project
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.customization.model.CustomizationManager.OptionsFetchedListener;

import com.android.wallpaper.model.CustomizationSectionController;

import java.util.List;

/** A {@link CustomizationSectionController} for system Themes. */

public abstract class ThemeEngineSectionController implements CustomizationSectionController<ThemeEngineSectionView> {

    private static final String TAG = "ThemeEngineSectionController";

    private final ThemeEngineManager mThemeEngineOptionsManager;
    private final CustomizationSectionNavigationController mSectionNavigationController;

    public ThemeEngineSectionController(ThemeEngineManager ThemeEngineOptionsManager,
                                        CustomizationSectionNavigationController sectionNavigationController) {
        mThemeEngineOptionsManager = ThemeEngineOptionsManager;
        mSectionNavigationController = sectionNavigationController;
    }

    @Override
    public boolean isAvailable(Context context) {
        return mThemeEngineOptionsManager.isAvailable();
    }

    @Override
    public ThemeEngineSectionView createView(Context context) {
        ThemeEngineSectionView ThemeEngineSectionView = (ThemeEngineSectionView) LayoutInflater.from(context)
                .inflate(R.layout.te_section_view, /* root= */ null);

        TextView sectionDescription = ThemeEngineSectionView.findViewById(R.id.icon_section_description);
        View sectionTile = ThemeEngineSectionView.findViewById(R.id.icon_section_tile);

        mThemeEngineOptionsManager.fetchOptions(new OptionsFetchedListener<ThemeEngineOption>() {
            @Override
            public void onOptionsLoaded(List<ThemeEngineOption> options) {
                ThemeEngineOption activeOption = getActiveOption(options);
                sectionDescription.setText(activeOption.getTitle());
                activeOption.bindThumbnailTile(sectionTile);
            }

            @Override
            public void onError(@Nullable Throwable throwable) {
                if (throwable != null) {
                    Log.e(TAG, "Error loading theme options", throwable);
                }
                sectionDescription.setText(R.string.something_went_wrong);
                sectionTile.setVisibility(View.GONE);
            }
        }, /* reload= */ true);

        ThemeEngineSectionView.setOnClickListener(v -> mSectionNavigationController.navigateTo(
                ThemeEngineFragment.newInstance(context.getString(R.string.app_name))));

        return ThemeEngineSectionView;
    }

    private ThemeEngineOption getActiveOption(List<ThemeEngineOption> options) {
        return options.stream()
                .filter(option -> option.isActive(mThemeEngineOptionsManager))
                .findAny()
                // For development only, as there should always be a grid set.
                .orElse(options.get(0));
    }

    @Override
    public boolean shouldRetainInstanceWhenSwitchingTabs() {
        return false;
    }

    @NonNull
    @Override
    public ThemeEngineSectionView createView(@NonNull Context context, @NonNull ViewCreationParams params) {
        return createView(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void release() {
    }

    @Override
    public void onTransitionOut() {
    }
}