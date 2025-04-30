package io.github.dot166.jlib.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.R;

import java.util.Objects;

import io.github.dot166.jlib.themeengine.ThemeEngine;

/**
 * custom {@link MaterialDivider} for use in dialogs, size is hardcoded nto 5dp and the colour is hard set to the colour secondary attribute, this view deletes itself when a jTheme (or descendant) is not selected in {@link io.github.dot166.jlib.themeengine.ThemeEngine}, if this is not a custom dialog (not extending {@link androidx.appcompat.app.AppCompatDialog} (or descendant)) please use {@link MaterialDivider} instead
 */
public class DialogBar extends MaterialDivider {

    public DialogBar(@NonNull Context context) {
        this(context, null);
    }

    public DialogBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.materialDividerStyle);
    }

    public DialogBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (Objects.equals(ThemeEngine.getTmpCurrentTheme(), "jLib")) {
            // sets top colour to the colour secondary attribute, if that fails it would set to the default secondary colour attribute for android 11 and older, this also force sets the size to 5dp
            setDividerColor(getContext().getTheme().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSecondary}).getColor(0, getResources().getColor(android.R.color.holo_blue_dark)));
            setDividerThickness(getResources().getDimensionPixelSize(io.github.dot166.jlib.R.dimen.dialog_bar_thickness));
        } else {
            setDividerThickness(0); // hides it
        }
    }
}
