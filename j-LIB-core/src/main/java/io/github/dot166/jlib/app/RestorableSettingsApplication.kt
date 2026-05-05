package io.github.dot166.jlib.app

import android.app.Application
import com.android.settingslib.datastore.BackupRestoreStorageManager
import com.google.android.material.color.DynamicColors

class RestorableSettingsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        BackupRestoreStorageManager.getInstance(this)
            .add(
                LocalSharedPrefsManager.getSharedPreferencesStorage(this),
                DefaultSharedPrefsManager.getSharedPreferencesStorage(this),
            )
        LocalSharedPrefsManager(this).migrateToLocalSharedPrefs()
    }

    override fun onTerminate() {
        BackupRestoreStorageManager.getInstance(this).removeAll()
        super.onTerminate()
    }
}