package io.github.dot166.jlib.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import androidx.core.os.UserManagerCompat
import com.android.settingslib.datastore.BackupRestoreStorageManager
import com.android.settingslib.spa.framework.common.SpaEnvironmentFactory
import dev.patrickgold.jetpref.datastore.runtime.initAndroid
import io.github.dot166.jlib.app.crashpad.CrashUtility
import io.github.dot166.jlib.app.devtools.Log
import io.github.dot166.jlib.app.devtools.LogTopics
import io.github.dot166.jlib.app.devtools.logError
import io.github.dot166.jlib.app.devtools.logInfo
import io.github.dot166.jlib.dagger.DaggerJLibAppComponent
import io.github.dot166.jlib.dagger.JLibAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile


open class RestorableSettingsApplication: Application() {

    private val mainHandler by lazy { Handler(mainLooper) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var mAppComponent: JLibAppComponent? = null
    val appComponent: JLibAppComponent
        get() {
            if (mAppComponent == null) {
                synchronized(this) {
                    // Check for null again, as it may have been assigned on a different thread. This
                    // avoids holding synchronization locks everytime.
                    if (mAppComponent == null) {
                        // Initialize the dagger component on demand as content providers can get
                        // accessed before the jLib application (b/36917845#comment4)
                        initDaggerComponent()
                    }
                }
            }
            // Since supertype setters will return a supertype.builder and @Component.Builder types
            // must not have any generic types.
            // We need to cast mAppComponent to {@link JLibAppComponent} since appContext()
            // method is defined in the super class JLibBaseComponent#Builder.
            return mAppComponent as JLibAppComponent
        }

    override fun onCreate() {
        super.onCreate()
        val jLibSharedPreferencesStorage = LocalSharedPreferencesStorage(this)
        BackupRestoreStorageManager.getInstance(this)
            .add(
                jLibSharedPreferencesStorage,
                DefaultSharedPreferencesStorage(this),
            )
        jLibSharedPreferencesStorage.migrateToLocalSharedPrefs()
        setSpaEnvironment(JLibSpaEnvironmentStub(this))
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
                context = this@RestorableSettingsApplication,
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

    /**
     * Init with the desired dagger component.
     */
    open fun initDaggerComponent() {
        mAppComponent = DaggerJLibAppComponent.builder()
            .appContext(this)
            .build()
    }

    @Deprecated("No Longer Supported")
    fun setSpaEnvironment(env: JLibSpaEnvironment) {
        SpaEnvironmentFactory.reset(env)
    }

    override fun onTerminate() {
        BackupRestoreStorageManager.getInstance(this).removeAll()
        super.onTerminate()
    }
}