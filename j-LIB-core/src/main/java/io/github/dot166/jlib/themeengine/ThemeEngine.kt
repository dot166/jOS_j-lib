package io.github.dot166.jlib.themeengine

import android.annotation.SuppressLint
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.material.snackbar.Snackbar
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.jWebActivity
import io.github.dot166.jlib.jos.Build
import io.github.dot166.jlib.utils.VersionUtils
import io.github.dot166.jlib.web.jWebIntent

object ThemeEngine {
    var TAG: String = "jLib Theme Engine"
    var themeClass: values? = null
    private val LIGHT_CHECK_ATTRS = intArrayOf(androidx.appcompat.R.attr.isLightTheme)
    @JvmStatic
    public var tmpCurrentTheme: String = null.toString();

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
    fun getSystemTheme(context: Activity): Int {
        val theme: String = getThemeFromThemeProvider(context)
        Log.i(TAG, theme)
        when (theme) {
            "jLib" -> {
                tmpCurrentTheme = null.toString()
                if (themeClass != null && themeClass!!.jLibTheme() != 0 && isValidTheme("jLib", context, themeClass!!.jLibTheme())
                ) {
                    return themeClass!!.jLibTheme()
                }
                return R.style.j_Theme
            }

            "M3" -> {
                tmpCurrentTheme = null.toString()
                if (themeClass != null && themeClass!!.M3() != 0 && isValidTheme("M3", context, themeClass!!.M3())
                ) {
                    return themeClass!!.M3()
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight_NoActionBar
            }

            "Disabled" -> {
                tmpCurrentTheme = null.toString()
                Log.i(
                    TAG,
                    "ThemeEngine Disabled, Returning no legacy scheme and the Default Compose Theme"
                )
                return 0
            }
        }
        if (theme != "none") {
            tmpCurrentTheme = null.toString()
            Log.i(TAG, "Unrecognised Theme '$theme'")
            return R.style.j_Theme
        } else {
            tmpCurrentTheme = null.toString()
            Log.e(TAG, "ThemeEngine is MISSING!!!!")
            if (isWebViewUsed(context)) {
                Snackbar.make(context.findViewById<View>(android.R.id.content), R.string.theme_snackbar, Snackbar.LENGTH_SHORT).setAction(R.string.theme_dialog_positive, object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val url = "https://github.com/dot166/jOS_j-lib/releases/tag/v" + VersionUtils.getLibVersion(context)
                        val intent = jWebIntent(context)
                        intent.setUrl(url)
                        intent.configureWebView(true, true)
                        intent.launch()
                    }
                }).show()
            }
            return 0 // let app handle it when ThemeEngine is missing
        }
    }

    @SuppressLint("Recycle")
    private fun getColourScheme(context: Context): ColorScheme {
        return ColorScheme(
            primary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimary)).getColor(0, 0)),
            onPrimary= Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnPrimary)).getColor(0, 0)),
            primaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimaryContainer)).getColor(0, 0)),
            onPrimaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnPrimaryContainer)).getColor(0, 0)),
            inversePrimary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimaryInverse)).getColor(0, 0)),
            secondary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSecondary)).getColor(0, 0)),
            onSecondary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnSecondary)).getColor(0, 0)),
            secondaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSecondaryContainer)).getColor(0, 0)),
            onSecondaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnSecondaryContainer)).getColor(0, 0)),
            tertiary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorTertiary)).getColor(0, 0)),
            onTertiary = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnTertiary)).getColor(0, 0)),
            tertiaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorTertiaryContainer)).getColor(0, 0)),
            onTertiaryContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnTertiaryContainer)).getColor(0, 0)),
            background = Color(context.obtainStyledAttributes(intArrayOf(android.R.attr.colorBackground)).getColor(0, 0)),
            onBackground = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnBackground)).getColor(0, 0)),
            surface = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurface)).getColor(0, 0)),
            onSurface = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnSurface)).getColor(0, 0)),
            surfaceVariant = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceVariant)).getColor(0, 0)),
            onSurfaceVariant = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnSurfaceVariant)).getColor(0, 0)),
            surfaceTint = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimary)).getColor(0, 0)),
            inverseSurface = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceInverse)).getColor(0, 0)),
            inverseOnSurface = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnSurfaceInverse)).getColor(0, 0)),
            error = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorError)).getColor(0, 0)),
            onError = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnError)).getColor(0, 0)),
            errorContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorErrorContainer)).getColor(0, 0)),
            onErrorContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnErrorContainer)).getColor(0, 0)),
            outline = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOutline)).getColor(0, 0)),
            outlineVariant = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOutlineVariant)).getColor(0, 0)),
            scrim = Color(red = 0, green = 0, blue = 0), // copied from PaletteTokens.kt (default in light and dark)
            surfaceBright = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceBright)).getColor(0, 0)),
            surfaceContainer = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceContainer)).getColor(0, 0)),
            surfaceContainerHigh = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceContainerHigh)).getColor(0, 0)),
            surfaceContainerHighest = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceContainerHighest)).getColor(0, 0)),
            surfaceContainerLow = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceContainerLow)).getColor(0, 0)),
            surfaceContainerLowest = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceContainerLowest)).getColor(0, 0)),
            surfaceDim = Color(context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorSurfaceDim)).getColor(0, 0)),)
    }

    @Composable
    private fun getTypography(context: Context): Typography {
        return if (themeClass != null && themeClass!!.composeTypography(context) != null) {
            themeClass!!.composeTypography(context)!!
        } else {
            MaterialTheme.typography
        }
    }

    @Composable
    private fun getShapes(context: Context): Shapes {
        return if (themeClass != null && themeClass!!.composeShapes(context) != null) {
            themeClass!!.composeShapes(context)!!
        } else {
            MaterialTheme.shapes
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
        MaterialTheme(colorScheme = getColourScheme(context), shapes = getShapes(context), typography = getTypography(context), content = content)
        Log.i(TAG, "jLib ThemeEngine Compose initialised")
    }

    @SuppressLint("PrivateResource")
    private fun isValidTheme(themeName: String, context: Context, theme: Int): Boolean {
        when (themeName) {
            "jLib" -> {
                val isDark = !isThemeBoolean(context, LIGHT_CHECK_ATTRS, theme) // jLib Theme is dark only
                val isExtendedFromJLib = isThemeBoolean(context, intArrayOf(R.attr.isJTheme), theme)
                return isDark && isExtendedFromJLib
            }
            "M3" -> {
                val manager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                val isDayNight =
                    isThemeBoolean(context, LIGHT_CHECK_ATTRS, theme) == (manager.nightMode != UiModeManager.MODE_NIGHT_YES) // let android determine whether it is dark or light (m3 supports light and dark)
                val isExtendedFromM3 = isThemeBoolean(context, intArrayOf(com.google.android.material.R.attr.isMaterial3Theme), theme)
                return isDayNight && isExtendedFromM3
            }
            else -> return false
        }
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
    fun getThemeFromThemeProvider(context: Activity): String {
        // get from ThemeProvider

        // creating a cursor object of the
        // content URI

        @SuppressLint("Recycle") val cursor = context.contentResolver.query(
            Uri.parse("content://io.github.dot166.themeengine.ThemeProvider/themes"),
            null,
            null,
            null,
            null
        )

        val allValues = HashMap<String?, String?>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                allValues.put(cursor.getString(0), cursor.getString(1))
            }
            tmpCurrentTheme = allValues.get("Theme").toString()
            if (tmpCurrentTheme == null.toString() || tmpCurrentTheme == "Disabled") {
                tmpCurrentTheme = "jLib"
            }

            if (allValues.containsKey("UpdateAvailable")) {
                // update check logic
                if (allValues.get("UpdateAvailable").toBoolean()) {
                    val builder = AlertDialog.Builder(context)

                    builder.setMessage(R.string.dialog_te_message)
                        .setTitle(R.string.text_te_label)
                    if (!Build.is_jOS || VersionUtils.isAtLeastBaklava()) { // ensures that ThemeEngine can be updated out of band (only non jOS and jOS Plasma (16) and above can do that)
                        builder.setPositiveButton(
                            R.string.dialog_te_positive,
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    val intent = jWebIntent(context)
                                    intent.setUrl("https://github.com/dot166/jOS_j-lib/releases/latest")
                                    intent.configureWebView(true, true)
                                    intent.launch()
                                }
                            })
                    }
                    builder.setNegativeButton(
                        R.string.dialog_te_negative,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        })

                    val dialog = builder.create()
                    dialog.show()
                    val handler = Handler()
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            dialog.dismiss()
                        }
                    }, 2000)
                }
            } else {
                Log.e(TAG, "Update Check Value not found in cursor")
            }

            if (allValues.containsKey("Theme")) {
                return allValues.get("Theme").toString()
            } else {
                Log.e(TAG, "Theme not found in cursor")
            }
        }
        Log.e(TAG, "No Records Found")
        return "none"
    }

    private fun isWebViewUsed(context: Context): Boolean {
        return if (context is jWebActivity) {
            context.useWebView;
        } else {
            true; // return true normally because the activity would not exiting 2 seconds after startup
        }
    }
}


