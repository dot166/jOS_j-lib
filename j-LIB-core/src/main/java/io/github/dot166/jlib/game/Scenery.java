package io.github.dot166.jlib.game;

import static io.github.dot166.jlib.game.GameView.PARAMS;

import android.content.Context;
import android.widget.FrameLayout;

public class Scenery extends FrameLayout implements GameObject {
    public float z;
    public float v;
    public int h, w;

    public Scenery(Context context) {
        super(context);
    }

    @Override
    public void step(long t_ms, long dt_ms, float t, float dt) {
        setTranslationX(getTranslationX() - PARAMS.TRANSLATION_PER_SEC * dt * v);
    }
}
