package io.github.dot166.jlib.app;

import android.app.Application;

public class jLIBCoreApp extends Application {
    public static final String TAG = "jLIB";
    private static jLIBCoreApp instance;

    public jLIBCoreApp() {
        instance = this;
    }

    public static jLIBCoreApp getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
    }
}
