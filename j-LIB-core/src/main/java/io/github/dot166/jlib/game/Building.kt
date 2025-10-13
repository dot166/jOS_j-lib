package io.github.dot166.jlib.game

import android.content.Context

class Building(context: Context?) : Scenery(context) {
    init {
        w = GameView.irand(GameView.PARAMS.BUILDING_WIDTH_MIN, GameView.PARAMS.BUILDING_WIDTH_MAX)
        h = 0 // will be setup later, along with z

        translationZ = GameView.PARAMS.SCENERY_Z
    }
}