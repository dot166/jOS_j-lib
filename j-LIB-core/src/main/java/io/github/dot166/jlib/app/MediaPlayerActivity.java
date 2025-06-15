package io.github.dot166.jlib.app;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static io.github.dot166.jlib.utils.TimeUtils.convertMillisToHMS;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.utils.ErrorUtils;

public class MediaPlayerActivity  extends jActivity {

    MediaPlayer mPlayer;
    SeekBar mProgress;

    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            if (hideSeekBar()) {
                ((TextView)findViewById(R.id.text)).setText(Calendar.getInstance().getTime().toString());
                return;
            }
            int pos = setProgress();
            if (mPlayer.isPlaying()) {
                mProgress.postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    protected boolean hideSeekBar() {
        return isRadio();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = Objects.requireNonNull(getIntent().getExtras()).getString("uri");
        String drawUrl = Objects.requireNonNull(getIntent().getExtras()).getString("drawableUrl");
        String title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);
        if (title != null && !title.isEmpty()) {
            setTitle(title);
        }
        setSupportActionBar(findViewById(R.id.actionbar));
        if (drawUrl != null && !drawUrl.isEmpty()) {
            Glide.with(this)
                    .load(drawUrl)
                    .into(((ImageView) findViewById(R.id.imageView)));
        }
        mProgress = findViewById(R.id.seekBar);
        createPlayer(url);
        findViewById(R.id.button6).setActivated(false);
        mProgress.setMin(0);
        mProgress.setMax(1000);
        setProgress();
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (hideSeekBar()) {
                    return;
                }
                long duration = mPlayer.getDuration();
                long newposition = (duration * progress) / 1000L;
                String formattedTextString = convertMillisToHMS(newposition) + "/" + convertMillisToHMS(duration);
                ((TextView)findViewById(R.id.text)).setText(formattedTextString);
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }
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
                    if (isRadio()) {
                        mPlayer.stop();
                    } else {
                        mPlayer.pause();
                    }
                    findViewById(R.id.button6).setActivated(false);
                } else {
                    if (isRadio()) {
                        // yes we rebuild the player when it is a radio station being played
                        recreatePlayer(url);
                    } else {
                        mPlayer.start();
                    }
                    findViewById(R.id.button6).setActivated(true);
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

        if (hideSeekBar()) {
            findViewById(R.id.button5).setVisibility(GONE);
            findViewById(R.id.button7).setVisibility(GONE);
            findViewById(R.id.seekBar).setVisibility(INVISIBLE);
        }
    }

    protected boolean isRadio() {
        return false;
    }

    protected int setProgress() {
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

    protected void recreatePlayer(String url) {
        mPlayer.release();
        createPlayer(url);
    }

    protected void createPlayer(String url) {
        try {
            mPlayer = new MediaPlayer();
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
    }
}
