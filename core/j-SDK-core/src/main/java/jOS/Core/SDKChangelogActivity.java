package jOS.Core;

import android.os.Bundle;

public class SDKChangelogActivity extends jWebActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configure("https://dot166.github.io/jOS/commlib/Changelog/", true, false, true, false, true);
        super.onCreate(savedInstanceState);
    }
}
