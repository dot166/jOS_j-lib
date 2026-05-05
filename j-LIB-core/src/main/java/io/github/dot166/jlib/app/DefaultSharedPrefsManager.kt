package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import com.android.settingslib.datastore.SharedPreferencesStorage
import androidx.preference.PreferenceDataStore

/**
 * A data manager that manages the default [SharedPreferences]
 */
class DefaultSharedPrefsManager() {

    companion object {

        /** not really necessary for default [SharedPreferences] but it is here if you need it */
        fun getDataStore(context: Context): PreferenceDataStore {
            return object : PreferenceDataStore() {
                private val prefs = getSharedPreferences(context)

                override fun putString(key: String, value: String?) = prefs.edit().putString(key, value).apply()
                override fun getString(key: String, defValue: String?): String? = prefs.getString(key, defValue)

                override fun putBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
                override fun getBoolean(key: String, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)

                override fun putInt(key: String, value: Int) = prefs.edit().putInt(key, value).apply()
                override fun getInt(key: String, defValue: Int): Int = prefs.getInt(key, defValue)

                override fun putStringSet(key: String, values: Set<String?>?) = prefs.edit().putStringSet(key, values).apply()
                override fun getStringSet(key: String, defValues: Set<String?>?): Set<String?>? = prefs.getStringSet(key, defValues)

                override fun putFloat(key: String, value: Float) = prefs.edit().putFloat(key, value).apply()
                override fun getFloat(key: String, defValue: Float): Float = prefs.getFloat(key, defValue)

                override fun putLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()
                override fun getLong(key: String?, defValue: Long): Long = prefs.getLong(key, defValue)
            }
        }

        /** Returns the underlying [SharedPreferences] storage.  */
        fun getSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
            return SharedPreferencesStorage.getDefault(context, SharedPreferencesStorage.getDefaultSharedPreferencesName(context))
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SharedPreferencesStorage.getDefaultSharedPreferencesName(context), Context.MODE_PRIVATE)
        }
    }
}