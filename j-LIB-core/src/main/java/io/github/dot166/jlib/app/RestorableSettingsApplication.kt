package io.github.dot166.jlib.app

import android.app.Application
import com.android.settingslib.datastore.BackupRestoreStorageManager
import com.android.settingslib.spa.framework.common.SpaEnvironmentFactory
import io.github.dot166.jlib.dagger.DaggerJLibAppComponent
import io.github.dot166.jlib.dagger.JLibAppComponent
import kotlin.concurrent.Volatile


open class RestorableSettingsApplication: Application() {

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
    }

    /**
     * Init with the desired dagger component.
     */
    open fun initDaggerComponent() {
        mAppComponent = DaggerJLibAppComponent.builder()
            .appContext(this)
            .build()
    }

    fun setSpaEnvironment(env: JLibSpaEnvironment) {
        SpaEnvironmentFactory.reset(env)
    }

    override fun onTerminate() {
        BackupRestoreStorageManager.getInstance(this).removeAll()
        super.onTerminate()
    }
}