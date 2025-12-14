package io.github.dot166.jlib.app

import android.app.Application
import com.google.android.material.color.DynamicColors

open class jLIBCoreApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
