package io.github.dot166.jlib.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jLIBCoreApp;
import io.github.dot166.jlib.media.PlayerCommon;
import io.github.dot166.jlib.utils.ErrorUtils;

public class MiniPlayer extends FrameLayout {

    MediaController mPlayer;

    Handler mHandled = new Handler();

    private final Runnable updateThread = new Runnable() {
        @OptIn(markerClass = UnstableApi.class)
        @Override
        public void run() {
            if (mPlayer == null) {
                mHandled.post(updateThread);
                return;
            }
            if (mPlayer.isCurrentMediaItemLive()) {
                findViewById(R.id.button5).setVisibility(GONE);
                findViewById(R.id.button7).setVisibility(GONE);
                findViewById(R.id.seekBar).setVisibility(GONE);
            } else {
                findViewById(R.id.button5).setVisibility(VISIBLE);
                findViewById(R.id.button7).setVisibility(VISIBLE);
                findViewById(R.id.seekBar).setVisibility(VISIBLE);
            }
            findViewById(R.id.button6).setActivated(mPlayer.isPlaying());
            PlayerCommon.setProgress(mPlayer, findViewById(R.id.seekBar));
            ((TextView)findViewById(R.id.now_playing_title)).setText(mPlayer.getMediaMetadata().title);
            if (mPlayer.isCurrentMediaItemLive()) {
                ((TextView) findViewById(R.id.now_playing_subtitle)).setText(mPlayer.getMediaMetadata().station);
            } else {
                ((TextView) findViewById(R.id.now_playing_subtitle)).setText(mPlayer.getMediaMetadata().artist);
            }
            mHandled.post(updateThread);
        }
    };

    private final Runnable mTryLoadSavedArtwork = new Runnable() {
        @Override
        public void run() { // this is a separate task to prevent the artwork view from dying (being overridden multiple times)
            if (mPlayer == null) {
                mHandled.post(mTryLoadSavedArtwork);
                return;
            }
            Glide.with(MiniPlayer.this)
                    .load(mPlayer.getMediaMetadata().artworkUri)
                    .into(((ImageView) findViewById(R.id.imageView)));
        }
    };

    public MiniPlayer(Context context) {
        this(context, null);
    }

    public MiniPlayer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiniPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View layout = LayoutInflater.from(context).inflate(R.layout.media_player_view, this, true);
        context.startService(new Intent(context, ((jLIBCoreApp)context.getApplicationContext()).getMediaPlayerService().getClass()));
        // begin player service initialisation
        SessionToken sessionToken =
                new SessionToken(context, new ComponentName(context, ((jLIBCoreApp)context.getApplicationContext()).getMediaPlayerService().getClass()));
        ListenableFuture<MediaController> controllerFuture =
                new MediaController.Builder(context, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                mPlayer = controllerFuture.get();
            } catch (Exception e) {
                ErrorUtils.handle(e, context);
                context.stopService(new Intent(context, ((jLIBCoreApp)context.getApplicationContext()).getMediaPlayerService().getClass()));
            }
        }, ContextCompat.getMainExecutor(context));
        mHandled.post(mTryLoadSavedArtwork);
        PlayerCommon.initControls(this, mPlayer);
        mHandled.post(updateThread);
    }
}
