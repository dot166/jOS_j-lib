package io.github.dot166.jLib.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;

public class jWebActivity extends jActivity {

    String uri;

    protected void configure(String uri)
    {
        this.uri = uri;
        super.configure(null, 0, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri webpage = Uri.parse(uri);
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .build();
        intent.intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.launchUrl(this, webpage);
        finish();
    }
}
