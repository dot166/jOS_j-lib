package io.github.dot166.jlib.registry

import android.content.Context
import io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile

object RegistryHelper {
    fun getFromRegistry(context: Context): MutableList<Object> {
        return readXmlFromFile(context, "Registry.xml")
    }

    class Object(attributes: MutableMap<String, String>) {
        lateinit var url: String
            private set

        init {
            if (attributes["objectUrl"] != null) {
                this.url = attributes["objectUrl"]!!
            }
        }

        fun updateAttributes(attributes: MutableMap<String, String>) {
            if (attributes["objectUrl"] != null) {
                this.url = attributes["objectUrl"]!!
            }
        }
    }
}
