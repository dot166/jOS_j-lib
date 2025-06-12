package io.github.dot166.jlib.rss;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.utils.ErrorUtils;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class RSSAudioActivity extends jActivity {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = Objects.requireNonNull(getIntent().getExtras()).getString("uri");
        String drawUrl = Objects.requireNonNull(getIntent().getExtras()).getString("drawableUrl");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);

        playerView = findViewById(R.id.player_view);

        try {
            exoPlayer = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(exoPlayer);

            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(url)
                    .setMediaMetadata(
                            MediaItem.fromUri(url).mediaMetadata.buildUpon()
                                    .setArtworkUri(Uri.parse(drawUrl))
                                    .build()
                    )
                    .build();
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();

        } catch (Exception e) {
            ErrorUtils.handle(e, this);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        super.onDestroy();
    }

    public static void playAudioFromFeed(String url, Context context, String drawUrl) {
        Intent i = new Intent(context, RSSAudioActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", drawUrl);
        context.startActivity(i);
    }
}
