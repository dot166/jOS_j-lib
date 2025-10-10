package io.github.dot166.jlib.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.res.TypedArrayUtils;

/**
 * @deprecated just use {@link SliderPreference}, this is just a wrapper round it anyway
 */
@Deprecated
public class SeekBarPreference extends SliderPreference {
    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("RestrictedApi")
    public SeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context,
                androidx.preference.R.attr.preferenceStyle,
                android.R.attr.preferenceStyle));
    }

    public SeekBarPreference(Context context) {
        this(context, null);
    }
}
