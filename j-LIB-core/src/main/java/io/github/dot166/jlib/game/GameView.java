package io.github.dot166.jlib.game;

import android.animation.TimeAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.dot166.jlib.R;

public class GameView extends FrameLayout {

    private static final String defaultTag = "jLib Game";

    protected static String mTag = defaultTag;
    public static boolean AUTOSTART = true;

    public static boolean HAVE_STARS = true;

    public static final boolean DEBUG = Log.isLoggable(mTag, Log.DEBUG);
    public static final boolean DEBUG_DRAW = false; // DEBUG

    public static final void L(String s, Object... objects) {
        if (DEBUG) {
            Log.d(mTag, String.format(s, objects));
        }
    }

    protected static void setTag(String tag) {
        mTag = tag;
    }

    protected static Params PARAMS;

    protected static class Params {
        public float TRANSLATION_PER_SEC;
        public int OBSTACLE_SPACING, OBSTACLE_PERIOD;
        public int BOOST_DV;
        public int PLAYER_HIT_SIZE;
        public int PLAYER_SIZE;
        public int OBSTACLE_WIDTH, OBSTACLE_STEM_WIDTH;
        public int OBSTACLE_GAP;
        public int OBSTACLE_MIN;
        public int BUILDING_WIDTH_MIN, BUILDING_WIDTH_MAX;
        public int BUILDING_HEIGHT_MIN;
        public int CLOUD_SIZE_MIN, CLOUD_SIZE_MAX;
        public int STAR_SIZE_MIN, STAR_SIZE_MAX;
        public int G;
        public int MAX_V;
        public float SCENERY_Z, OBSTACLE_Z, PLAYER_Z, PLAYER_Z_BOOST, HUD_Z;

        public Params(Resources res) {
            TRANSLATION_PER_SEC = res.getDimension(R.dimen.l_translation_per_sec);
            OBSTACLE_SPACING = res.getDimensionPixelSize(R.dimen.l_obstacle_spacing);
            OBSTACLE_PERIOD = (int) (OBSTACLE_SPACING / TRANSLATION_PER_SEC);
            BOOST_DV = res.getDimensionPixelSize(R.dimen.l_boost_dv);
            PLAYER_HIT_SIZE = res.getDimensionPixelSize(R.dimen.l_player_hit_size);
            PLAYER_SIZE = res.getDimensionPixelSize(R.dimen.l_player_size);
            OBSTACLE_WIDTH = res.getDimensionPixelSize(R.dimen.l_obstacle_width);
            OBSTACLE_STEM_WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, res.getDisplayMetrics());
            OBSTACLE_GAP = res.getDimensionPixelSize(R.dimen.l_obstacle_gap);
            OBSTACLE_MIN = res.getDimensionPixelSize(R.dimen.l_obstacle_height_min);
            BUILDING_HEIGHT_MIN = res.getDimensionPixelSize(R.dimen.l_building_height_min);
            BUILDING_WIDTH_MIN = res.getDimensionPixelSize(R.dimen.l_building_width_min);
            BUILDING_WIDTH_MAX = res.getDimensionPixelSize(R.dimen.l_building_width_max);
            CLOUD_SIZE_MIN = res.getDimensionPixelSize(R.dimen.l_cloud_size_min);
            CLOUD_SIZE_MAX = res.getDimensionPixelSize(R.dimen.l_cloud_size_max);
            STAR_SIZE_MIN = res.getDimensionPixelSize(R.dimen.l_star_size_min);
            STAR_SIZE_MAX = res.getDimensionPixelSize(R.dimen.l_star_size_max);

            G = res.getDimensionPixelSize(R.dimen.l_G);
            MAX_V = res.getDimensionPixelSize(R.dimen.l_max_v);

            SCENERY_Z = res.getDimensionPixelSize(R.dimen.l_scenery_z);
            OBSTACLE_Z = res.getDimensionPixelSize(R.dimen.l_obstacle_z);
            PLAYER_Z = res.getDimensionPixelSize(R.dimen.l_player_z);
            PLAYER_Z_BOOST = res.getDimensionPixelSize(R.dimen.l_player_z_boost);
            HUD_Z = res.getDimensionPixelSize(R.dimen.l_hud_z);
        }
    }

    public float getLastTimeStep() {
        return dt;
    }

    public void setSplash(View v) {
        mSplash = v;
    }

    protected void addScore(int incr) {
        setScore(mScore + incr);
    }
    protected int mWidth, mHeight;

    protected int mTimeOfDay;

    protected int mScore;

    protected TimeAnimator mAnim;
    protected boolean mAnimating, mPlaying;
    protected boolean mFrozen; // after death, a short backoff

    protected TextView mScoreField;
    protected Player mDroid;
    protected float t, dt;
    public static final float DEBUG_SPEED_MULTIPLIER = 1f; // 0.1f;
    protected static final int DAY = 0, NIGHT = 1, TWILIGHT = 2, SUNSET = 3;

    protected float mLastObstacleTime; // in sec
    protected View mSplash;

    protected ArrayList<Obstacle> mObstaclesInPlay = new ArrayList<Obstacle>();
    protected static final int[][] SKIES = {
            {0xFFc0c0FF, 0xFFa0a0FF}, // DAY
            {0xFF000010, 0xFF000000}, // NIGHT
            {0xFF000040, 0xFF000010}, // TWILIGHT
            {0xFFa08020, 0xFF204080}, // SUNSET
    };

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        PARAMS = new Params(getResources());
        mTimeOfDay = irand(0, SKIES.length);
    }

    public int getGameWidth() {
        return mWidth;
    }

    public int getGameHeight() {
        return mHeight;
    }

    public void setScoreField(TextView tv) {
        mScoreField = tv;
        setScore(mScore);
        if (tv != null) {
            tv.setTranslationZ(PARAMS.HUD_Z);
            if (!(mAnimating && mPlaying)) {
                tv.setTranslationY(-500);
            }
        }
    }

    protected void setScore(int score) {
        mScore = score;
        if (mScoreField != null) mScoreField.setText(String.valueOf(score));
    }

    @Override
    public boolean willNotDraw() {
        return !DEBUG;
    }

    public static final int irand(int a, int b) {
        return (int) lerp(frand(), (float) a, (float) b);
    }

    public static final float lerp(float x, float a, float b) {
        return (b - a) * x + a;
    }

    public static final float frand() {
        return (float) Math.random();
    }

    public static final float rlerp(float v, float a, float b) {
        return (v - a) / (b - a);
    }

    public static final float clamp(float f) {
        return f < 0f ? 0f : f > 1f ? 1f : f;
    }

    public static final float frand(float a, float b) {
        return lerp(frand(), a, b);
    }

    protected void stop() {
        if (mAnimating) {
            mAnim.cancel();
            mAnim = null;
            mAnimating = false;
            mScoreField.setTextColor(0xFFFFFFFF);
            mScoreField.setBackgroundResource(R.drawable.l_scorecard_gameover);
            mTimeOfDay = irand(0, SKIES.length); // for next reset
            mFrozen = true;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFrozen = false;
                }
            }, 250);
        }
    }

    protected void poke() {
        L("poke");
        if (mFrozen) return;
        if (!mAnimating) {
            reset();
            start(true);
        } else if (!mPlaying) {
            start(true);
        }
        mDroid.boost();
        if (DEBUG) {
            mDroid.dv *= DEBUG_SPEED_MULTIPLIER;
            mDroid.animate().setDuration((long) (200 / DEBUG_SPEED_MULTIPLIER));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        stop();
        reset();
        if (AUTOSTART) {
            start(false);
        }
    }

    final float hsv[] = {0, 0, 0};

    protected void reset() {
        L("reset");
        final Drawable sky = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                SKIES[mTimeOfDay]
        );
        sky.setDither(true);
        setBackground(sky);

        setScore(0);

        int i = getChildCount();
        while (i-- > 0) {
            final View v = getChildAt(i);
            if (v instanceof GameObject) {
                removeViewAt(i);
            }
        }

        mObstaclesInPlay.clear();

        mWidth = getWidth();
        mHeight = getHeight();

        boolean showingSun = (mTimeOfDay == DAY || mTimeOfDay == SUNSET) && frand() > 0.25;
        if (showingSun) {
            final Star sun = new Star(getContext());
            sun.setBackgroundResource(R.drawable.l_sun);
            final int w = getResources().getDimensionPixelSize(R.dimen.l_sun_size);
            sun.setTranslationX(frand(w, mWidth - w));
            if (mTimeOfDay == DAY) {
                sun.setTranslationY(frand(w, (mHeight * 0.66f)));
                sun.getBackground().setTint(0);
            } else {
                sun.setTranslationY(frand(mHeight * 0.66f, mHeight - w));
                sun.getBackground().setTintMode(PorterDuff.Mode.SRC_ATOP);
                sun.getBackground().setTint(0xC0FF8000);

            }
            addView(sun, new LayoutParams(w, w));
        }
        if (!showingSun) {
            final boolean dark = mTimeOfDay == NIGHT || mTimeOfDay == TWILIGHT;
            final float ff = frand();
            if ((dark && ff < 0.75f) || ff < 0.5f) {
                final Star moon = new Star(getContext());
                moon.setBackgroundResource(R.drawable.l_moon);
                moon.getBackground().setAlpha(dark ? 255 : 128);
                moon.setScaleX(frand() > 0.5 ? -1 : 1);
                moon.setRotation(moon.getScaleX() * frand(5, 30));
                final int w = getResources().getDimensionPixelSize(R.dimen.l_sun_size);
                moon.setTranslationX(frand(w, mWidth - w));
                moon.setTranslationY(frand(w, mHeight - w));
                addView(moon, new LayoutParams(w, w));
            }
        }

        final int mh = mHeight / 6;
        final boolean cloudless = frand() < 0.25;
        final int N = 20;
        for (i = 0; i < N; i++) {
            final float r1 = frand();
            final Scenery s;
            if (HAVE_STARS && r1 < 0.3 && mTimeOfDay != DAY) {
                s = new Star(getContext());
            } else if (r1 < 0.6 && !cloudless) {
                s = new Cloud(getContext());
            } else {
                s = new Building(getContext());

                s.z = (float) i / N;
                s.setTranslationZ(PARAMS.SCENERY_Z * (1 + s.z));
                s.v = 0.85f * s.z; // buildings move proportional to their distance
                hsv[0] = 175;
                hsv[1] = 0.25f;
                hsv[2] = 1 * s.z;
                s.setBackgroundColor(Color.HSVToColor(hsv));
                s.h = irand(PARAMS.BUILDING_HEIGHT_MIN, mh);
            }
            final LayoutParams lp = new LayoutParams(s.w, s.h);
            if (s instanceof Building) {
                lp.gravity = Gravity.BOTTOM;
            } else {
                lp.gravity = Gravity.TOP;
                final float r = frand();
                if (s instanceof Star) {
                    lp.topMargin = (int) (r * r * mHeight);
                } else {
                    lp.topMargin = (int) (1 - r * r * mHeight / 2) + mHeight / 2;
                }
            }

            addView(s, lp);
            s.setTranslationX(frand(-lp.width, mWidth + lp.width));
        }

        mDroid = new Player(getContext());
        mDroid.setX(mWidth / 2);
        mDroid.setY(mHeight / 2);
        addView(mDroid, new LayoutParams(PARAMS.PLAYER_SIZE, PARAMS.PLAYER_SIZE));

        mAnim = new TimeAnimator();
        mAnim.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator timeAnimator, long t, long dt) {
                step(t, dt);
            }
        });
    }

    protected void unpoke() {
        L("unboost");
        if (mFrozen) return;
        if (!mAnimating) return;
        mDroid.unboost();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (DEBUG) L("touch: %s", ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                poke();
                return true;
            case MotionEvent.ACTION_UP:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (DEBUG) L("trackball: %s", ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                poke();
                return true;
            case MotionEvent.ACTION_UP:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        if (DEBUG) L("keyDown: %d", keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_BUTTON_A:
                poke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent ev) {
        if (DEBUG) L("keyDown: %d", keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_BUTTON_A:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent ev) {
        if (DEBUG) L("generic: %s", ev);
        return false;
    }

    public float getGameTime() {
        return t;
    }

    protected void start(boolean startPlaying) {
        L("start(startPlaying=%s)", startPlaying ? "true" : "false");
        if (startPlaying) {
            mPlaying = true;

            t = 0;
            // there's a sucker born every OBSTACLE_PERIOD
            mLastObstacleTime = getGameTime() - PARAMS.OBSTACLE_PERIOD;

            if (mSplash != null && mSplash.getAlpha() > 0f) {
                mSplash.setTranslationZ(PARAMS.HUD_Z);
                mSplash.animate().alpha(0).translationZ(0).setDuration(400);

                mScoreField.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(1500);
            }

            mScoreField.setTextColor(0xFFAAAAAA);
            mScoreField.setBackgroundResource(R.drawable.l_scorecard);
            mDroid.setVisibility(View.VISIBLE);
            mDroid.setX(mWidth / 2);
            mDroid.setY(mHeight / 2);
        } else {
            mDroid.setVisibility(View.GONE);
        }
        if (!mAnimating) {
            mAnim.start();
            mAnimating = true;
        }
    }

    final Paint pt = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        if (!DEBUG_DRAW) return;

//        final Paint pt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pt.setColor(0xFFFFFFFF);
        final int L = mDroid.corners.length;
        final int N = L / 2;
        for (int i = 0; i < N; i++) {
            final int x = (int) mDroid.corners[i * 2];
            final int y = (int) mDroid.corners[i * 2 + 1];
            c.drawCircle(x, y, 4, pt);
            c.drawLine(x, y,
                    mDroid.corners[(i * 2 + 2) % L],
                    mDroid.corners[(i * 2 + 3) % L],
                    pt);
        }

        pt.setStyle(Paint.Style.STROKE);
        pt.setStrokeWidth(getResources().getDisplayMetrics().density);

        final int M = getChildCount();
        pt.setColor(0x8000FF00);
        for (int i = 0; i < M; i++) {
            final View v = getChildAt(i);
            if (v == mDroid) continue;
            if (!(v instanceof GameObject)) continue;
            final Rect r = new Rect();
            v.getHitRect(r);
            c.drawRect(r, pt);
        }

        pt.setColor(Color.BLACK);
        final StringBuilder sb = new StringBuilder("obstacles: ");
        for (Obstacle ob : mObstaclesInPlay) {
            sb.append(ob.hitRect.toShortString());
            sb.append(" ");
        }
        pt.setTextSize(20f);
        c.drawText(sb.toString(), 20, 100, pt);
    }

    protected void step(long t_ms, long dt_ms) {
        // do nothing yet
    }
}
