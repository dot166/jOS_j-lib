package io.github.dot166.jlib.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.View

@SuppressLint("ViewConstructor")
open class Obstacle(context: Context?, h: Float) : View(context), GameObject {
    @JvmField
    var h: Float

    @JvmField
    val hitRect: Rect = Rect()

    init {
        setBackgroundColor(-0x10000)
        this.h = h
    }

    fun intersects(p: Player): Boolean {
        val n = p.corners.size / 2
        for (i in 0..<n) {
            val x = p.corners[i * 2].toInt()
            val y = p.corners[i * 2 + 1].toInt()
            if (hitRect.contains(x, y)) return true
        }
        return false
    }

    fun cleared(p: Player): Boolean {
        val n = p.corners.size / 2
        for (i in 0..<n) {
            val x = p.corners[i * 2].toInt()
            if (hitRect.right >= x) return false
        }
        return true
    }

    override fun step(tMs: Long, dtMs: Long, t: Float, dt: Float) {
        translationX = translationX - GameView.PARAMS.TRANSLATION_PER_SEC * dt
        getHitRect(hitRect)
    }
}
