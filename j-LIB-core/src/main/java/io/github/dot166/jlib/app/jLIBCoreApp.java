package io.github.dot166.jlib.app;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class jLIBCoreApp extends Application {
    public static final String TAG = "jLIB";

    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
