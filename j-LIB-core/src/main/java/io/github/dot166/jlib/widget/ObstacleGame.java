/*
 * Copyright (C) 2014 The Android Open Source Project
 * adapted to jLib in 2025 by ._______166
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

package io.github.dot166.jlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;

import java.util.Random;

import io.github.dot166.jlib.game.GameObject;
import io.github.dot166.jlib.game.GameView;
import io.github.dot166.jlib.game.Obstacle;
import io.github.dot166.jlib.game.Scenery;

public class ObstacleGame extends GameView {
    public static final String TAG = "jLib ObstacleGame";

    public static final boolean DEBUG_IDDQD = false;

    private int mTimePoke;

    public ObstacleGame(Context context) {
        this(context, null);
    }

    public ObstacleGame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObstacleGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTag(TAG);
    }

    @Override
    protected void start(boolean startPlaying) {
        super.start(startPlaying);
        mTimePoke = 0;
    }

    @Override
    protected void step(long t_ms, long dt_ms) {
        t = t_ms / 1000f; // seconds
        dt = dt_ms / 1000f;

        if (DEBUG) {
            t *= DEBUG_SPEED_MULTIPLIER;
            dt *= DEBUG_SPEED_MULTIPLIER;
        }

        // 1. Move all objects and update bounds
        final int N = getChildCount();
        int i = 0;
        for (; i < N; i++) {
            final View v = getChildAt(i);
            if (v instanceof GameObject) {
                ((GameObject) v).step(t_ms, dt_ms, t, dt);
            }
        }

        // 2. Check for altitude
        if (mPlaying && mDroid.below(mHeight)) {
            mDroid.setTranslationY(mHeight - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            mDroid.setRotation(0);
            mTimePoke = 0;
        } else {
            mTimePoke++;
            if (mTimePoke == 100) {
                stop();
            }
        }

        // 3. Check for obstacles
        boolean passedBarrier = false;
        for (int j = mObstaclesInPlay.size(); j-- > 0; ) {
            final Obstacle ob = mObstaclesInPlay.get(j);
            if (mPlaying && ob.intersects(mDroid) && !DEBUG_IDDQD) {
                L("player hit an obstacle");
                stop();
            } else if (ob.cleared(mDroid)) {
                if (ob instanceof Stem) passedBarrier = true;
                mObstaclesInPlay.remove(j);
            }
        }

        if (mPlaying && passedBarrier) {
            addScore(1);
        }

        // 4. Handle edge of screen
        // Walk backwards to make sure removal is safe
        while (i-- > 0) {
            final View v = getChildAt(i);
            if (v instanceof Obstacle) {
                if (v.getTranslationX() + v.getWidth() < 0) {
                    removeViewAt(i);
                }
            } else if (v instanceof Scenery) {
                final Scenery s = (Scenery) v;
                if (v.getTranslationX() + s.w < 0) {
                    v.setTranslationX(getWidth());
                }
            }
        }

        // 3. Time for more obstacles!
        if (mPlaying && (t - mLastObstacleTime) > PARAMS.OBSTACLE_PERIOD) {
            mLastObstacleTime = t;
            int obstacley;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                obstacley = new Random().nextInt((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 262, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 287, getResources().getDisplayMetrics()));
            } else {
                obstacley = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 275, getResources().getDisplayMetrics());
            }

            final int inset = (PARAMS.OBSTACLE_WIDTH - PARAMS.OBSTACLE_STEM_WIDTH) / 2;
            final int yinset = PARAMS.OBSTACLE_WIDTH / 2;

//            final int d1 = irand(0, 250);
//            final Stem s1 = new Stem(getContext(), obstacley - yinset, false);
//            s1.setColor(0xFFC0CB);
//            addView(s1, new LayoutParams(
//                    PARAMS.OBSTACLE_STEM_WIDTH,
//                    (int) s1.h,
//                    Gravity.TOP | Gravity.LEFT));
//            s1.setTranslationX(mWidth + inset);
//            s1.setTranslationY(-s1.h - yinset);
//            s1.setTranslationZ(PARAMS.OBSTACLE_Z * 0.75f);
//            s1.animate()
//                    .translationY(0)
//                    .setStartDelay(d1)
//                    .setDuration(250);
//            mObstaclesInPlay.add(s1);

            final int d2 = irand(0, 250);
            final Obstacle s2 = new Stem(getContext(),
                    mHeight - obstacley - PARAMS.OBSTACLE_GAP - yinset,
                    true);
            addView(s2, new LayoutParams(
                    PARAMS.OBSTACLE_STEM_WIDTH,
                    (int) s2.h,
                    Gravity.TOP | Gravity.LEFT));
            s2.setTranslationX(mWidth + inset);
            s2.setTranslationY(mHeight + yinset);
            s2.setTranslationZ(PARAMS.OBSTACLE_Z * 0.75f);
            s2.animate()
                    .translationY(mHeight - s2.h)
                    .setStartDelay(d2)
                    .setDuration(400);
            mObstaclesInPlay.add(s2);
        }

        if (DEBUG_DRAW) invalidate();
    }

    private class Stem extends Obstacle {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path mShadow = new Path();
        boolean mDrawShadow;

        public void setColor(int i) {
            mPaint.setColor(i);
        }

        public Stem(Context context, float h, boolean drawShadow) {
            super(context, h);
            mDrawShadow = drawShadow;
            mPaint.setColor(0xFF00AA00);
            setBackground(null);
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            setWillNotDraw(false);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRect(0, 0, getWidth(), getHeight());
                }
            });
        }

        @Override
        public void onDraw(Canvas c) {
            final int w = c.getWidth();
            final int h = c.getHeight();
            final GradientDrawable g = new GradientDrawable();
            g.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            g.setGradientCenter(w * 0.75f, 0);
            g.setColors(new int[]{0xFF00FF00, 0xFF00AA00});
            g.setBounds(0, 0, w, h);
            g.draw(c);
            if (!mDrawShadow) return;
            mShadow.reset();
            mShadow.moveTo(0, 0);
            mShadow.lineTo(w, 0);
            mShadow.lineTo(w, PARAMS.OBSTACLE_WIDTH / 2 + w * 1.5f);
            mShadow.lineTo(0, PARAMS.OBSTACLE_WIDTH / 2);
            mShadow.close();
            c.drawPath(mShadow, mPaint);
        }
    }

    private class Floor extends Scenery {
        public Floor(Context context) {
            super(context);

            w = mWidth;//irand(PARAMS.BUILDING_WIDTH_MIN, PARAMS.BUILDING_WIDTH_MAX);
            h = 0; // will be setup later, along with z

            setTranslationZ(PARAMS.SCENERY_Z);
        }
    }
}