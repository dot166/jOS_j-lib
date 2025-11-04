package io.github.dot166.jlib.registry

import android.content.Context
import io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile

object RegistryHelper {
    fun getFromRegistry(context: Context): MutableList<Object> {
        return readXmlFromFile(context, "Registry.xml")
    }

    class Object(attributes: MutableMap<String, String>) {
        lateinit var name: String
            private set
        lateinit var url: String
            private set
        lateinit var logoUrl: String
            private set

        init {
            if (attributes["objectName"] != null) {
                this.name = attributes["objectName"]!!
            }
            if (attributes["objectUrl"] != null) {
                this.url = attributes["objectUrl"]!!
            }
            if (attributes["objectLogoUrl"] != null) {
                this.logoUrl = attributes["objectLogoUrl"]!!
            }
        }

        fun updateAttributes(attributes: MutableMap<String, String>) {
            if (attributes["objectName"] != null) {
                this.name = attributes["objectName"]!!
            }
            if (attributes["objectUrl"] != null) {
                this.url = attributes["objectUrl"]!!
            }
            if (attributes["objectLogoUrl"] != null) {
                this.logoUrl = attributes["objectLogoUrl"]!!
            }
        }
    }
}
