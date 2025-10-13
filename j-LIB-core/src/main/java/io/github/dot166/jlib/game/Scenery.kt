package io.github.dot166.jlib.game

import android.content.Context
import android.widget.FrameLayout

open class Scenery(context: Context?) : FrameLayout(context!!), GameObject {
    @JvmField
    var z: Float = 0f
    @JvmField
    var v: Float = 0f
    @JvmField
    var h: Int = 0
    @JvmField
    var w: Int = 0

    override fun step(tMs: Long, dtMs: Long, t: Float, dt: Float) {
        translationX = translationX - GameView.PARAMS.TRANSLATION_PER_SEC * dt * v
    }
}
