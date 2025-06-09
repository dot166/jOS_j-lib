package io.github.dot166.jlib;

import android.os.Bundle;

import io.github.dot166.jlib.app.jWebActivity;
import io.github.dot166.jlib.utils.VersionUtils;

public class LIBChangelogActivity extends jWebActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        useWebPageTitleAsActivityTitle = false;
        setUri("https://github.com/dot166/jOS_j-lib/commits/v" + VersionUtils.getLibVersion(this));
        configureWebView(true, true);
        super.onCreate(savedInstanceState);
    }
}
