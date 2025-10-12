package io.github.dot166.jlib.utils

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import io.github.dot166.jlib.utils.ErrorUtils.handle
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetUtils {
    fun getDataRaw(urlString: String?, context: Context, timeoutMs: Int = 2000): String {
        val policy = ThreadPolicy.Builder().permitNetwork().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection)
            connection.setConnectTimeout(timeoutMs)
            return inputSteramToString(connection.getInputStream())
        } catch (e: Exception) {
            handle(e, context)
            return ""
        }
    }

    @Throws(IOException::class)
    fun inputSteramToString(`in`: InputStream?): String {
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            builder.append(line).append("\n")
        }
        reader.close()
        return builder.toString()
    }
}
