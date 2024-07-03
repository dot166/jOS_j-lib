package jOS.Core;

import android.os.Bundle;

public class SDKChangelogActivity extends jWebActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configure("https://github.com/dot166/jOS_j-sdk/commits/v" + BuildConfig.SDKVersion, true, false, true, false, true);
        super.onCreate(savedInstanceState);
    }
}
