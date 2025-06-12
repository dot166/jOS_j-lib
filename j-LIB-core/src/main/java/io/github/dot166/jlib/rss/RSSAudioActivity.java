package io.github.dot166.jlib.rss;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.app.jWebActivity;
import io.github.dot166.jlib.utils.ErrorUtils;

public class RSSAudioActivity extends jActivity {

    MediaPlayer mPlayer;
    SeekBar mProgress;

    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (mPlayer.isPlaying()) {
                mProgress.postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = Objects.requireNonNull(getIntent().getExtras()).getString("uri");
        String drawUrl = Objects.requireNonNull(getIntent().getExtras()).getString("drawableUrl");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);
        if (drawUrl != null && !drawUrl.isEmpty()) {
            Glide.with(RSSAudioActivity.this)
                    .load(drawUrl)
                    .into(((ImageView) findViewById(R.id.imageView)));
        }
        mPlayer = new MediaPlayer();
        mProgress = findViewById(R.id.seekBar);
        try {
            mPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) // no idea what the content type would be so use music for now
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            mPlayer.setDataSource(url);
            mPlayer.prepare();
            mPlayer.start();
            mProgress.post(mShowProgress);
        } catch (IOException e) {
            ErrorUtils.handle(e, this);
            finish();
        }
        mProgress.setMin(0);
        mProgress.setMax(1000);
        setProgress();
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long duration = mPlayer.getDuration();
                long newposition = (duration * progress) / 1000L;
                mPlayer.seekTo( (int) newposition);
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
                    mPlayer.start();
                }
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition() - 10000);
                setProgress();
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition() + 10000);
                setProgress();
            }
        });
    }

    public static void playAudioFromFeed(String url, Context context, String drawUrl) {
        Intent i = new Intent(context, RSSAudioActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", drawUrl);
        context.startActivity(i);
    }

    private int setProgress() {
        if (mPlayer == null) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress( (int) pos);
            }
        }

        return position;
    }
}
