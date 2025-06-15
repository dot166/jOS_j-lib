package io.github.dot166.jlib.app;

import android.content.Context;
import android.content.Intent;

public class RadioActivity extends MediaPlayerActivity {

    @Override
    protected boolean isRadio() {
        return true;
    }

    public static void playStation(String url, Context context, String stationLogoImageUrl, String title) {
        Intent i = new Intent(context, RadioActivity.class);
        i.putExtra("uri", url);
        i.putExtra("drawableUrl", stationLogoImageUrl);
        i.putExtra("title", title);
        context.startActivity(i);
    }
}
