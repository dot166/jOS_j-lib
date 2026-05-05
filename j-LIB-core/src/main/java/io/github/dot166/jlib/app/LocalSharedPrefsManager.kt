package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceManager
import com.android.settingslib.datastore.SharedPreferencesStorage
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.dot166.jlib.RSSFeed
import io.github.dot166.jlib.RssFilter


/**
 * A data manager that manages [SharedPreferences] for jLib components,
 * individual apps can write their own version of this or use normal [SharedPreferences] with a
 * different backup implementation if they want
 * @param mContext The context
 */
class LocalSharedPrefsManager(private val mContext: Context) {
    internal val logTag = "BackupRestoreStorage"

    fun saveExcludedRssFeeds(list: MutableList<String>) {
        val gson = GsonBuilder()
            .setExclusionStrategies(RssFilter())
            .serializeNulls()
            .create()
        val json = gson.toJson(list)
        getDataStore(mContext).putString("ExcludedRssUrls", json)
    }

    fun saveRssFeeds(list: MutableList<RSSFeed>) {
        val gson = GsonBuilder()
            .setExclusionStrategies(RssFilter())
            .serializeNulls()
            .create()
        val json = gson.toJson(list)
        getDataStore(mContext).putString("RssUrls", json)
    }

    fun getRssFeeds(): MutableList<RSSFeed> {
        val gson = GsonBuilder()
            .setExclusionStrategies(RssFilter())
            .serializeNulls()
            .create()
        val b = gson.fromJson(getDataStore(mContext).getString("RssUrls", ""), object : TypeToken<MutableCollection<RSSFeed>>() {})
        return b.toMutableList()
    }

    fun getExcludedRssFeeds(): MutableList<String> {
        val gson = GsonBuilder()
            .setExclusionStrategies(RssFilter())
            .serializeNulls()
            .create()
        val b = gson.fromJson(getDataStore(mContext).getString("ExcludedRssUrls", ""), object : TypeToken<MutableCollection<String>>() {})
        return b.toMutableList()
    }

    fun migrateToLocalSharedPrefs() {
        val newPrefs = getSharedPreferences(mContext)
        val oldPrefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val keys = listOf("RssUrls", "ExcludedRssUrls")
        val migratedKeys = newPrefs.getString("migratedKeys", "")!!.split(";")
        val oldEntries = oldPrefs.all.filterKeys { it in keys }.filterKeys { it !in migratedKeys }
        if (oldEntries.isNotEmpty()) {
            val editor = mergeSharedPreferences(newPrefs, oldEntries, "Migrate")
            editor.putString("migratedKeys", keys.joinToString(";"))
            editor.commit() // commit to avoid race condition
            // clear the old SharedPreferences
            oldPrefs.edit {
                for (key in oldEntries.keys) {
                    remove(key)
                }
            }
        }
    }
    @VisibleForTesting
    internal fun mergeSharedPreferences(
        sharedPreferences: SharedPreferences,
        entries: Map<String, Any?>,
        operation: String,
    ): SharedPreferences.Editor {
        val filter: (String, Any?) -> Boolean = { _, _ -> true }
        val editor = sharedPreferences.edit()
        for ((key, value) in entries) {
            if (!filter.invoke(key, value)) {
                Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation skips $key=$value")
                continue
            }
            if (key == "RssUrls") {
                Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation $key=$value")
                val gson = GsonBuilder()
                    .setExclusionStrategies(RssFilter())
                    .serializeNulls()
                    .create()
                val list = (value as String).split(";")
                val feedList = mutableListOf<RSSFeed>()
                for (urlString in list) {
                    var excludeList = PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString("ExcludedRssUrls", "")!!.split(";")
                    if (excludeList.isEmpty()) {
                        // assume already migrated, if the list was empty or nonexistent it is technically already migrated if migration for exclude wasn't completed yet
                        // although, if migration for exclude was completed before this, this logic won't register as migrated as it doesn't clear, so the migrated and original should be the same
                        excludeList = (entries["ExcludedRssUrls"] as String).split(";")
                    }
                    val feed =
                        RSSFeed(false, urlString, null, excludeList.contains(urlString))
                    feedList.add(feed)
                }
                val json = gson.toJson(feedList)
                editor.putString(key, json)
                continue
            } else if (key == "ExcludedRssUrls") {
                Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation $key=$value")
                val gson = GsonBuilder()
                    .setExclusionStrategies(RssFilter())
                    .serializeNulls()
                    .create()
                val list = (value as String).split(";")
                val json = gson.toJson(list)
                editor.putString(key, json)
            }
            when (value) {
                is Boolean -> {
                    editor.putBoolean(key, value)
                    Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation Boolean $key=$value")
                }
                is Float -> {
                    editor.putFloat(key, value)
                    Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation Float $key=$value")
                }
                is Int -> {
                    editor.putInt(key, value)
                    Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation Int $key=$value")
                }
                is Long -> {
                    editor.putLong(key, value)
                    Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation Long $key=$value")
                }
                is String -> {
                    editor.putString(key, value)
                    Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation String $key=$value")
                }
                is Set<*> -> {
                    val nonString = value.firstOrNull { it !is String }
                    if (nonString != null) {
                        Log.e(
                            logTag,
                            "[${mContext.packageName + LOCAL_PREFS}] $operation StringSet $key=$value" +
                                    " but non string found: $nonString (${nonString.javaClass})",
                        )
                    } else {
                        @Suppress("UNCHECKED_CAST") editor.putStringSet(key, value as Set<String>)
                        Log.v(logTag, "[${mContext.packageName + LOCAL_PREFS}] $operation StringSet $key=$value")
                    }
                }
                else -> {
                    Log.e(
                        logTag,
                        "[${mContext.packageName + LOCAL_PREFS}] $operation $key=$value, unknown type: ${value?.javaClass}",
                    )
                }
            }
        }
        return editor
    }

    companion object {
        const val LOCAL_PREFS: String = "_jLib_main_prefs"

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
                override fun getLong(key: String, defValue: Long): Long = prefs.getLong(key, defValue)
            }
        }

        /** Returns the underlying [SharedPreferences] storage.  */
        fun getSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
            return SharedPreferencesStorage(context, context.packageName + LOCAL_PREFS, Context.MODE_PRIVATE)
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.packageName + LOCAL_PREFS, Context.MODE_PRIVATE)
        }
    }
}