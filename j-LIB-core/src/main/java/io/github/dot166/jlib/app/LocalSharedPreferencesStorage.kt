package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken
import io.github.dot166.jlib.RSSFeed

class LocalSharedPreferencesStorage(context: Context): JLibSharedPreferencesStorage(
    context = context,
    name = context.packageName + LOCAL_PREFS,
    mode = Context.MODE_PRIVATE
) {
    fun saveRssFeeds(list: MutableList<RSSFeed>) {
        setString("RssUrls", gson.toJson(list))
    }

    fun getRssFeeds(): MutableList<RSSFeed> {
        return gson.fromJson(
            getString("RssUrls") ?: "",
            object : TypeToken<MutableList<RSSFeed>>() {}) ?: mutableListOf()
    }

    fun migrateToLocalSharedPrefs() {
        val oldPrefs = DefaultSharedPreferencesStorage(context).sharedPreferences
        val keys = listOf("RssUrls", "ExcludedRssUrls")
        val migratedKeys = (getString("migratedKeys") ?: "").split(";")
        val oldEntries = oldPrefs.all.filterKeys { it in keys }.filterKeys { it !in migratedKeys }
        if (oldEntries.isNotEmpty()) {
            val editor = migrateSharedPreferences(sharedPreferences, oldEntries)
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

    fun migrateSharedPreferences(
        sharedPreferences: SharedPreferences,
        entries: Map<String, Any?>,
    ): SharedPreferences.Editor {
        val editor = sharedPreferences.edit()
        for ((key, value) in entries) {
            if (key == "RssUrls") {
                val list = (value as String).split(";")
                val feedList = mutableListOf<RSSFeed>()
                for (urlString in list) {
                    var excludeList =
                        DefaultSharedPreferencesStorage(context).sharedPreferences
                            .getString("ExcludedRssUrls", "")!!.split(";")
                    if (excludeList.isEmpty()) {
                        // assume already migrated, if the list was empty or nonexistent it is technically already migrated if migration for exclude wasn't completed yet
                        // although, if migration for exclude was completed before this, this logic won't register as migrated as it doesn't clear, so the migrated and original should be the same
                        excludeList = (entries["ExcludedRssUrls"] as String).split(";")
                    }
                    val feed =
                        RSSFeed(false, urlString, excludeList.contains(urlString))
                    feedList.add(feed)
                }
                val json = gson.toJson(feedList)
                editor.putString(key, json)
                Log.v(LOG_TAG, "[${name}] Migrate $key=$value")
                continue
            } else if (key == "ExcludedRssUrls") {
                val list = (value as String).split(";")
                val json = gson.toJson(list)
                editor.putString(key, json)
                Log.v(LOG_TAG, "[${name}] Migrate $key=$value")
                continue
            }
        }
        return editor
    }

    companion object {
        const val LOCAL_PREFS: String = "_jLib_main_prefs"
    }
}