package jOS.Core

import android.app.Activity
import android.app.Application
import android.util.Log

class jSDKCoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    private var mCurrentActivity: Activity? = null
    var currentActivity: Activity?
        get() = mCurrentActivity
        set(currentActivity) {
            this.mCurrentActivity = currentActivity
            Log.i(TAG, mCurrentActivity.toString())
            Log.i(TAG, currentActivity.toString())
        }

    companion object {
        const val TAG: String = "jSDK"
    }
}
