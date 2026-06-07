package io.github.dot166.jlib.app

import android.app.Application
import com.android.settingslib.datastore.BackupRestoreStorageManager
import com.android.settingslib.spa.framework.common.SpaEnvironment
import com.android.settingslib.spa.framework.common.SpaEnvironmentFactory
import com.google.android.material.color.DynamicColors

open class RestorableSettingsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        BackupRestoreStorageManager.getInstance(this)
            .add(
                LocalSharedPrefsManager.getSharedPreferencesStorage(this),
                DefaultSharedPrefsManager.getSharedPreferencesStorage(this),
            )
        LocalSharedPrefsManager(this).migrateToLocalSharedPrefs()
        setSpaEnvironment(JLibSpaEnvironmentStub(this))
    }

    fun setSpaEnvironment(env: JLibSpaEnvironment) {
        SpaEnvironmentFactory.reset(env)
    }

    override fun onTerminate() {
        BackupRestoreStorageManager.getInstance(this).removeAll()
        super.onTerminate()
    }
}