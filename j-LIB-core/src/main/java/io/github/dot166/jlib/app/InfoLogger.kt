package io.github.dot166.jlib.app

import android.os.Bundle
import android.util.Log
import com.android.settingslib.spa.framework.common.LogCategory
import com.android.settingslib.spa.framework.common.LogEvent
import com.android.settingslib.spa.framework.common.SpaLogger

class InfoLogger : SpaLogger {
    override fun message(tag: String, msg: String, category: LogCategory) {
        Log.i("SpaMsg-$category", "[$tag] $msg")
    }

    override fun event(id: String, event: LogEvent, category: LogCategory, extraData: Bundle) {
        val extraMsg = extraData.toString().removeRange(0, 6)
        Log.i("SpaEvent-$category", "[$id] $event $extraMsg")
    }
}