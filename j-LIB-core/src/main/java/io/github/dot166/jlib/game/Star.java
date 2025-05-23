package io.github.dot166.jlib.game;

import static io.github.dot166.jlib.game.GameView.PARAMS;
import static io.github.dot166.jlib.game.GameView.irand;

import android.content.Context;

import io.github.dot166.jlib.R;

public class Star extends Scenery {
    public Star(Context context) {
        super(context);
        setBackgroundResource(R.drawable.l_star);
        w = h = irand(PARAMS.STAR_SIZE_MIN, PARAMS.STAR_SIZE_MAX);
        v = z = 0;
    }
}
