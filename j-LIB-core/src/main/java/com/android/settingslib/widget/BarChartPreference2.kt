package com.android.settingslib.widget

import android.content.Context
import android.util.AttributeSet
import io.github.dot166.jlib.R

@Deprecated("STUB!, this class never really worked that well, this is a stub, please impl this yourself if you need it")
class BarChartPreference2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : BannerMessagePreference(context, attrs) {
    init {
        setTitle(R.string.deprecated)
        setAttentionLevel(AttentionLevel.HIGH)
        setNegativeButtonVisible(false)
        setPositiveButtonVisible(false)
        setDismissButtonVisible(false)
    }
}
