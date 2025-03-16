package io.github.dot166.jlib.app;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class jLIBCoreApp extends Application {
    public static final String TAG = "jLIB";

    public void onCreate() {
        super.onCreate();
        jLibFeatureFlags.init_values(this);
    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity currentActivity){
        this.mCurrentActivity = currentActivity;
        Log.i(TAG, String.valueOf(mCurrentActivity));
        Log.i(TAG, String.valueOf(currentActivity));
    }
}
