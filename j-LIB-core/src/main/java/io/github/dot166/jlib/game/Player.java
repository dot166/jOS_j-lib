package io.github.dot166.jlib.game;

import static io.github.dot166.jlib.game.GameView.PARAMS;
import static io.github.dot166.jlib.game.GameView.clamp;
import static io.github.dot166.jlib.game.GameView.lerp;
import static io.github.dot166.jlib.game.GameView.rlerp;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewOutlineProvider;

import java.util.Random;

import io.github.dot166.jlib.R;

public class Player extends androidx.appcompat.widget.AppCompatImageView implements GameObject {
    public float dv;

    private boolean mBoosting;

    private final float[] sHull = new float[]{
            0.3f, 0f,    // left antenna
            0.7f, 0f,    // right antenna
            0.92f, 0.33f, // off the right shoulder of Orion
            0.92f, 0.75f, // right hand (our right, not his right)
            0.6f, 1f,    // right foot
            0.4f, 1f,    // left foot BLUE!
            0.08f, 0.75f, // sinistram
            0.08f, 0.33f,  // cold shoulder
    };
    public final float[] corners = new float[sHull.length];

    private int getRandomTint() {
        int[] colours = new int[] {0xFF680000, 0xFF006800, 0xFF000068};
        Random rand = new Random();
        return colours[rand.nextInt(3)];
    }

    public Player(Context context) {
        super(context);

        setBackgroundResource(R.drawable.l_android);// TODO: Create a new character drawable
        getBackground().setTintMode(PorterDuff.Mode.SRC_ATOP);
        getBackground().setTint(getRandomTint());
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                final int w = view.getWidth();
                final int h = view.getHeight();
                final int ix = (int) (w * 0.3f);
                final int iy = (int) (h * 0.2f);
                outline.setRect(ix, iy, w - ix, h - iy);
            }
        });
    }

    public void prepareCheckIntersections() {
        final int inset = (PARAMS.PLAYER_SIZE - PARAMS.PLAYER_HIT_SIZE) / 2;
        final int scale = PARAMS.PLAYER_HIT_SIZE;
        final int N = sHull.length / 2;
        for (int i = 0; i < N; i++) {
            corners[i * 2] = scale * sHull[i * 2] + inset;
            corners[i * 2 + 1] = scale * sHull[i * 2 + 1] + inset;
        }
        final Matrix m = getMatrix();
        m.mapPoints(corners);
    }

    public boolean below(int h) {
        final int N = corners.length / 2;
        for (int i = 0; i < N; i++) {
            final int y = (int) corners[i * 2 + 1];
            if (y >= h) return true;
        }
        return false;
    }

    public void step(long t_ms, long dt_ms, float t, float dt) {
        if (getVisibility() != View.VISIBLE) return; // not playing yet

        if (mBoosting) {
            dv = -PARAMS.BOOST_DV;
        } else {
            dv += PARAMS.G;
        }
        if (dv < -PARAMS.MAX_V) dv = -PARAMS.MAX_V;
        else if (dv > PARAMS.MAX_V) dv = PARAMS.MAX_V;

        final float y = getTranslationY() + dv * dt;
        setTranslationY(y < 0 ? 0 : y);
        setRotation(
                90 + lerp(clamp(rlerp(dv, PARAMS.MAX_V, -1 * PARAMS.MAX_V)), 90, -90));

        prepareCheckIntersections();
    }

    public void boost() {
        mBoosting = true;
        dv = -PARAMS.BOOST_DV;

        animate().cancel();
        animate()
                .scaleX(1.25f)
                .scaleY(1.25f)
                .translationZ(PARAMS.PLAYER_Z_BOOST)
                .setDuration(100);
        setScaleX(1.25f);
        setScaleY(1.25f);
    }

    public void unboost() {
        mBoosting = false;

        animate().cancel();
        animate()
                .scaleX(1f)
                .scaleY(1f)
                .translationZ(PARAMS.PLAYER_Z)
                .setDuration(200);
    }
}
