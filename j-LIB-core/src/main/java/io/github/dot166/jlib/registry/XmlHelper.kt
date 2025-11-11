package io.github.dot166.jlib.registry

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Xml
import io.github.dot166.jlib.utils.ErrorUtils.handle
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object XmlHelper {
    @JvmStatic
    fun writeXmlToFile(
        context: Context,
        fileName: String?,
        stations: MutableList<RegistryHelper.Object>
    ) {
        try {
            // Create an XmlSerializer instance
            val serializer = Xml.newSerializer()
            // Open file output stream
            val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(fos)

            // Initialize XML Serializer
            serializer.setOutput(writer)

            // Start XML document
            serializer.startDocument("UTF-8", true)

            // Start a root element
            serializer.startTag("", "objects")

            // Add child elements with data
            for (i in stations.indices) {
                val station = stations[i]
                serializer.startTag("", "object")
                serializer.attribute("", "objectName", station.name)
                serializer.attribute("", "objectUrl", station.url)
                serializer.attribute("", "objectLogoUrl", station.logoUrl)
                serializer.endTag("", "object")
            }

            // End root element
            serializer.endTag("", "objects")

            // End the document
            serializer.endDocument()

            writer.close() // Close the writer to save the file
        } catch (e: Exception) {
            handle(e, context)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun readXmlFromFile(
        context: Context,
        fileName: String?,
        uri: Uri? = null
    ): MutableList<RegistryHelper.Object> {
        val stationList: MutableList<RegistryHelper.Object> = ArrayList()
        try {
            // Open the XML file
            val fis: InputStream? = if (uri != null) {
                context.contentResolver.openInputStream(uri)
            } else {
                context.openFileInput(fileName)
            }
            val reader = InputStreamReader(fis)

            // Create a new XML pull parser instance
            val parser = Xml.newPullParser()
            parser.setInput(reader)

            // Start parsing the XML file
            val str = parseXmlAsString(parser)
            val strAar: Array<String?> =
                str.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in strAar.indices) {
                val attributes: MutableMap<String, String> = HashMap()
                val attrs: Array<String?> =
                    strAar[i]!!.replace("<object# ", "").replace("></object>", "")
                        .replace("# ", "#").split("#".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                for (j in attrs.indices) {
                    val keyValuePair: Array<String> =
                        attrs[j]!!.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val value: String = if (keyValuePair.size == 1) {
                        "" // must be empty
                    } else {
                        keyValuePair[1]
                    }
                    attributes[keyValuePair[0]] = value
                }
                stationList.add(RegistryHelper.Object(attributes))
            }
        } catch (e: Exception) {
            handle(e, context)
        }
        return stationList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseXmlAsString(`in`: XmlPullParser): String {
        val stringBuilder = StringBuilder()

        // Some parsers may have already consumed the event that starts the
        // document, so we manually emit that event here for consistency
//        if (in.getEventType() == XmlPullParser.START_DOCUMENT) {
//            stringBuilder.append("<?xml version='1.0' encoding='").append(in.getInputEncoding()).append("' standalone='yes' ?>");
//        }
        while (true) {
            val token = `in`.nextToken()
            when (token) {
                XmlPullParser.END_DOCUMENT -> return stringBuilder.toString()
                    .replace("<objects>", "").replace("<object></object>;", "")
                    .replace(";</objects>;", "")

                XmlPullParser.START_TAG -> {
                    stringBuilder.append("<").append(fixNamespace(`in`.namespace))
                        .append(`in`.name)
                    var i = 0
                    while (i < `in`.attributeCount) {
                        stringBuilder.append("# ")
                            .append(fixNamespace(`in`.getAttributeNamespace(i)))
                            .append(`in`.getAttributeName(i)).append("=")
                            .append(`in`.getAttributeValue(i))
                        i++
                    }
                    stringBuilder.append(">")
                }

                XmlPullParser.END_TAG -> stringBuilder.append("</")
                    .append(fixNamespace(`in`.namespace)).append(`in`.name).append(">;")

                else -> Log.w("Xml", "Unknown or unsupported token $token")
            }
        }
    }

    private fun fixNamespace(namespace: String?): String {
        return if (namespace == null || namespace.isEmpty()) {
            ""
        } else {
            "$namespace:"
        }
    }
}
