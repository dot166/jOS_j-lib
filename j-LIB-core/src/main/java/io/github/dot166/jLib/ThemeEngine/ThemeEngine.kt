package io.github.dot166.jLib.ThemeEngine

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.github.dot166.jLib.R
import io.github.dot166.jLib.app.jWebActivity
import io.github.dot166.jLib.utils.VersionUtils

object ThemeEngine {
    @JvmField
    var currentTheme: String? = null
    var TAG: String = "jLib Theme Engine"
    var TAG_DB: String = "$TAG - DB"
    private var isDarkOnly: Boolean? = null
    var themeClass: values? = null
    private val jLIB_CHECK_ATTRS = intArrayOf(R.attr.isJTheme)
    private val LIGHT_CHECK_ATTRS = intArrayOf(androidx.appcompat.R.attr.isLightTheme)

    @SuppressLint("PrivateResource")
    private val MATERIAL3_CHECK_ATTRS =
        intArrayOf(com.google.android.material.R.attr.isMaterial3Theme)

    /**
     * Set the class that contains the theme values.
     * MUST BE SET IN THE onCreate() FUNCTION IN EITHER THE APPLICATION CLASS OR ALL OF THE EXPORTED ACTIVITIES
     */
    fun setCustomThemeClass(customThemeClass: values?) {
        themeClass = customThemeClass
    }

    /**
     * jOS ThemeEngine: get the theme
     *
     * @param context context
     * @return theme, will return 0 if ThemeEngine is disabled
     */
    @JvmStatic
    fun getSystemTheme(context: Context): Int {
        var theme: String = getThemeFromDB(context)
        currentTheme = getThemeFromDB(context)
        Log.i(TAG, theme)
        when (theme) {
            "jLib" -> {
                isDarkOnly = true
                if (themeClass != null && themeClass!!.jLibTheme() != 0 && isjLibTheme(
                        context,
                        themeClass!!.jLibTheme()
                    ) && !isLightTheme(context, themeClass!!.jLibTheme())
                ) {
                    return themeClass!!.jLibTheme()
                }
                return R.style.j_Theme
            }

            "M3" -> {
                isDarkOnly = false
                if (themeClass != null && themeClass!!.M3() != 0 && isMaterial3Theme(
                        context,
                        themeClass!!.M3()
                    ) && isDayNightTheme(context, themeClass!!.M3())
                ) {
                    return themeClass!!.M3()
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight_NoActionBar
            }

            "Disabled" -> {
                isDarkOnly = false
                Log.i(
                    TAG,
                    "ThemeEngine Disabled, Returning no legacy scheme and the Default Compose Theme"
                )
                return 0
            }
        }
        if (theme != "none") {
            Log.i(TAG, "Unrecognised Theme '$currentTheme'")
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!")
            if (context !is jWebActivity) {
                val builder = AlertDialog.Builder(context)

                builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setCancelable(false)
                    .setPositiveButton(
                        R.string.dialog_positive,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, id: Int) {
                                val url = "https://github.com/dot166/jOS_ThemeEngine/releases"
                                val intent = CustomTabsIntent.Builder()
                                    .build()
                                intent.launchUrl(context, Uri.parse(url))
                            }
                        })
                    .setNegativeButton(
                        R.string.dialog_negative,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, id: Int) {
                                Log.i(TAG, "IGNORING ThemeEngine ERROR")
                            }
                        })
                //Creating dialog box
                val alert = builder.create()
                alert.show()
            }
        }
        isDarkOnly = true
        return R.style.j_Theme
    }

    private fun getLightColourScheme(context: Context): ColorScheme {
        if (themeClass != null && themeClass!!.LComposeColourScheme(context) != null) {
            return themeClass!!.LComposeColourScheme(context)
        } else {
            if (VersionUtils.isAtLeastS()) {
                return dynamicLightColorScheme(context)
            }
            return lightColorScheme()
        }
    }

    private fun getDayNightColourScheme(context: Context): ColorScheme {
        val manager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val dark = manager.nightMode == UiModeManager.MODE_NIGHT_YES
        return if (dark || isDarkOnly == true) {
            getDarkColourScheme(context)
        } else {
            getLightColourScheme(context)
        }
    }

    @Composable
    private fun getTypography(context: Context): Typography {
        return if (themeClass != null && themeClass!!.ComposeTypography(context) != null) {
            themeClass!!.ComposeTypography(context)
        } else {
            MaterialTheme.typography
        }
    }

    @Composable
    private fun getShapes(context: Context): Shapes {
        return if (themeClass != null && themeClass!!.ComposeShapes(context) != null) {
            themeClass!!.ComposeShapes(context)
        } else {
            MaterialTheme.shapes
        }
    }

    private fun getDarkColourScheme(context: Context): ColorScheme {
        if (themeClass != null && themeClass!!.DComposeColourScheme(context) != null) {
            return themeClass!!.DComposeColourScheme(context)
        } else {
            if (VersionUtils.isAtLeastS()) {
                return dynamicDarkColorScheme(context)
            }
            return darkColorScheme()
        }
    }


    /**
     * gets the androidx compose theme
     * @return the compose theme
     */
    @Composable
    @JvmStatic
    fun GetComposeTheme(context: Context,
                        content: @Composable () -> Unit) {
        MaterialTheme(colorScheme = getDayNightColourScheme(context), shapes = getShapes(context), typography = getTypography(context), content = content)
        Log.i(TAG, "jLib ThemeEngine Compose initialised")
    }

    /**
     * checks if theme is DayNight or not
     * @param context context for getting value of lightTheme attribute
     * @param theme int theme to check
     * @return boolean true if theme is a light theme
     */
    fun isDayNightTheme(context: Context, theme: Int): Boolean {
        val manager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val dark = manager.nightMode == UiModeManager.MODE_NIGHT_YES
        return if (dark) {
            !isLightTheme(context, theme)
        } else {
            isLightTheme(context, theme)
        }
    }

    /**
     * checks if theme is light or not
     * @param context context for getting value of lightTheme attribute
     * @param theme int theme to check
     * @return boolean true if theme is a light theme
     */
    fun isLightTheme(context: Context, theme: Int): Boolean {
        return isThemeBoolean(context, LIGHT_CHECK_ATTRS, theme)
    }

    /**
     * checks if theme is extended from jTheme
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from jTheme
     */
    fun isjLibTheme(context: Context, theme: Int): Boolean {
        return isThemeBoolean(context, jLIB_CHECK_ATTRS, theme)
    }

    /**
     * checks if theme is extended from ThemeMaterial3
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from ThemeMaterial3
     */
    fun isMaterial3Theme(context: Context, theme: Int): Boolean {
        return isThemeBoolean(context, MATERIAL3_CHECK_ATTRS, theme)
    }

    /**
     * checks if the theme has the attributes and returns value of boolean attribute at index 0
     * @param context Context for getting attributes to check
     * @param themeAttributes int[] attributes to check
     * @param theme int theme id
     * @return boolean value of checked attribute at index 0
     */
    fun isThemeBoolean(context: Context, themeAttributes: IntArray, theme: Int): Boolean {
        val array = context.theme.obtainStyledAttributes(theme, themeAttributes)
        val value = array.getBoolean(0, false)
        array.recycle()
        return value
    }

    @JvmStatic
    @SuppressLint("Range")
    fun getThemeFromDB(context: Context): String {
        // get from database

        // creating a cursor object of the
        // content URI

        @SuppressLint("Recycle") val cursor = context.contentResolver.query(
            Uri.parse("content://io.github.dot166.ThemeEngine.database/themes"),
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
                        Log.i(TAG_DB, cursor.getString(cursor.getColumnIndex("name")))
                        return cursor.getString(cursor.getColumnIndex("name"))
                    } else {
                        cursor.moveToNext()
                    }
                }
            }
        }
        Log.i(TAG_DB, "No Records Found")
        return "none"
    }
}
