package io.github.dot166.jlib.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.CommandButton;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionResult;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.MediaPlayerActivity;
import io.github.dot166.jlib.media.jMediaNotificationProvider;

public class MediaPlayerService extends MediaSessionService {
    public MediaSession mediaSession = null;
    boolean mIsWorking = false;
    private static final SessionCommand KILL_SERVICE =
            new SessionCommand("ACTION_KILL", Bundle.EMPTY);

    private static MediaPlayerService instance;

    public MediaPlayerService() {
        instance = this;
    }

    public static MediaPlayerService getInstance() {
        return instance;
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if (isPlaying) {
                    if (!mIsWorking) {
                        mIsWorking = true;
                        if (player.isCurrentMediaItemLive()) {
                            player.seekToDefaultPosition();
                            player.play();
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsWorking = false;
                        }
                    }, 1000);
                }
            }
        });
        CommandButton exitButton =
                new CommandButton.Builder(CommandButton.ICON_STOP)
                        .setDisplayName(getString(R.string.close_media_player))
                        .setSessionCommand(KILL_SERVICE)
                        .build();
        mediaSession = new MediaSession.Builder(this, player)
                .setCallback(new jLibCallback())
                .setMediaButtonPreferences(ImmutableList.of(exitButton))
                .build();
        setMediaNotificationProvider(new jMediaNotificationProvider(this));
        mediaSession.setSessionActivity(PendingIntent.getActivity(this, 1, new Intent(this, MediaPlayerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
    }

    @Override
    public void onDestroy() {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }
    @Nullable
    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    private static class jLibCallback implements MediaSession.Callback {
        @NonNull
        @OptIn(markerClass = UnstableApi.class)
        @Override
        public MediaSession.ConnectionResult onConnect(
                @NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller) {
            // Set available player and session commands.
            return new MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                    .setAvailableSessionCommands(
                            MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                                    .add(KILL_SERVICE)
                                    .build())
                    .build();
        }

        @NonNull
        public ListenableFuture<SessionResult> onCustomCommand(
                @NonNull MediaSession session,
                @NonNull MediaSession.ControllerInfo controller,
                SessionCommand customCommand,
                @NonNull Bundle args) {
            if (customCommand.customAction.equals(KILL_SERVICE.customAction)) {
                session.getPlayer().release();
                session.release();
                return Futures.immediateFuture(new SessionResult(SessionResult.RESULT_SUCCESS));
            }
            return MediaSession.Callback.super.onCustomCommand(
                    session, controller, customCommand, args);
        }
    }
}
