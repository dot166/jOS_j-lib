package io.github.dot166.jlib.app

import dev.patrickgold.jetpref.datastore.annotations.Preferences
import dev.patrickgold.jetpref.datastore.jetprefDataStoreOf
import dev.patrickgold.jetpref.datastore.model.PreferenceMigrationEntry
import dev.patrickgold.jetpref.datastore.model.PreferenceModel

val jLibPreferenceStore = jetprefDataStoreOf(JLibPreferenceModel::class)

@Preferences
abstract class JLibPreferenceModel : PreferenceModel() {
    companion object {
        const val NAME = "jLib-prefs"
    }

    val internal = Internal()
    inner class Internal {
    }

    val tests = Tests()
    inner class Tests {
        val test = string("aaaaa", "null")
        val testBool = boolean("test", false)
    }

    override fun migrate(entry: PreferenceMigrationEntry): PreferenceMigrationEntry {
        return when (entry.key) {

            // Default: keep entry
            else -> entry.keepAsIs()
        }
    }
}
