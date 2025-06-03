package io.github.dot166.jlib.web;

import android.content.Context;
import android.content.Intent;

import io.github.dot166.jlib.app.jWebActivity;

public class jWebIntent {

    public Intent intent;
    private Context mContext;
    String url;

    public jWebIntent(Context context) {
        intent = new Intent(context, jWebActivity.class);
        mContext = context;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void configureWebView(boolean js, boolean DOM) {
        intent.putExtra("js", js);
        intent.putExtra("DOM", DOM);
        intent.putExtra("useWebView", true);
    }

    public void launch() {
        if (url == null) {
            throw new IllegalArgumentException("url is not set!");
        }
        intent.putExtra("uri", url);
        mContext.startActivity(intent);
    }
}
