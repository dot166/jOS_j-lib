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
            if (findViewById(R.id.now_playing_logo) == null) { // check if view is still there
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
            setProgress(findViewById(R.id.seekBar));
            if (((TextView)findViewById(R.id.now_playing_title)).getText() != mPlayer.getMediaMetadata().title) {
                ((TextView) findViewById(R.id.now_playing_title)).setText(mPlayer.getMediaMetadata().title);
                ((TextView) findViewById(R.id.now_playing_title)).setSelected(true);
            }
            if (((TextView)findViewById(R.id.now_playing_subtitle)).getText() != (mPlayer.isCurrentMediaItemLive() ? mPlayer.getMediaMetadata().station : mPlayer.getMediaMetadata().artist)) {
                ((TextView) findViewById(R.id.now_playing_subtitle)).setText(mPlayer.isCurrentMediaItemLive() ? mPlayer.getMediaMetadata().station : mPlayer.getMediaMetadata().artist);
                ((TextView) findViewById(R.id.now_playing_subtitle)).setSelected(true);
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
            if (mPlayer.getCurrentMediaItem() == null) { // probably no media item loaded, idiot (me) tried to load the url of an item that is not loaded and the entire app went down
                mHandled.post(mTryLoadSavedArtwork);
                return;
            }
            if (findViewById(R.id.now_playing_logo) == null) { // check if view is still there
                mHandled.post(mTryLoadSavedArtwork);
                return;
            }
            Glide.with(MiniPlayer.this)
                    .load(mPlayer.getMediaMetadata().artworkUri)
                    .into(((ImageView) findViewById(R.id.now_playing_logo)));
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
        LayoutInflater.from(context).inflate(R.layout.media_player_view, this, true);
        if (!isInEditMode()) {
            context.startService(new Intent(context, ((jLIBCoreApp) context.getApplicationContext()).getMediaPlayerService().getClass()));
            // begin player service initialisation
            SessionToken sessionToken =
                    new SessionToken(context, new ComponentName(context, ((jLIBCoreApp) context.getApplicationContext()).getMediaPlayerService().getClass()));
            ListenableFuture<MediaController> controllerFuture =
                    new MediaController.Builder(context, sessionToken).buildAsync();
            controllerFuture.addListener(() -> {
                try {
                    mPlayer = controllerFuture.get();
                } catch (Exception e) {
                    ErrorUtils.handle(e, context);
                    context.stopService(new Intent(context, ((jLIBCoreApp) context.getApplicationContext()).getMediaPlayerService().getClass()));
                }
            }, ContextCompat.getMainExecutor(context));
            mHandled.post(mTryLoadSavedArtwork);
            SeekBar seekBarMain = findViewById(R.id.seekBar);
            seekBarMain.setMin(0);
            seekBarMain.setMax(1000);
            setProgress(seekBarMain);
            seekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mPlayer.isCurrentMediaItemLive()) {
                        return;
                    }
                    long duration = mPlayer.getDuration();
                    long newposition = (duration * progress) / 1000L;
                    String formattedTextString = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(newposition).replaceAll("^00:", "") + "/" + new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(duration).replaceAll("^00:", "");
                    ((TextView) findViewById(R.id.text)).setText(formattedTextString);
                    if (!fromUser) {
                        // We're not interested in programmatically generated changes to
                        // the progress bar's position.
                        return;
                    }
                    mPlayer.seekTo((int) newposition);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    } else {
                        mPlayer.play();
                    }
                }
            });

            findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() - 10000);
                    setProgress(seekBarMain);
                }
            });

            findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() + 10000);
                    setProgress(seekBarMain);
                }
            });
            mHandled.post(updateThread);
        } else {
            ((ImageView)findViewById(R.id.now_playing_logo)).setImageResource(R.mipmap.ic_launcher_j);
            SeekBar seekBarMain = findViewById(R.id.seekBar);
            seekBarMain.setMin(0);
            seekBarMain.setMax(100);
            seekBarMain.setProgress(50);
            ((TextView)findViewById(R.id.now_playing_title)).setText(R.string.placeholder);
            ((TextView) findViewById(R.id.now_playing_subtitle)).setText(R.string.preview_mode);
        }
    }

    protected void setProgress(SeekBar progress) {
        if (mPlayer == null) {
            return;
        }
        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (progress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progress.setProgress((int) pos);
            }
        }
    }

    public void onResumeHook() {
        mHandled.post(mTryLoadSavedArtwork);
        mHandled.post(updateThread);
    }
}
