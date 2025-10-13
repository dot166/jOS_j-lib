package io.github.dot166.jlib.game

interface GameObject {
    fun step(tMs: Long, dtMs: Long, t: Float, dt: Float)
}
