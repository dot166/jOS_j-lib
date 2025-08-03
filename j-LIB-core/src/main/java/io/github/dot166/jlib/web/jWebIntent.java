package io.github.dot166.jlib.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import io.github.dot166.jlib.app.jWebActivity;

/**
 * this is just a deprecated alias to {@link androidx.browser.customtabs.CustomTabsIntent} now
 * @deprecated please use {@link androidx.browser.customtabs.CustomTabsIntent} instead
 */
@Deprecated(forRemoval = true, since = "4.3.0")
public class jWebIntent {

    @Deprecated(forRemoval = true, since = "4.3.0")
    public CustomTabsIntent intent;
    @Deprecated(forRemoval = true, since = "4.3.0")
    private Context mContext;
    @Deprecated(forRemoval = true, since = "4.3.0")
    Uri url;

    @Deprecated(forRemoval = true, since = "4.3.0")
    public jWebIntent(Context context) {
        intent = new CustomTabsIntent.Builder().build();
        mContext = context;
    }

    @Deprecated(forRemoval = true, since = "4.3.0")
    public void setUrl(String url) {
        this.url = Uri.parse(url);
    }

    @Deprecated(forRemoval = true, since = "4.3.0")
    public void configureWebView(boolean js, boolean DOM) {
    }

    @Deprecated(forRemoval = true, since = "4.3.0")
    public void launch() {
        if (url == null) {
            throw new IllegalArgumentException("url is not set!");
        }
        intent.launchUrl(mContext, url);
    }

    @Deprecated(forRemoval = true, since = "4.3.0")
    public Intent getIntent() {
        if (url == null) {
            throw new IllegalArgumentException("url is not set!");
        }
        intent.intent.setData(url);
        return intent.intent;
    }
}
