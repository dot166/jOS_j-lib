package io.github.dot166.jlib.game

import android.content.Context
import io.github.dot166.jlib.R

class Cloud(context: Context?) : Scenery(context) {
    init {
        setBackgroundResource(if (GameView.frand() < 0.01f) R.drawable.l_cloud_off else R.drawable.l_cloud)
        background.alpha = 0x40
        h = GameView.irand(GameView.PARAMS.CLOUD_SIZE_MIN, GameView.PARAMS.CLOUD_SIZE_MAX)
        w = h
        z = 0f
        v = GameView.frand(0.15f, 0.5f)
    }
}
