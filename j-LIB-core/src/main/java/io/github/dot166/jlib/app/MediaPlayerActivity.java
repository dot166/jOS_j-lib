package io.github.dot166.jlib.app;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.media.PlayerCommon;
import io.github.dot166.jlib.service.MediaPlayerService;
import io.github.dot166.jlib.utils.ErrorUtils;

public class MediaPlayerActivity  extends jActivity {

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
            ProgressBar progress = findViewById(R.id.progress);
            if (!mPlayer.isConnected()) {
                // uh oh, service died
                progress.setVisibility(VISIBLE);
                mHandled.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recreate(); // quick, rebuild UI to start service again.
                    }
                }, 2000);
                return; // just in case
            }
            if (mPlayer.getPlaybackState() == Player.STATE_BUFFERING) {
                progress.setVisibility(VISIBLE);
            } else {
                progress.setVisibility(GONE);
            }
            if (mPlayer.isCurrentMediaItemLive()) {
                findViewById(R.id.button5).setVisibility(GONE);
                findViewById(R.id.button7).setVisibility(GONE);
                findViewById(R.id.seekBar_layout).setVisibility(GONE);
            } else {
                findViewById(R.id.button5).setVisibility(VISIBLE);
                findViewById(R.id.button7).setVisibility(VISIBLE);
                findViewById(R.id.seekBar_layout).setVisibility(VISIBLE);
            }
            findViewById(R.id.button6).setActivated(mPlayer.isPlaying());
            PlayerCommon.setProgress(mPlayer, findViewById(R.id.seekBar));
            ((TextView)findViewById(R.id.now_playing_title)).setText(mPlayer.getMediaMetadata().title);
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
            Glide.with(MediaPlayerActivity.this)
                    .load(mPlayer.getMediaMetadata().artworkUri)
                    .into(((ImageView) findViewById(R.id.imageView)));
        }
    };

    private final Runnable mTryLoadSavedTitle = new Runnable() {
        @Override
        public void run() {
            if (mPlayer == null) {
                mHandled.post(mTryLoadSavedTitle);
                return;
            }
            if (mPlayer.getMediaMetadata().station != null) {
                setTitle(mPlayer.getMediaMetadata().station);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url;
        String drawUrl;
        String title;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("uri");
            drawUrl = getIntent().getExtras().getString("drawableUrl");
            title = getIntent().getExtras().getString("title");
        } else {
            url = "";
            drawUrl = "";
            title = "";
        }
        super.onCreate(savedInstanceState);
        forceNotificationPermission();
        setContentView(R.layout.media_player);
        if (title != null && !title.isEmpty()) {
            setTitle(title);
        } else {
            mHandled.post(mTryLoadSavedTitle);
        }
        setSupportActionBar(findViewById(R.id.actionbar));
        startService(new Intent(this, ((jLIBCoreApp)getApplicationContext()).getMediaPlayerService().getClass()));
        SessionToken sessionToken =
                new SessionToken(this, new ComponentName(this, ((jLIBCoreApp)getApplicationContext()).getMediaPlayerService().getClass()));
        ListenableFuture<MediaController> controllerFuture =
                new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                mPlayer = controllerFuture.get();
                createPlayer(url, drawUrl);
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
                stopService(new Intent(this, ((jLIBCoreApp)getApplicationContext()).getMediaPlayerService().getClass()));
                finish();
            }
        }, ContextCompat.getMainExecutor(this));
        if (drawUrl != null && !drawUrl.isEmpty()) {
            Glide.with(this)
                    .load(drawUrl)
                    .into(((ImageView) findViewById(R.id.imageView)));
        } else {
            mHandled.post(mTryLoadSavedArtwork);
        }
        PlayerCommon.initControls(findViewById(android.R.id.content), mPlayer);
        mHandled.post(updateThread);
    }

    protected void createPlayer(String url, String drawUrl) {
        try {
            String urltest;
            if (Objects.equals(url, "")) {
                urltest = ""; // force override because if this condition is true then the service is running and audio is loaded
            } else if (mPlayer.getCurrentMediaItem() != null && mPlayer.getCurrentMediaItem().localConfiguration != null) {
                urltest = mPlayer.getCurrentMediaItem().localConfiguration.uri.toString();
            } else {
                urltest = ""; // force override because if the other conditions are false, most likely the service is not running
            }
            if (!urltest.equals(url)) {
                MediaItem.Builder mIBuilder = new MediaItem.Builder();
                mIBuilder.setUri(url);
                MediaMetadata.Builder metadata = MediaItem.fromUri(url).mediaMetadata.buildUpon();
                if (drawUrl != null && !drawUrl.isEmpty()) {
                    metadata.setArtworkUri(Uri.parse(drawUrl));
                }
                addExtraMetadata(mIBuilder, metadata);
                mIBuilder.setMediaMetadata(metadata.build());
                MediaItem mediaItem = mIBuilder.build();
                mPlayer.setMediaItem(mediaItem);
                mPlayer.prepare();
                mPlayer.play();
            }
        } catch (Exception e) {
            ErrorUtils.handle(e, this);
            finish();
        }
    }

    public static void playAudio(String url, Context context, String drawUrl, String title) {
        Intent i = new Intent(context, MediaPlayerActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", drawUrl);
        i.putExtra("title", title);
        context.startActivity(i);
    }

    protected void addExtraMetadata(MediaItem.Builder mIdBuilder, MediaMetadata.Builder metadata) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandled.post(updateThread);
    }
}
