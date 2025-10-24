package io.github.dot166.jlib.preference

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import io.github.dot166.jlib.R
import io.github.dot166.jlib.utils.ErrorUtils.handle
import java.text.MessageFormat
import kotlin.math.min

open class SliderPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes), Slider.OnChangeListener {
    protected var mInterval: Int = 1
    protected var mUnits: String = ""

    protected var mMinValue: Int = 0
    protected var mMaxValue: Int = 100
    protected var mDefaultValueExists: Boolean = false
    protected var mDefaultValue: Int = 0

    protected var mValue: Int = 0

    protected var mValueTextView: TextView? = null
    protected var mResetImageButton: MaterialButton? = null
    protected var mMinusImageButton: MaterialButton? = null
    protected var mPlusImageButton: MaterialButton? = null
    protected var mSlider: Slider? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.jLibSliderPreference)
        try {
            val units = a.getString(R.styleable.jLibSliderPreference_units)
            if (units != null) mUnits = " $units"
            mMinValue = a.getInt(R.styleable.jLibSliderPreference_android_min, mMinValue)
            mMaxValue = a.getInt(R.styleable.jLibSliderPreference_android_max, mMaxValue)
            mInterval = a.getInt(R.styleable.jLibSliderPreference_interval, mInterval)
            val defaultValue = a.getString(R.styleable.jLibSliderPreference_android_defaultValue)
            mDefaultValueExists = defaultValue != null && !defaultValue.isEmpty()
            if (mDefaultValueExists) {
                mDefaultValue = getLimitedValue(defaultValue!!.toInt())
                mValue = mDefaultValue
            } else {
                mValue = mMinValue
            }
        } finally {
            a.recycle()
        }
        if (mMaxValue < mMinValue) mMaxValue = mMinValue

        mSlider = Slider(context, attrs)
        layoutResource = R.layout.preference_slider
    }

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs, TypedArrayUtils.getAttr(
            context,
            androidx.preference.R.attr.preferenceStyle,
            android.R.attr.preferenceStyle
        )
    )

    constructor(context: Context) : this(context, null)

    override fun onDependencyChanged(dependency: Preference, disableDependent: Boolean) {
        super.onDependencyChanged(dependency, disableDependent)
        shouldDisableView = true
        if (mSlider != null) mSlider!!.setEnabled(!disableDependent)
        if (mResetImageButton != null) mResetImageButton!!.setEnabled(!disableDependent)
        if (mPlusImageButton != null) mPlusImageButton!!.setEnabled(!disableDependent)
        if (mMinusImageButton != null) mMinusImageButton!!.setEnabled(!disableDependent)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        try {
            // move our slider to the new view we've been given
            val oldContainer = mSlider!!.parent
            val newContainer = holder.findViewById(R.id.slider) as ViewGroup
            if (oldContainer !== newContainer) {
                // remove the slider from the old view
                if (oldContainer != null) {
                    (oldContainer as ViewGroup).removeView(mSlider)
                }
                // remove the existing slider (there may not be one) and add ours
                newContainer.removeAllViews()
                newContainer.addView(
                    mSlider, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        } catch (ex: Exception) {
            handle(ex, context)
        }

        mSlider!!.setValueTo(getSeekValue(mMaxValue).toFloat())
        mSlider!!.setValueFrom(getSeekValue(mMinValue).toFloat())
        mSlider!!.value = getSeekValue(mValue).toFloat()
        mSlider!!.setEnabled(isEnabled)

        mValueTextView = holder.findViewById(R.id.value) as TextView?
        mResetImageButton = holder.findViewById(R.id.reset) as MaterialButton?
        mMinusImageButton = holder.findViewById(R.id.minus) as MaterialButton?
        mPlusImageButton = holder.findViewById(R.id.plus) as MaterialButton?

        updateValueViews()

        mSlider!!.addOnChangeListener(this)
        mResetImageButton!!.setOnClickListener { view: View? ->
            Snackbar.make(
                view!!,
                context.getString(
                    R.string.slider_default_value_to_set,
                    getTextValue(mDefaultValue)
                ),
                Snackbar.LENGTH_LONG
            ).show()
        }
        mResetImageButton!!.setOnLongClickListener { view: View? ->
            setValue(mDefaultValue, true)
            true
        }
        mMinusImageButton!!.setOnClickListener { view: View? ->
            setValue(
                mValue - mInterval,
                true
            )
        }
        mPlusImageButton!!.setOnClickListener { view: View? ->
            setValue(
                mValue + mInterval,
                true
            )
        }
    }

    protected fun getLimitedValue(v: Int): Int {
        return if (v < mMinValue) mMinValue else (min(v, mMaxValue))
    }

    protected fun getSeekValue(v: Int): Int {
        return -Math.floorDiv(mMinValue - v, mInterval)
    }

    protected fun getTextValue(v: Int): String {
        return (if (v > 0) "+" else "") + v.toString() + mUnits
    }

    protected fun updateValueViews() {
        if (mValueTextView != null) {
            mValueTextView!!.text = MessageFormat.format(
                "{0}{1}",
                context.getString(R.string.slider_value, getTextValue(mValue)),
                if (mDefaultValueExists && mValue == mDefaultValue) (" (" +
                        context.getString(R.string.slider_default_value) + ")") else ""
            )
        }
        if (mResetImageButton != null) {
            if (!mDefaultValueExists || mValue == mDefaultValue) mResetImageButton!!.visibility =
                MaterialButton.INVISIBLE
            else mResetImageButton!!.visibility = MaterialButton.VISIBLE
        }
        if (mMinusImageButton != null) {
            if (mValue == mMinValue) {
                mMinusImageButton!!.visibility = MaterialButton.INVISIBLE
            } else {
                mMinusImageButton!!.visibility = MaterialButton.VISIBLE
            }
        }
        if (mPlusImageButton != null) {
            if (mValue == mMaxValue) {
                mPlusImageButton!!.visibility = MaterialButton.INVISIBLE
            } else {
                mPlusImageButton!!.visibility = MaterialButton.VISIBLE
            }
        }
    }

    override fun onValueChange(slider: Slider, progress: Float, fromUser: Boolean) {
        val newValue = getLimitedValue((mMinValue + (progress * mInterval)).toInt())
        if (mValue != newValue) {
            // change rejected, revert to the previous value
            if (!callChangeListener(newValue)) {
                mSlider!!.value = getSeekValue(mValue).toFloat()
                return
            }
            // change accepted, store it
            persistInt(newValue)

            mValue = newValue
            updateValueViews()
        }
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        if (shouldPersist()) {
            mValue = getPersistedInt(mValue)
        }
    }

    override fun setDefaultValue(defaultValue: Any?) {
        if (defaultValue is Int) setDefaultValue(defaultValue, mSlider != null)
        else setDefaultValue(
            defaultValue?.toString() ?: null as String?,
            mSlider != null
        )
    }

    fun setDefaultValue(newValue: Int, update: Boolean) {
        var newValue = newValue
        newValue = getLimitedValue(newValue)
        if (!mDefaultValueExists || mDefaultValue != newValue) {
            mDefaultValueExists = true
            mDefaultValue = newValue
            if (update) updateValueViews()
        }
    }

    fun setDefaultValue(newValue: String?, update: Boolean) {
        if (mDefaultValueExists && (newValue == null || newValue.isEmpty())) {
            mDefaultValueExists = false
            if (update) updateValueViews()
        } else if (newValue != null && !newValue.isEmpty()) {
            setDefaultValue(newValue.toInt(), update)
        }
    }

    fun setValue(newValue: Int, update: Boolean) {
        var newValue = newValue
        newValue = getLimitedValue(newValue)
        if (mValue != newValue) {
            if (update) mSlider!!.value = getSeekValue(newValue).toFloat()
            else mValue = newValue
        }
    }

    var value: Int
        get() = mValue
        set(newValue) {
            mValue = getLimitedValue(newValue)
            if (mSlider != null) mSlider!!.setValue(getSeekValue(mValue).toFloat())
        }

    fun refresh(newValue: Int) {
        // this will ...
        setValue(newValue, mSlider != null)
    }
}