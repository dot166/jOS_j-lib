package jOS.Core;

import android.os.Bundle;

public class LIBChangelogActivity extends jWebActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configure("https://github.com/dot166/jOS_j-lib/commits/v" + BuildConfig.LIBVersion);
        super.onCreate(savedInstanceState);
    }
}
