package io.github.dot166.jlib.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import androidx.core.os.UserManagerCompat
import dev.patrickgold.jetpref.datastore.runtime.initAndroid
import io.github.dot166.jlib.app.crashpad.CrashUtility
import io.github.dot166.jlib.app.devtools.Log
import io.github.dot166.jlib.app.devtools.LogTopics
import io.github.dot166.jlib.app.devtools.logError
import io.github.dot166.jlib.app.devtools.logInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


open class CoreApplication: Application() {

    private val mainHandler by lazy { Handler(mainLooper) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        try {
            Log.install(
                isFloggingEnabled = true,
                logTopics = LogTopics.ALL,
                logLevels = Log.LEVEL_ALL,
            )
            CrashUtility.install(this)

            if (!UserManagerCompat.isUserUnlocked(this)) {
                registerReceiver(BootComplete(), IntentFilter(Intent.ACTION_USER_UNLOCKED))
                return
            }

            init()
        } catch (e: Exception) {
            CrashUtility.stageException(e)
            return
        }
    }

    fun init() {
        scope.launch {
            val result = jLibPreferenceStore.initAndroid(
                context = this@CoreApplication,
                datastoreName = JLibPreferenceModel.NAME,
            )
            logInfo { "PREFS $result" }
        }
    }

    private inner class BootComplete : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            if (intent.action == Intent.ACTION_USER_UNLOCKED) {
                try {
                    unregisterReceiver(this)
                } catch (e: Exception) {
                    logError { e.toString() }
                }
                mainHandler.post {
                    init()
                }
            }
        }
    }
}