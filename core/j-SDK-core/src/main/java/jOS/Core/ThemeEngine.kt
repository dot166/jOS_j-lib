package jOS.Core

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent

object ThemeEngine {
    @JvmField
    var currentTheme: String? = null
    var builder: AlertDialog.Builder? = null
    var TAG: String = "jOS Theme Engine"
    var TAGDB1: String = TAG + " - DB1"

    /**
     * jOS ThemeEngine: get the theme
     *
     * @param context context
     * @return theme
     */
    @JvmStatic
    fun getSystemTheme(context: Context): Int {
        val Theme = getThemeFromDB1(context)
        currentTheme = getThemeFromDB1(context)
        when (Theme) {
            "Holo" -> {
                Log.i(TAG, "jOS.Core.R.style.jOS_Theme")
                return R.style.jOS_Theme
            }

            "M3 Dark" -> {
                Log.i(TAG, "com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar")
                return com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar
            }

            "M3 Light" -> {
                Log.i(TAG, "com.google.android.material.R.style.Theme_Material3_Light_NoActionBar")
                return com.google.android.material.R.style.Theme_Material3_Light_NoActionBar
            }

            "AppCompat Dark" -> {
                Log.i(TAG, "androidx.appcompat.R.style.Theme_AppCompat_NoActionBar")
                return androidx.appcompat.R.style.Theme_AppCompat_NoActionBar
            }

            "AppCompat Light" -> {
                Log.i(TAG, "androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar")
                return androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar
            }
        }
        if (Theme != "none") {
            Log.i(TAG, "Unrecognised Theme '" + currentTheme + "'")
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!")
            missingThemeEngine(context)
        }
        return R.style.jOS_Theme
    }

    private fun missingThemeEngine(context: Context) {
        builder = AlertDialog.Builder(context, R.style.jOS_Theme)

        builder!!.setMessage(R.string.dialog_message)
            .setTitle(R.string.dialog_title)
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_positive) { dialog, id ->
                val url = "https://github.com/dot166/jOS_ThemeEngine/releases"
                val intent = CustomTabsIntent.Builder()
                    .build()
                intent.launchUrl(context, Uri.parse(url))
            }
            .setNegativeButton(R.string.dialog_negative) { dialog, id ->
                Log.i(
                    TAG,
                    "IGNORING ThemeEngine ERROR"
                )
            }
        //Creating dialog box
        val alert = builder!!.create()
        alert.show()
    }

    @SuppressLint("Range")
    fun getAllThemes(context: jActivity): StringBuilder {
        // creating a cursor object of the
        // content URI

        val cursor = context.contentResolver.query(
            Uri.parse("content://jOS.Core.ThemeEngine.database/themes"),
            null,
            null,
            null,
            null
        )

        if (cursor != null) {
            // iteration of the cursor
            // to print whole table
            if (cursor.moveToFirst()) {
                val strBuild = StringBuilder()
                while (!cursor.isAfterLast) {
                    Log.i(
                        "Theme Engine - Load Data",
                        cursor.getString(cursor.getColumnIndex("id")) + "-" + cursor.getString(
                            cursor.getColumnIndex("name")
                        ) + "-" + cursor.getString(cursor.getColumnIndex("current"))
                    )
                    strBuild.append(
                        """
    
    ${cursor.getString(cursor.getColumnIndex("id"))}-${cursor.getString(cursor.getColumnIndex("name"))}-${
                            cursor.getString(
                                cursor.getColumnIndex("current")
                            )
                        }
    """.trimIndent()
                    )
                    cursor.moveToNext()
                }
                return strBuild
            } else {
                return StringBuilder(context.getString(R.string.no_records_found))
            }
        } else {
            return StringBuilder(context.getString(R.string.no_records_found))
        }
    }

    @JvmStatic
    @SuppressLint("Range")
    fun getThemeFromDB1(context: Context): String {
        // get from database

        // creating a cursor object of the
        // content URI

        val cursor = context.contentResolver.query(
            Uri.parse("content://jOS.Core.ThemeEngine.database/themes"),
            null,
            null,
            null,
            null
        )

        if (cursor != null) {
            // iteration of the cursor
            // to find selected theme
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    if (cursor.getString(cursor.getColumnIndex("current")).toBoolean()) {
                        Log.i(TAGDB1, cursor.getString(cursor.getColumnIndex("name")))
                        return cursor.getString(cursor.getColumnIndex("name"))
                    } else {
                        cursor.moveToNext()
                    }
                }
            }
        }
        Log.i(TAGDB1, "No Records Found")
        return "none"
    }
}
