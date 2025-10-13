package io.github.dot166.jlib.media

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList

@UnstableApi
class jMediaNotificationProvider @JvmOverloads constructor(
    context: Context,
    notificationIdProvider: NotificationIdProvider = NotificationIdProvider { session: MediaSession? -> 1001 },
    channelId: String = "default_channel_id",
    channelNameResourceId: Int = DEFAULT_CHANNEL_NAME_RESOURCE_ID
) : DefaultMediaNotificationProvider(
    context,
    notificationIdProvider,
    channelId,
    channelNameResourceId
) {
    var mPlayer: Player? = null
    override fun getNotificationContentText(metadata: MediaMetadata): CharSequence? {
        if (mPlayer!!.isCurrentMediaItemLive) {
            mPlayer = null // remove reference, not meant to have it here anyway
            return metadata.station
        } else {
            mPlayer = null // remove reference, not meant to have it here anyway
            return super.getNotificationContentText(metadata)
        }
    }

    override fun addNotificationActions(
        mediaSession: MediaSession,
        mediaButtons: ImmutableList<CommandButton>,
        builder: NotificationCompat.Builder,
        actionFactory: MediaNotification.ActionFactory
    ): IntArray {
        mPlayer = mediaSession.player
        return super.addNotificationActions(mediaSession, mediaButtons, builder, actionFactory)
    }
}
