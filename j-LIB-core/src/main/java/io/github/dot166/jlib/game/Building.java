package io.github.dot166.jlib.game;

import static io.github.dot166.jlib.game.GameView.PARAMS;
import static io.github.dot166.jlib.game.GameView.irand;

import android.content.Context;

public class Building extends Scenery {
    public Building(Context context) {
        super(context);

        w = irand(PARAMS.BUILDING_WIDTH_MIN, PARAMS.BUILDING_WIDTH_MAX);
        h = 0; // will be setup later, along with z

        setTranslationZ(PARAMS.SCENERY_Z);
    }
}