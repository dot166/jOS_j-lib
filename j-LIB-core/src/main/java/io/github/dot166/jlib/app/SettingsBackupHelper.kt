package io.github.dot166.jlib.app

import android.app.backup.BackupAgentHelper
import com.android.settingslib.datastore.BackupRestoreStorageManager

/** Backup agent  */
class SettingsBackupHelper : BackupAgentHelper() {
    override fun onCreate() {
        super.onCreate()
        BackupRestoreStorageManager.getInstance(this).addBackupAgentHelpers(this)
    }

    override fun onRestoreFinished() {
        super.onRestoreFinished()
        BackupRestoreStorageManager.getInstance(this).onRestoreFinished()
    }
}