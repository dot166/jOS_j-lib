package io.github.dot166.jlib.compose.model

import android.content.Context
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player

interface MediaViewModel {
    val isRss: Boolean
    var controller: Player?
    var currentPosition: Long
    var duration: Long
    var isPlaying: Boolean
    var shuffle: Boolean
    var repeat: Int
    var mediaMetadata: MediaMetadata
    fun toggleRepeatMode()
    fun toggleShuffleMode()
    suspend fun pollPosition()
    fun connectController(context: Context)
}