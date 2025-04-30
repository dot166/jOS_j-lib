/*
 * Copyright (C) 2018 The Android Open Source Project
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
import static com.android.wallpaper.widget.BottomActionBar.BottomAction.APPLY_TEXT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.customization.model.CustomizationManager.Callback;
import com.android.customization.model.CustomizationManager.OptionsFetchedListener;
import com.android.customization.model.CustomizationOption;

import io.github.dot166.jlib.LIBAboutActivity;
import io.github.dot166.themeengine.OptionSelectorController.CheckmarkStyle;

import com.android.wallpaper.picker.AppbarFragment;
import com.android.wallpaper.widget.BottomActionBar;

import java.util.List;

/**
 * Fragment that contains the UI for selecting and applying a ThemeEngineOption.
 */
public class ThemeEngineFragment extends AppbarFragment {

    private static final String TAG = "ThemeEngineFragment";
    private static final String KEY_STATE_SELECTED_OPTION = "ThemeEngineFragment.selectedOption";
    private static final String KEY_STATE_BOTTOM_ACTION_BAR_VISIBLE =
            "ThemeEngineFragment.bottomActionBarVisible";

    public static ThemeEngineFragment newInstance(CharSequence title) {
        ThemeEngineFragment fragment = new ThemeEngineFragment();
        fragment.setArguments(AppbarFragment.createArguments(title));
        return fragment;
    }

    private RecyclerView mOptionsContainer;
    private OptionSelectorController<ThemeEngineOption> mOptionsController;
    private ThemeEngineManager mThemeEngineManager;
    private ThemeEngineOption mSelectedOption;
    private ContentLoadingProgressBar mLoading;
    private ViewGroup mContent;
    private View mError;
    private BottomActionBar mBottomActionBar;

    private final Callback mApplyThemeEngineCallback = new Callback() {
        @Override
        public void onSuccess() {
            Log.i(TAG, "Theme changed, recreating activity.");
            Toast.makeText(getContext(), TAG + ": Theme changed, recreating activity.", LENGTH_LONG).show();
            requireActivity().recreate();
        }

        @Override
        public void onError(@Nullable Throwable throwable) {
            // Since we disabled it when clicked apply button.
            mBottomActionBar.enableActions();
            mBottomActionBar.hide();
            //TODO: handle
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_te_theme_picker, container, /* attachToRoot */ false);
        setUpToolbar(view);
        mContent = view.findViewById(R.id.content_section);
        mOptionsContainer = view.findViewById(R.id.options_container);
        mLoading = view.findViewById(R.id.loading_indicator);
        mError = view.findViewById(R.id.error_section);

        // For nav bar edge-to-edge effect.
        view.setOnApplyWindowInsetsListener((v, windowInsets) -> {
            v.setPadding(
                    v.getPaddingLeft(),
                    windowInsets.getSystemWindowInsetTop(),
                    v.getPaddingRight(),
                    windowInsets.getSystemWindowInsetBottom());
            return windowInsets.consumeSystemWindowInsets();
        });

        mThemeEngineManager = ThemeEngineManager.getInstance(getActivity());
        setUpOptions(savedInstanceState);

        view.findViewById(R.id.custom_toolbar_title).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getContext(), LIBAboutActivity.class));
                return true;
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBottomActionBar != null) {
            outState.putBoolean(KEY_STATE_BOTTOM_ACTION_BAR_VISIBLE, mBottomActionBar.isVisible());
        }
    }

    @Override
    protected void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        super.onBottomActionBarReady(bottomActionBar);
        mBottomActionBar = bottomActionBar;
        mBottomActionBar.showActionsOnly(APPLY_TEXT);
        mBottomActionBar.setActionClickListener(APPLY_TEXT, v -> applyThemeEngineOption(mSelectedOption));
    }

    private void applyThemeEngineOption(ThemeEngineOption ThemeEngineOption) {
        mBottomActionBar.disableActions();
        mThemeEngineManager.apply(ThemeEngineOption, mApplyThemeEngineCallback);
    }

    private void setUpOptions(@Nullable Bundle savedInstanceState) {
        hideError();
        mLoading.show();
        mThemeEngineManager.fetchOptions(new OptionsFetchedListener<ThemeEngineOption>() {
            @Override
            public void onOptionsLoaded(List<ThemeEngineOption> options) {
                mLoading.hide();
                mOptionsController = new OptionSelectorController<>(
                        mOptionsContainer, options, /* useGrid= */ false, CheckmarkStyle.CORNER);
                mOptionsController.initOptions(mThemeEngineManager);
                mSelectedOption = getActiveOption(options);
                mOptionsController.setSelectedOption(mSelectedOption);
                onOptionSelected(mSelectedOption);
                restoreBottomActionBarVisibility(savedInstanceState);

                mOptionsController.addListener(selectedOption -> {
                    onOptionSelected(selectedOption);
                    mBottomActionBar.show();
                });
            }

            @Override
            public void onError(@Nullable Throwable throwable) {
                if (throwable != null) {
                    Log.e(TAG, "Error loading ThemeEngine options", throwable);
                }
                showError();
            }
        }, /*reload= */ true);
    }

    private ThemeEngineOption getActiveOption(List<ThemeEngineOption> options) {
        return options.stream()
                .filter(option -> option.isActive(mThemeEngineManager))
                .findAny()
                // For development only, as there should always be an ThemeEngine set.
                .orElse(options.get(0));
    }

    private void hideError() {
        mContent.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    private void showError() {
        mLoading.hide();
        mContent.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }

    private void onOptionSelected(CustomizationOption selectedOption) {
        mSelectedOption = (ThemeEngineOption) selectedOption;
        refreshPreview();
    }

    private void refreshPreview() {
        mSelectedOption.bindPreview(mContent);
    }

    private void restoreBottomActionBarVisibility(@Nullable Bundle savedInstanceState) {
        boolean isBottomActionBarVisible = savedInstanceState != null
                && savedInstanceState.getBoolean(KEY_STATE_BOTTOM_ACTION_BAR_VISIBLE);
        if (mBottomActionBar == null) return;
        if (isBottomActionBarVisible) {
            mBottomActionBar.show();
        } else {
            mBottomActionBar.hide();
        }
    }
}