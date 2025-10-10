package io.github.dot166.jlib.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.utils.ErrorUtils;

public class SliderPreference extends Preference implements Slider.OnChangeListener {

    protected int mInterval = 1;
    protected String mUnits = "";

    protected int mMinValue = 0;
    protected int mMaxValue = 100;
    protected boolean mDefaultValueExists = false;
    protected int mDefaultValue;

    protected int mValue;

    protected TextView mValueTextView;
    protected MaterialButton mResetImageButton;
    protected MaterialButton mMinusImageButton;
    protected MaterialButton mPlusImageButton;
    protected Slider mSlider;

    public SliderPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.jLibSliderPreference);
        try {
            String units = a.getString(R.styleable.jLibSliderPreference_units);
            if (units != null)
                mUnits = " " + units;
            mMinValue = a.getInt(R.styleable.jLibSliderPreference_android_min, mMinValue);
            mMaxValue = a.getInt(R.styleable.jLibSliderPreference_android_max, mMaxValue);
            mInterval = a.getInt(R.styleable.jLibSliderPreference_interval, mInterval);
            String defaultValue = a.getString(R.styleable.jLibSliderPreference_android_defaultValue);
            mDefaultValueExists = defaultValue != null && !defaultValue.isEmpty();
            if (mDefaultValueExists) {
                mDefaultValue = getLimitedValue(Integer.parseInt(defaultValue));
                mValue = mDefaultValue;
            } else {
                mValue = mMinValue;
            }
        } finally {
            a.recycle();
        }
        if (mMaxValue < mMinValue)
            mMaxValue = mMinValue;

        mSlider = new Slider(context, attrs);
        setLayoutResource(R.layout.preference_slider);
    }

    public SliderPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("RestrictedApi")
    public SliderPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context,
                androidx.preference.R.attr.preferenceStyle,
                android.R.attr.preferenceStyle));
    }

    public SliderPreference(Context context) {
        this(context, null);
    }

    @Override
    public void onDependencyChanged(@NonNull Preference dependency, boolean disableDependent) {
        super.onDependencyChanged(dependency, disableDependent);
        this.setShouldDisableView(true);
        if (mSlider != null)
            mSlider.setEnabled(!disableDependent);
        if (mResetImageButton != null)
            mResetImageButton.setEnabled(!disableDependent);
        if (mPlusImageButton != null)
            mPlusImageButton.setEnabled(!disableDependent);
        if (mMinusImageButton != null)
            mMinusImageButton.setEnabled(!disableDependent);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        try
        {
            // move our slider to the new view we've been given
            ViewParent oldContainer = mSlider.getParent();
            ViewGroup newContainer = (ViewGroup) holder.findViewById(R.id.slider);
            if (oldContainer != newContainer) {
                // remove the slider from the old view
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(mSlider);
                }
                // remove the existing slider (there may not be one) and add ours
                newContainer.removeAllViews();
                newContainer.addView(mSlider, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception ex) {
            ErrorUtils.handle(ex, getContext());
        }

        mSlider.setValueTo(getSeekValue(mMaxValue));
        mSlider.setValueFrom(getSeekValue(mMinValue));
        mSlider.setValue(getSeekValue(mValue));
        mSlider.setEnabled(isEnabled());

        mValueTextView = (TextView) holder.findViewById(R.id.value);
        mResetImageButton = (MaterialButton) holder.findViewById(R.id.reset);
        mMinusImageButton = (MaterialButton) holder.findViewById(R.id.minus);
        mPlusImageButton = (MaterialButton) holder.findViewById(R.id.plus);

        updateValueViews();

        mSlider.addOnChangeListener(this);
        mResetImageButton.setOnClickListener(view -> Snackbar.make(view, getContext().getString(R.string.slider_default_value_to_set, getTextValue(mDefaultValue)), Snackbar.LENGTH_LONG).show());
        mResetImageButton.setOnLongClickListener(view -> {
            setValue(mDefaultValue, true);
            return true;
        });
        mMinusImageButton.setOnClickListener(view -> setValue(mValue - mInterval, true));
        mPlusImageButton.setOnClickListener(view -> setValue(mValue + mInterval, true));
    }

    protected int getLimitedValue(int v) {
        return v < mMinValue ? mMinValue : (Math.min(v, mMaxValue));
    }

    protected int getSeekValue(int v) {
        return -Math.floorDiv(mMinValue - v, mInterval);
    }

    protected String getTextValue(int v) {
        return (v > 0 ? "+" : "") + String.valueOf(v) + mUnits;
    }

    protected void updateValueViews() {
        if (mValueTextView != null) {
            mValueTextView.setText(MessageFormat.format("{0}{1}", getContext().getString(R.string.slider_value, getTextValue(mValue)), mDefaultValueExists && mValue == mDefaultValue ? " (" +
                    getContext().getString(R.string.slider_default_value) + ")" : ""));
        }
        if (mResetImageButton != null) {
            if (!mDefaultValueExists || mValue == mDefaultValue)
                mResetImageButton.setVisibility(MaterialButton.INVISIBLE);
            else
                mResetImageButton.setVisibility(MaterialButton.VISIBLE);
        }
        if (mMinusImageButton != null) {
            if (mValue == mMinValue) {
                mMinusImageButton.setVisibility(MaterialButton.INVISIBLE);
            } else {
                mMinusImageButton.setVisibility(MaterialButton.VISIBLE);
            }
        }
        if (mPlusImageButton != null) {
            if (mValue == mMaxValue) {
                mPlusImageButton.setVisibility(MaterialButton.INVISIBLE);
            } else {
                mPlusImageButton.setVisibility(MaterialButton.VISIBLE);
            }
        }
    }

    @Override
    public void onValueChange(@NonNull Slider slider, float progress, boolean fromUser) {
        int newValue = getLimitedValue((int) (mMinValue + (progress * mInterval)));
        if (mValue != newValue) {
            // change rejected, revert to the previous value
            if (!callChangeListener(newValue)) {
                mSlider.setValue(getSeekValue(mValue));
                return;
            }
            // change accepted, store it
            persistInt(newValue);

            mValue = newValue;
            updateValueViews();
        }
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        if (shouldPersist()) {
            mValue = getPersistedInt(mValue);
        }
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        if (defaultValue instanceof Integer)
            setDefaultValue((Integer) defaultValue, mSlider != null);
        else
            setDefaultValue(defaultValue == null ? (String) null : defaultValue.toString(), mSlider != null);
    }

    public void setDefaultValue(int newValue, boolean update) {
        newValue = getLimitedValue(newValue);
        if (!mDefaultValueExists || mDefaultValue != newValue) {
            mDefaultValueExists = true;
            mDefaultValue = newValue;
            if (update)
                updateValueViews();
        }
    }

    public void setDefaultValue(String newValue, boolean update) {
        if (mDefaultValueExists && (newValue == null || newValue.isEmpty())) {
            mDefaultValueExists = false;
            if (update)
                updateValueViews();
        } else if (newValue != null && !newValue.isEmpty()) {
            setDefaultValue(Integer.parseInt(newValue), update);
        }
    }

    public void setValue(int newValue) {
        mValue = getLimitedValue(newValue);
        if (mSlider != null) mSlider.setValue(getSeekValue(mValue));
    }

    public void setValue(int newValue, boolean update) {
        newValue = getLimitedValue(newValue);
        if (mValue != newValue) {
            if (update)
                mSlider.setValue(getSeekValue(newValue));
            else
                mValue = newValue;
        }
    }

    public int getValue() {
        return mValue;
    }

    public void refresh(int newValue) {
        // this will ...
        setValue(newValue, mSlider != null);
    }
}