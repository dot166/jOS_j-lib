package io.github.dot166.jlib.registry

import android.content.Context
import io.github.dot166.jlib.registry.XmlHelper.readXmlFromFile

object RegistryHelper {
    fun getFromRegistry(context: Context): MutableList<Object> {
        return readXmlFromFile(context, "Registry.xml")
    }

    class Object(var name: String, var url: String, var logoUrl: String) {
        fun updateAttributes(name: String, url: String, logoUrl: String) {
            this.name = name
            this.url = url
            this.logoUrl = logoUrl
        }
    }
}
