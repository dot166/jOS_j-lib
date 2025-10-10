package io.github.dot166.jlib.widget;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * @deprecated please use {@code com.google.android.material.appbar.MaterialToolbar} instead, this is just a wrapper around it now, doing nothing
 */
@Deprecated
public class ActionBar2 extends MaterialToolbar {

    private static final int DEF_STYLE_RES = com.google.android.material.R.style.Widget_Material3_Toolbar;

    public ActionBar2(@NonNull Context context) {
        this(context, null);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public ActionBar2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
    }

}