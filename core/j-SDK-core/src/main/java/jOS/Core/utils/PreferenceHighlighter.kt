/*
 * Copyright (C) 2018 The Android Open Source Project
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
package jOS.Core.utils

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Property
import android.view.View
import androidx.annotation.ColorInt
import androidx.preference.Preference
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Utility class for highlighting a preference
 */
class PreferenceHighlighter(
    private val mRv: RecyclerView,
    private val mIndex: Int,
    private val mPreference: Preference
) : ItemDecoration(), Runnable {
    private val mPaint = Paint()
    private val mDrawRect = RectF()

    private var mHighLightStarted = false
    private var mHighlightColor = END_COLOR

    override fun run() {
        mRv.addItemDecoration(this)
        mRv.smoothScrollToPosition(mIndex)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val holder = parent.findViewHolderForAdapterPosition(mIndex) ?: return
        if (!mHighLightStarted && state.remainingScrollVertical != 0) {
            // Wait until scrolling stopped
            return
        }

        if (!mHighLightStarted) {
            // Start highlight
            val colorTo = setColorAlphaBound(getColorAccent(mRv.context), 66)
            val anim = ObjectAnimator.ofArgb(
                this, HIGHLIGHT_COLOR, END_COLOR,
                colorTo
            )
            anim.setDuration(HIGHLIGHT_FADE_IN_DURATION)
            anim.repeatMode = ValueAnimator.REVERSE
            anim.repeatCount = 4
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeHighlight()
                }
            })
            anim.start()
            mHighLightStarted = true
        }

        val view = holder.itemView
        mPaint.color = mHighlightColor
        mDrawRect[0f, view.y, parent.width.toFloat()] = view.y + view.height
        if (mPreference is HighlightDelegate) {
            (mPreference as HighlightDelegate).offsetHighlight(view, mDrawRect)
        }
        c.drawRect(mDrawRect, mPaint)
    }

    private fun removeHighlight() {
        val anim = ObjectAnimator.ofArgb(
            this, HIGHLIGHT_COLOR, mHighlightColor, END_COLOR
        )
        anim.setDuration(HIGHLIGHT_FADE_OUT_DURATION)
        anim.startDelay = HIGHLIGHT_DURATION
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mRv.removeItemDecoration(this@PreferenceHighlighter)
            }
        })
        anim.start()
    }

    /**
     * Interface to be implemented by a preference to customize the highlight are
     */
    interface HighlightDelegate {
        /**
         * Allows the preference to update the highlight area
         */
        fun offsetHighlight(prefView: View?, bounds: RectF?)
    }

    companion object {
        private val HIGHLIGHT_COLOR: Property<PreferenceHighlighter, Int> =
            object : Property<PreferenceHighlighter, Int>(
                Integer.TYPE, "highlightColor"
            ) {
                override fun get(highlighter: PreferenceHighlighter): Int {
                    return highlighter.mHighlightColor
                }

                override fun set(highlighter: PreferenceHighlighter, value: Int) {
                    highlighter.mHighlightColor = value
                    highlighter.mRv.invalidateItemDecorations()
                }
            }

        private const val HIGHLIGHT_DURATION = 15000L
        private const val HIGHLIGHT_FADE_OUT_DURATION = 500L
        private const val HIGHLIGHT_FADE_IN_DURATION = 200L
        private val END_COLOR = setColorAlphaBound(Color.WHITE, 0)

        /**
         * Set the alpha component of `color` to be `alpha`. Unlike the support lib version,
         * it bounds the alpha in valid range instead of throwing an exception to allow for safer
         * interpolation of color animations
         */
        @ColorInt
        fun setColorAlphaBound(color: Int, alpha: Int): Int {
            var alpha = alpha
            if (alpha < 0) {
                alpha = 0
            } else if (alpha > 255) {
                alpha = 255
            }
            return (color and 0x00ffffff) or (alpha shl 24)
        }

        fun getColorAccent(context: Context): Int {
            return getAttrColor(context, R.attr.colorAccent)
        }

        /**
         * Returns the color associated with the attribute
         */
        fun getAttrColor(context: Context, attr: Int): Int {
            val ta = context.obtainStyledAttributes(intArrayOf(attr))
            val colorAccent = ta.getColor(0, 0)
            ta.recycle()
            return colorAccent
        }
    }
}