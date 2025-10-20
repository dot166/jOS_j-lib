package io.github.dot166.jlib.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
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
            if (findViewById(R.id.now_playing_logo) == null) { // check if view is still there
                mHandled.post(mTryLoadSavedArtwork);
                return;
            }
            if (mPlayer == null) {
                Drawable[] dr = new Drawable[0];
                ((ImageView) findViewById(R.id.now_playing_logo)).setImageDrawable(new LayerDrawable(dr));
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
            Slider seekBarMain = findViewById(R.id.seekBar);
            seekBarMain.setValueFrom(0);
            seekBarMain.setValueTo(1000);
            setProgress(seekBarMain);
            seekBarMain.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float progress, boolean fromUser) {
                    if (mPlayer.isCurrentMediaItemLive()) {
                        return;
                    }
                    long duration = mPlayer.getDuration();
                    long newposition = (long) ((duration * progress) / 1000L);
                    String formattedTextString = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(newposition) + "/" + new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(duration);
                    ((TextView) findViewById(R.id.text)).setText(formattedTextString);
                    if (!fromUser) {
                        // We're not interested in programmatically generated changes to
                        // the progress bar's position.
                        return;
                    }
                    mPlayer.seekTo((int) newposition);
                }
            });

            findViewById(R.id.button6).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    } else {
                        mPlayer.play();
                    }
                }
            });

            findViewById(R.id.button5).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() - 10000);
                    setProgress(seekBarMain);
                }
            });

            findViewById(R.id.button7).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition() + 10000);
                    setProgress(seekBarMain);
                }
            });
            mHandled.post(updateThread);
        } else {
            ((ImageView)findViewById(R.id.now_playing_logo)).setImageResource(R.mipmap.ic_app_background);
            Slider seekBarMain = findViewById(R.id.seekBar);
            seekBarMain.setValueFrom(0);
            seekBarMain.setValueTo(100);
            seekBarMain.setValue(50);
            ((TextView)findViewById(R.id.now_playing_title)).setText(R.string.placeholder);
            ((TextView) findViewById(R.id.now_playing_subtitle)).setText(R.string.preview_mode);
        }
    }

    protected void setProgress(Slider progress) {
        if (mPlayer == null) {
            return;
        }
        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (progress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progress.setValue((int) pos);
            }
        }
    }

    public void onResumeHook() {
        mHandled.post(mTryLoadSavedArtwork);
        mHandled.post(updateThread);
    }
}
