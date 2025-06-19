package io.github.dot166.jlib.media;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.CommandButton;
import androidx.media3.session.DefaultMediaNotificationProvider;
import androidx.media3.session.MediaNotification;
import androidx.media3.session.MediaSession;

import com.google.common.collect.ImmutableList;

@UnstableApi
public class jMediaNotificationProvider extends DefaultMediaNotificationProvider {
    Player mPlayer;
    public jMediaNotificationProvider(Context context) {
        this(context, (session) -> 1001, "default_channel_id", DEFAULT_CHANNEL_NAME_RESOURCE_ID);
    }

    public jMediaNotificationProvider(Context context, NotificationIdProvider notificationIdProvider, String channelId, int channelNameResourceId) {
        super(context, notificationIdProvider, channelId, channelNameResourceId);
    }

    @Override
    @Nullable
    protected CharSequence getNotificationContentText(@NonNull MediaMetadata metadata) {
        if (mPlayer.isCurrentMediaItemLive()) {
            mPlayer = null; // remove reference, not meant to have it here anyway
            return metadata.station;
        } else {
            mPlayer = null; // remove reference, not meant to have it here anyway
            return super.getNotificationContentText(metadata);
        }
    }

    @NonNull
    @Override
    protected int[] addNotificationActions(@NonNull MediaSession mediaSession, @NonNull ImmutableList<CommandButton> mediaButtons, @NonNull NotificationCompat.Builder builder, @NonNull MediaNotification.ActionFactory actionFactory) {
        mPlayer = mediaSession.getPlayer();
        return super.addNotificationActions(mediaSession, mediaButtons, builder, actionFactory);
    }
}
