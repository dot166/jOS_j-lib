package io.github.dot166.jlib.game;

import static io.github.dot166.jlib.game.GameView.PARAMS;
import static io.github.dot166.jlib.game.GameView.frand;
import static io.github.dot166.jlib.game.GameView.irand;

import android.content.Context;

import io.github.dot166.jlib.R;

public class Cloud extends Scenery {
    public Cloud(Context context) {
        super(context);
        setBackgroundResource(frand() < 0.01f ? R.drawable.l_cloud_off : R.drawable.l_cloud);
        getBackground().setAlpha(0x40);
        w = h = irand(PARAMS.CLOUD_SIZE_MIN, PARAMS.CLOUD_SIZE_MAX);
        z = 0;
        v = frand(0.15f, 0.5f);
    }
}
