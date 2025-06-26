package io.github.dot166.jlib.media;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.media3.session.MediaController;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.github.dot166.jlib.R;

public class PlayerCommon {

    public static void initControls(View view, MediaController player) {
        SeekBar seekBarMain = view.findViewById(R.id.seekBar);
        seekBarMain.setMin(0);
        seekBarMain.setMax(1000);
        setProgress(player, seekBarMain);
        seekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player.isCurrentMediaItemLive()) {
                    return;
                }
                long duration = player.getDuration();
                long newposition = (duration * progress) / 1000L;
                String formattedTextString = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(newposition).replaceAll("^00:", "") + "/" + new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(duration).replaceAll("^00:", "");
                ((TextView)view.findViewById(R.id.text)).setText(formattedTextString);
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }
                player.seekTo( (int) newposition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    player.play();
                }
            }
        });

        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekTo(player.getCurrentPosition() - 10000);
                setProgress(player, seekBarMain);
            }
        });

        view.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekTo(player.getCurrentPosition() + 10000);
                setProgress(player, seekBarMain);
            }
        });
    }

    public static void setProgress(MediaController player, SeekBar progress) {
        if (player == null) {
            return;
        }
        long position = player.getCurrentPosition();
        long duration = player.getDuration();
        if (progress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progress.setProgress((int) pos);
            }
        }
    }
}
