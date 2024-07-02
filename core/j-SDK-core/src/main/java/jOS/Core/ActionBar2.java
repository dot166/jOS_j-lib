/*
 * Copyright (C) 2024 ._______166
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

package jOS.Core;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;

import jOS.Core.utils.ThemeEnforcement;
import jOS.Core.utils.ToolbarUtils;

/**
 * {@code ActionBar2} is a {@link MaterialToolbar} that implements certain features and fixes.
 *
 *
 * <p>To get started with the {@code ActionBar2} component, use {@code
 * jOS.Core.ActionBar2} in your layout XML instead of {@code
 * androidx.appcompat.widget.Toolbar} or {@code Toolbar} or {@code
 * com.google.android.material.appbar.MaterialToolbar} or {@code MaterialToolbar}. E.g.,:
 *
 * <pre>
 * &lt;jOS.Core.ActionBar2
 *         android:layout_width=&quot;match_parent&quot;
 *         android:layout_height=&quot;wrap_content&quot;/&gt;
 * </pre>
 */
public class ActionBar2 extends MaterialToolbar {

    private static final int DEF_STYLE_RES = R.style.j_ActionBar;
    private static final String TAG = "ActionBar2";

    public ActionBar2(@NonNull Context context) {
        this(context, null);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext();

        final TypedArray a =
                ThemeEnforcement.obtainStyledAttributes(
                        context, attrs, R.styleable.jToolbar, defStyleAttr, DEF_STYLE_RES);

        //titleCentered = a.getBoolean(R.styleable.jToolbar_jtitleCentered, false);
        //subtitleCentered = a.getBoolean(R.styleable.jToolbar_jsubtitleCentered, false);

        a.recycle();

        Log.i(TAG, "init complete!!");
    }

    public void fixLogo(boolean icon) {
        if (icon) {
            setLogo(getActivityIcon());
        } else {
            Log.i(TAG, "icon disabled in config");
        }
    }

    private Drawable getActivityIcon() {
        PackageManager pm = getContext().getPackageManager();

        Intent intent = ((jSDKCoreApp)getContext().getApplicationContext()).getCurrentActivity().getIntent();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

        return resolveInfo.loadIcon(pm);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        TextView titleTextView = ToolbarUtils.getTitleTextView(this);
        if (titleTextView != null) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17.50F);
        }
        TextView subtitleTextView = ToolbarUtils.getSubtitleTextView(this);
        if (subtitleTextView != null) {
            subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        }

    }

    public void fixParameters(){
        ImageButton nav = ToolbarUtils.getNavImageView(this);
        ImageView logoImageView = ToolbarUtils.getLogoImageView(this);
        TextView titleTextView = ToolbarUtils.getTitleTextView(this);
        TextView subtitleTextView = ToolbarUtils.getSubtitleTextView(this);
        Resources r = getContext().getResources();
        int pxl = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                0,
                r.getDisplayMetrics()
        );
        int pxr = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                r.getDisplayMetrics()
        );
        if (logoImageView != null) {
            LayoutParams params = (LayoutParams) logoImageView.getLayoutParams();
            params.setMargins(pxl, (int) r.getDimension(R.dimen.j_action_bar_icon_vertical_padding), pxr, (int) r.getDimension(R.dimen.j_action_bar_icon_vertical_padding));
            logoImageView.setLayoutParams(params);
        } else {
            Log.e(TAG, "logoImageView is disabled!!!");
        }
        if (titleTextView != null) {
            LayoutParams params = (LayoutParams) titleTextView.getLayoutParams();
            params.setMargins(pxl, pxl, pxl, pxl);
            titleTextView.setLayoutParams(params);
        } else {
            Log.e(TAG, "titleTextView is disabled!!!");
        }
        if (subtitleTextView != null) {
            LayoutParams params = (LayoutParams) subtitleTextView.getLayoutParams();
            params.setMargins(pxl, pxl, pxl, pxl);
            subtitleTextView.setLayoutParams(params);
        } else {
            Log.e(TAG, "subtitleTextView is disabled!!!");
        }
        if (nav != null) {
            LayoutParams params = (LayoutParams) nav.getLayoutParams();
            params.setMargins(pxl, pxl, pxl, pxl);
            nav.setLayoutParams(params);
        } else {
            Log.e(TAG, "nav is disabled!!!");
        }
    }

}