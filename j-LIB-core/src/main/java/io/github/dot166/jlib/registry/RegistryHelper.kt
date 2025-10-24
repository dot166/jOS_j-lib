package io.github.dot166.jlib.registry

import android.content.Context
import io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile

object RegistryHelper {
    fun getFromRegistry(context: Context): MutableList<Object?> {
        return readXmlFromFile(context, "Registry.xml")
    }

    class Object(attributes: MutableMap<String?, String?>) {
        var name: String?
            private set
        var url: String?
            private set
        var logoUrl: String?
            private set

        init {
            this.name = attributes["objectName"]
            this.url = attributes["objectUrl"]
            this.logoUrl = attributes["objectLogoUrl"]
        }

        fun updateAttributes(attributes: MutableMap<String?, String?>) {
            this.name = attributes["objectName"]
            this.url = attributes["objectUrl"]
            this.logoUrl = attributes["objectLogoUrl"]
        }
    }
}
