package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import com.android.settingslib.datastore.SharedPreferencesStorage

/**
 * A data manager that manages the default [SharedPreferences]
 */
object DefaultSharedPrefsManager {
    /** Returns the underlying [SharedPreferences] storage.  */
    fun getSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
        return SharedPreferencesStorage.getDefault(
            context,
            SharedPreferencesStorage.getDefaultSharedPreferencesName(context)
        )
    }
}