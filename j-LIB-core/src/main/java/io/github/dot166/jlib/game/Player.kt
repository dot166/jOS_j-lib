package io.github.dot166.jlib.game

import android.content.Context
import android.graphics.Outline
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import io.github.dot166.jlib.R
import java.util.Random

class Player(context: Context) : AppCompatImageView(context), GameObject {
    @JvmField
    var dv: Float = 0f

    private var mBoosting = false

    private val sHull = floatArrayOf(
        0.3f, 0f,  // left antenna
        0.7f, 0f,  // right antenna
        0.92f, 0.33f,  // off the right shoulder of Orion
        0.92f, 0.75f,  // right hand (our right, not his right)
        0.6f, 1f,  // right foot
        0.4f, 1f,  // left foot BLUE!
        0.08f, 0.75f,  // sinistram
        0.08f, 0.33f,  // cold shoulder
    )
    @JvmField
    val corners: FloatArray = FloatArray(sHull.size)

    private val randomTint: Int
        get() {
            val colours = intArrayOf(-0x980000, -0xff9800, -0xffff98)
            val rand = Random()
            return colours[rand.nextInt(3)]
        }

    init {
        setBackgroundResource(R.drawable.l_android) // TODO: Create a new character drawable
        background.setTintMode(PorterDuff.Mode.SRC_ATOP)
        background.setTint(this.randomTint)
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val w = view.width
                val h = view.height
                val ix = (w * 0.3f).toInt()
                val iy = (h * 0.2f).toInt()
                outline.setRect(ix, iy, w - ix, h - iy)
            }
        }
    }

    fun prepareCheckIntersections() {
        val inset = (GameView.PARAMS.PLAYER_SIZE - GameView.PARAMS.PLAYER_HIT_SIZE) / 2
        val scale = GameView.PARAMS.PLAYER_HIT_SIZE
        val n = sHull.size / 2
        for (i in 0..<n) {
            corners[i * 2] = scale * sHull[i * 2] + inset
            corners[i * 2 + 1] = scale * sHull[i * 2 + 1] + inset
        }
        val m = getMatrix()
        m.mapPoints(corners)
    }

    fun below(h: Int): Boolean {
        val n = corners.size / 2
        for (i in 0..<n) {
            val y = corners[i * 2 + 1].toInt()
            if (y >= h) return true
        }
        return false
    }

    override fun step(tMs: Long, dtMs: Long, t: Float, dt: Float) {
        if (visibility != VISIBLE) return  // not playing yet


        if (mBoosting) {
            dv = -GameView.PARAMS.BOOST_DV.toFloat()
        } else {
            dv += GameView.PARAMS.G.toFloat()
        }
        if (dv < -GameView.PARAMS.MAX_V) dv = -GameView.PARAMS.MAX_V.toFloat()
        else if (dv > GameView.PARAMS.MAX_V) dv = GameView.PARAMS.MAX_V.toFloat()

        val y = translationY + dv * dt
        translationY = if (y < 0) 0f else y
        rotation = 90 + GameView.lerp(
            GameView.clamp(
                GameView.rlerp(
                    dv,
                    GameView.PARAMS.MAX_V.toFloat(),
                    (-1 * GameView.PARAMS.MAX_V).toFloat()
                )
            ), 90f, -90f
        )

        prepareCheckIntersections()
    }

    fun boost() {
        mBoosting = true
        dv = -GameView.PARAMS.BOOST_DV.toFloat()

        animate().cancel()
        animate()
            .scaleX(1.25f)
            .scaleY(1.25f)
            .translationZ(GameView.PARAMS.PLAYER_Z_BOOST)
            .setDuration(100)
        scaleX = 1.25f
        scaleY = 1.25f
    }

    fun unboost() {
        mBoosting = false

        animate().cancel()
        animate()
            .scaleX(1f)
            .scaleY(1f)
            .translationZ(GameView.PARAMS.PLAYER_Z)
            .setDuration(200)
    }
}
