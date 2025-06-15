package io.github.dot166.jlib.rss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.github.dot166.jlib.app.MediaPlayerActivity;

public class RSSAudioActivity extends MediaPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void playAudioFromFeed(String url, Context context, String drawUrl, String title) {
        Intent i = new Intent(context, RSSAudioActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", drawUrl);
        i.putExtra("title", title);
        context.startActivity(i);
    }
}
