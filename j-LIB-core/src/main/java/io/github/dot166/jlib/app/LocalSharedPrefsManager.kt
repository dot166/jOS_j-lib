package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import com.android.settingslib.datastore.SharedPreferencesStorage
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.dot166.jlib.RSSFeed


/**
 * A data manager that manages [SharedPreferences] for jLib components,
 * individual apps can write their own version of this or use normal [SharedPreferences] with a
 * different backup implementation if they want
 * @param mContext The context
 */
@Deprecated(
    message = "Merged into LocalSharedPreferencesStorage",
    replaceWith = ReplaceWith(
        expression = "LocalSharedPreferencesStorage(context)",
        "io.github.dot166.jlib.app.LocalSharedPreferencesStorage"
    )
)
class LocalSharedPrefsManager(private val mContext: Context) {
    internal val logTag = "BackupRestoreStorage"

    @Deprecated(
        message = "Moved to LocalSharedPreferencesStorage",
        replaceWith = ReplaceWith(
            expression = "LocalSharedPreferencesStorage(context).saveRssFeeds(list)",
            "io.github.dot166.jlib.app.LocalSharedPreferencesStorage"
        ),
        level = DeprecationLevel.ERROR
    )
    fun saveRssFeeds(list: MutableList<RSSFeed>) {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val json = gson.toJson(list)
        LocalSharedPreferencesStorage(mContext).setString("RssUrls", json)
    }

    @Deprecated(
        message = "Moved to LocalSharedPreferencesStorage",
        replaceWith = ReplaceWith(
            expression = "LocalSharedPreferencesStorage(context).getRssFeeds()",
            "io.github.dot166.jlib.app.LocalSharedPreferencesStorage"
        ),
        level = DeprecationLevel.ERROR
    )
    fun getRssFeeds(): MutableList<RSSFeed> {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val b = gson.fromJson(LocalSharedPreferencesStorage(mContext).getString("RssUrls")?:"", object : TypeToken<MutableCollection<RSSFeed>>() {})
        return b?.toMutableList() ?: mutableListOf()
    }

    @Deprecated(
        message = "Moved to LocalSharedPreferencesStorage",
        replaceWith = ReplaceWith(
            expression = "LocalSharedPreferencesStorage(context).migrateToLocalSharedPrefs()",
            "io.github.dot166.jlib.app.LocalSharedPreferencesStorage"
        ),
        level = DeprecationLevel.ERROR
    )
    fun migrateToLocalSharedPrefs() {
        LocalSharedPreferencesStorage(mContext).migrateToLocalSharedPrefs()
    }

    companion object {
        const val LOCAL_PREFS: String = "_jLib_main_prefs"

        /** Returns the underlying [SharedPreferences] storage.  */
        @Deprecated(
            message = "Please use DefaultSharedPreferences directly",
            replaceWith = ReplaceWith(
                "LocalSharedPreferencesStorage(context)",
                "io.github.dot166.jlib.app.LocalSharedPreferencesStorage"
            ),
            level = DeprecationLevel.ERROR
        )
        fun getSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
            return LocalSharedPreferencesStorage(context)
        }
    }
}