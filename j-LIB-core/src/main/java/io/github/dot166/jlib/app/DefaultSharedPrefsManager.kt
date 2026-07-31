package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import com.android.settingslib.datastore.SharedPreferencesStorage

/**
 * A data manager that manages the default [SharedPreferences]
 */
@Deprecated(
    message = "Please use DefaultSharedPreferences directly",
    level = DeprecationLevel.ERROR
)
object DefaultSharedPrefsManager {
    /** Returns the underlying [SharedPreferences] storage.  */
    @Deprecated(
        message = "Please use DefaultSharedPreferences directly",
        replaceWith = ReplaceWith(
            "DefaultSharedPreferencesStorage(context)",
            "io.github.dot166.jlib.app.DefaultSharedPreferencesStorage"
        ),
        level = DeprecationLevel.ERROR
    )
    fun getSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
        return DefaultSharedPreferencesStorage(context)
    }
}