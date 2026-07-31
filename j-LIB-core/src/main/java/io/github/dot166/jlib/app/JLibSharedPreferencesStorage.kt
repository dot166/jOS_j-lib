package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.android.settingslib.datastore.BackupCodec
import com.android.settingslib.datastore.SharedPreferencesStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

private fun defaultVerbose() = Build.TYPE == "eng"

open class JLibSharedPreferencesStorage : SharedPreferencesStorage {
    protected val LOG_TAG = "BackupRestoreStorage"

    val gson: Gson = GsonBuilder().serializeNulls().create()

    @JvmOverloads
    constructor(
        context: Context,
        name: String,
        sharedPreferences: SharedPreferences,
        filePath: String = getSharedPreferencesFilePath(context, name),
        codec: BackupCodec? = null,
        verbose: Boolean = defaultVerbose(),
        filter: (String, Any?) -> Boolean = { _, _ -> true },
    ) : super(context, name, sharedPreferences, filePath, codec, verbose, filter)

    @JvmOverloads
    constructor(
        context: Context,
        name: String,
        mode: Int,
        codec: BackupCodec? = null,
        verbose: Boolean = defaultVerbose(),
        filter: (String, Any?) -> Boolean = { _, _ -> true },
    ) : super(
        context,
        name,
        mode,
        codec,
        verbose,
        filter,
    )

    override fun <T : Any> getValue(key: String, valueType: Class<T>): T? {
        return when {
            Set::class.javaObjectType.isAssignableFrom(valueType) -> {
                gson.fromJson(getString(key)?:"", object : TypeToken<Set<*>>() {}) as Set<*>
            }
            else -> super.getValue(key, valueType)
        } as T?
    }

    override fun <T : Any> setValue(key: String, valueType: Class<T>, value: T?) {
        when {
            Set::class.javaObjectType.isAssignableFrom(valueType) -> {
                setString(key, gson.toJson(value as Set<*>))
            }
            else -> super.setValue(key, valueType, value)
        }
    }

    /** Gets the string value of given key. */
    fun getSet(key: String): Set<*>? = getValue(key, Set::class.javaObjectType)

    /** Sets string value for given key, null value means delete the key from data store. */
    fun setSet(key: String, value: Set<*>?) = setValue(key, Set::class.javaObjectType, value)
}