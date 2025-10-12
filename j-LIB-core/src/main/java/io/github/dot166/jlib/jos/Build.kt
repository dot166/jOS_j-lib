package io.github.dot166.jlib.jos

import android.os.Build
import android.util.Log
import io.github.dot166.jlib.app.jLIBCoreApp
import io.github.dot166.jlib.utils.VersionUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Build {
    private val internalRelease: String
        get() {
            val date = Date(Build.TIME)
            val formattedDate =
                SimpleDateFormat("yMMdd", Locale.UK).format(date) + "j"
            if (formattedDate == Build.DISPLAY) {
                return VersionUtils.androidVersion + "-" + Build.DISPLAY.replace(
                    "j",
                    ""
                )
            }
            return "0"
        }


    /** A String utilized to distinguish jOS versions  */
    @JvmField
    val jOS_RELEASE: String = internalRelease

    /** A boolean value utilized to determine if the device is running jOS  */
    val is_jOS: Boolean = jOS_RELEASE != "0"

    private val codeNameInternal: String
        get() {
            val version =
                jOS_RELEASE.split("-".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()[0].replace("-", "")
                    .split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].replace(".", "")
            when (version) {
                "0" -> {
                    Log.e(jLIBCoreApp.TAG, "Device is not running jOS, returning '0'")
                    return "0"
                }

                "15" -> {
                    return "Obsidian"
                }

                "16" -> {
                    return "Plasma"
                }

                else -> {
                    Log.i(
                        jLIBCoreApp.TAG,
                        "this version of jLib is too old for this jOS version, quick return the jOS Version"
                    )
                    return version
                }
            }
        }

    var jOS_CODENAME: String = codeNameInternal

    var jOS_RELEASE_WITH_CODENAME: String = "$jOS_RELEASE ($jOS_CODENAME)"

    private val shortCodeNameInternal: String?
        get() {
            if (isNumber(jOS_CODENAME)) {
                // must have a number, just return it
                return jOS_CODENAME
            } else {
                return jOS_CODENAME.substring(0, 1)
            }
        }

    private fun isNumber(str: String): Boolean {
        if (str.length == 1) {
            return str.matches("[0-9]".toRegex())
        } else if (str.length == 2) {
            return str.matches("[0-9][0-9]".toRegex())
        } else if (str.length == 3) {
            return str.matches("[0-9][0-9][0-9]".toRegex())
        } else {
            return false // not bothering for any larger numbers just yet
        }
    }

    var jOS_CODENAME_SHORT: String? = shortCodeNameInternal
}
