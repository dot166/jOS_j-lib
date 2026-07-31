package io.github.dot166.jlib.app

import android.content.Context
import android.content.SharedPreferences
import com.android.settingslib.datastore.SharedPreferencesStorage
import io.github.dot166.jlib.app.LocalSharedPreferencesStorage
import io.github.dot166.jlib.dagger.ApplicationContext
import io.github.dot166.jlib.dagger.DaggerSingletonObject
import io.github.dot166.jlib.dagger.JLibAppComponent
import io.github.dot166.jlib.dagger.JLibAppSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Manages jLib [SharedPreferences] through [Preference] instances.
 */
@JLibAppSingleton
open class JLibPrefs
@Inject
constructor(@ApplicationContext private val encryptedContext: Context) {

    private val flows = mutableMapOf<Preference<*>, MutableStateFlow<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> flow(preference: Preference<T>): StateFlow<T> {
        return flows.getOrPut(preference) {
            MutableStateFlow(getSharedPrefs(preference).getValue(preference.sharedPrefKey, preference.type) ?: preference.defaultValue)
        } as MutableStateFlow<T>
    }

    protected open fun <T> getSharedPrefs(preference: Preference<T>): SharedPreferencesStorage =
        preference.run {
            when (storageKey) {
                "default" -> DefaultSharedPreferencesStorage(encryptedContext)
                "jLib" -> LocalSharedPreferencesStorage(encryptedContext)
                else -> throw IllegalArgumentException("Unrecognised SharedPreferencesStorage, please override getSharedPrefs(preference)")
            }
        }

    /**
     * Stores each of the values provided in `SharedPreferences` according to the configuration
     * contained within the associated items provided. Internally, it uses apply, so the caller
     * cannot assume that the values that have been put are immediately available for use.
     *
     * The forEach loop is necessary here since there is 1 `SharedPreference.Editor` returned from
     * prepareToPutValue(itemsToValues) for every distinct `SharedPreferences` file present in the
     * provided preference configurations.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> put(preference: Preference<T>, value: T) {
        getSharedPrefs(preference).setValue(preference.sharedPrefKey, preference.type as Class<T & Any>, value)
        (flows[preference] as? MutableStateFlow<T>)?.value = value
    }

    /**
     * Checks if all the provided [Preference] have values stored in their corresponding
     * `SharedPreferences` files.
     */
    fun <T> has(vararg preferences: Preference<T>): Boolean {
        preferences
            .groupBy { getSharedPrefs(it) }
            .forEach { (prefs, itemsSublist) ->
                if (!itemsSublist.none { !prefs.contains(it.sharedPrefKey) }) return false
            }
        return true
    }

    companion object {

        @JvmField val INSTANCE = DaggerSingletonObject(JLibAppComponent::getJLibPrefs)

        @JvmStatic fun get(context: Context): JLibPrefs = INSTANCE.get(context)

        // prefs go here...

        val test = defaultPref<String?>("aaaaa", null)
        val testBool = jLibPref("test", false)

        @JvmStatic
        inline fun <reified T> defaultPref(
            sharedPrefKey: String,
            defaultValue: T,
        ): Preference<T> =
            Preference(
                sharedPrefKey = sharedPrefKey,
                defaultValue = defaultValue,
                storageKey = "default",
                type = T::class.java
            )

        @JvmStatic
        inline fun <reified T> jLibPref(
            sharedPrefKey: String,
            defaultValue: T,
        ): Preference<T> =
            Preference(
                sharedPrefKey = sharedPrefKey,
                defaultValue = defaultValue,
                storageKey = "jLib",
                type = T::class.java
            )
    }
}

data class Preference<T>(
    val sharedPrefKey: String,
    val defaultValue: T,
    val storageKey: String,
    val type: Class<out T>,
) {
    fun flow(c: Context): StateFlow<T> = JLibPrefs.get(c).flow(this)
    fun put(c: Context, value: T) = JLibPrefs.get(c).put(this, value)
}

