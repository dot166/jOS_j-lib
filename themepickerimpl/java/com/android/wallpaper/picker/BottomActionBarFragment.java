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
package com.android.wallpaper.picker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.BottomActionBar.BottomActionBarHost;

import io.github.dot166.themeengine.R;

/**
 * Base class for Fragments that own a {@link BottomActionBar} widget.
 *
 * <p>A Fragment extending this class is expected to have a {@link BottomActionBar} in its activity
 * which is a {@link BottomActionBarHost}, which can handle lifecycle management of
 * {@link BottomActionBar} for extending fragment.
 *
 * <p>Or helping some fragments that own a {@link BottomActionBar} in fragment view to setup the
 * common behavior for {@link BottomActionBar}.
 */
public class BottomActionBarFragment extends Fragment {

    @Nullable private BottomActionBar mBottomActionBar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBottomActionBar = findBottomActionBar();
        if (mBottomActionBar != null) {
            mBottomActionBar.reset();
            onBottomActionBarReady(mBottomActionBar);
        }
    }

    /** Returns {@code true} if the fragment would handle the event. */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Gets called when {@link #onViewCreated} finished. For extending fragment, this is the only
     * one interface to get {@link BottomActionBar}.
     */
    @CallSuper
    protected void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        // Needed for some cases that need to recreate the BottomActionBar.
        mBottomActionBar = bottomActionBar;
        bottomActionBar.bindBackButtonToSystemBackKey(getActivity());
    }

    @Nullable
    private BottomActionBar findBottomActionBar() {
        Activity activity = getActivity();
        if (activity instanceof BottomActionBarHost) {
            return ((BottomActionBarHost) activity).getBottomActionBar();
        }

        View root = getView();
        if (root != null) {
            return root.findViewById(R.id.bottom_actionbar);
        }
        return null;
    }
}
