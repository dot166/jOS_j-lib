package io.github.dot166.jlib.game

import android.content.Context
import io.github.dot166.jlib.R

class Star(context: Context?) : Scenery(context) {
    init {
        setBackgroundResource(R.drawable.l_star)
        h = GameView.irand(GameView.PARAMS.STAR_SIZE_MIN, GameView.PARAMS.STAR_SIZE_MAX)
        w = h
        z = 0f
        v = z
    }
}
