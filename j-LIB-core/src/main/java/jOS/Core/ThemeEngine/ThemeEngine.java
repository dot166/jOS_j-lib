package jOS.Core.ThemeEngine;

import static java.lang.Boolean.parseBoolean;

import static io.github.dot166.jLib.internal.ThemeEngineKT.getDarkColourScheme;
import static io.github.dot166.jLib.internal.ThemeEngineKT.getDayNightColourScheme;
import static io.github.dot166.jLib.internal.ThemeEngineKT.getLightColourScheme;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.compose.material3.ColorScheme;

import io.github.dot166.jLib.R;
import jOS.Core.jWebActivity;

public class ThemeEngine {

    public static String currentTheme;
    static String TAG = "jLib Theme Engine";
    static String TAGDB = TAG + " - DB";
    private static ColorScheme ColourScheme;
    public static values themeClass;
    private static final int[] JLIB_CHECK_ATTRS = {R.attr.isJTheme};
    private static final int[] LIGHT_CHECK_ATTRS = {androidx.appcompat.R.attr.isLightTheme};
    @SuppressLint("PrivateResource")
    private static final int[] MATERIAL3_CHECK_ATTRS = {com.google.android.material.R.attr.isMaterial3Theme};

    /**
     * Set the class that contains the theme values.
     * MUST BE SET IN THE onCreate() FUNCTION IN EITHER THE APPLICATION CLASS OR ALL OF THE EXPORTED ACTIVITIES
     */
    public static void setCustomThemeClass(values customThemeClass){
        themeClass = customThemeClass;
    }

    /**
     * jOS ThemeEngine: get the theme
     *
     * @param context context
     * @return theme, will return 0 if ThemeEngine is disabled
     */
    public static int getSystemTheme(Context context) {
        String Theme;
        Theme = getThemeFromDB(context);
        currentTheme = getThemeFromDB(context);
        UiModeManager manager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        boolean isDarkMode = manager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
        Log.i(TAG, Theme);
        switch (Theme) {
            case "jLib":
                ColourScheme = getDarkColourScheme(context);
                if (themeClass != null && themeClass.jLibTheme() != 0 && isjLibTheme(context, themeClass.jLibTheme()) && !isLightTheme(context, themeClass.jLibTheme())) {
                    return themeClass.jLibTheme();
                }
                return R.style.j_Theme;
            case "M3 Dark":
                ColourScheme = getDarkColourScheme(context);
                if (themeClass != null && themeClass.M3D() != 0 && isMaterial3Theme(context, themeClass.M3D()) && !isLightTheme(context, themeClass.M3D())) {
                    return themeClass.M3D();
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Dark_NoActionBar;
            case "M3":
                ColourScheme = getDayNightColourScheme(context, isDarkMode);
                if (themeClass != null && themeClass.M3() != 0 && isMaterial3Theme(context, themeClass.M3()) && isDayNightTheme(context, themeClass.M3(), isDarkMode)) {
                    return themeClass.M3();
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight_NoActionBar;
            case "M3 Light":
                ColourScheme = getLightColourScheme(context);
                if (themeClass != null && themeClass.M3L() != 0 && isMaterial3Theme(context, themeClass.M3L()) && isLightTheme(context, themeClass.M3L())) {
                    return themeClass.M3L();
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Light_NoActionBar;
            case "Disabled":
                ColourScheme = getDayNightColourScheme(context, isDarkMode);
                Log.i(TAG, "ThemeEngine Disabled, Returning no legacy scheme and the DayNight compose colour scheme");
                return 0;
        }
        if (!Theme.equals("none")) {
            Log.i(TAG, "Unrecognised Theme '" + currentTheme + "'");
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!");
            if (!(context instanceof jWebActivity)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = "https://github.com/dot166/jOS_ThemeEngine/releases";
                                CustomTabsIntent intent = new CustomTabsIntent.Builder()
                                        .build();
                                intent.launchUrl(context, Uri.parse(url));
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i(TAG, "IGNORING ThemeEngine ERROR");
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        ColourScheme = getDarkColourScheme(context);
        return R.style.j_Theme;
    }

    /**
     * checks if theme is DayNight or not
     * @param context context for getting value of lightTheme attribute
     * @param theme int theme to check
     * @param dark boolean darkmode value
     * @return boolean true if theme is a light theme
     */
    public static boolean isDayNightTheme(@NonNull Context context, int theme, boolean dark) {
        if (dark) {
            return !isLightTheme(context, theme);
        } else {
            return isLightTheme(context, theme);
        }
    }

    /**
     * checks if theme is light or not
     * @param context context for getting value of lightTheme attribute
     * @param theme int theme to check
     * @return boolean true if theme is a light theme
     */
    public static boolean isLightTheme(@NonNull Context context, int theme) {
        return isThemeBoolean(context, LIGHT_CHECK_ATTRS, theme);
    }

    /**
     * checks if theme is extended from jTheme
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from jTheme
     */
    public static boolean isjLibTheme(@NonNull Context context, int theme) {
        return isThemeBoolean(context, JLIB_CHECK_ATTRS, theme);
    }

    /**
     * checks if theme is extended from ThemeMaterial3
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from ThemeMaterial3
     */
    public static boolean isMaterial3Theme(@NonNull Context context, int theme) {
        return isThemeBoolean(context, MATERIAL3_CHECK_ATTRS, theme);
    }

    /**
     * checks if the theme has the attributes and returns value of boolean attribute at index 0
     * @param context Context for getting attributes to check
     * @param themeAttributes int[] attributes to check
     * @param theme int theme id
     * @return boolean value of checked attribute at index 0
     */
    public static boolean isThemeBoolean(@NonNull Context context, @NonNull int[] themeAttributes, int theme) {
        TypedArray array = context.getTheme().obtainStyledAttributes(theme, themeAttributes);
        boolean value = array.getBoolean(0, false);
        array.recycle();
        return value;
    }

    /**
     * gets the corresponding colour scheme to the theme and android version for jetpack compose
     * @return the colour scheme
     */
    public static ColorScheme getColourScheme() {
        return ColourScheme;
    }

    @SuppressLint("Range")
    public static String getThemeFromDB(Context context) {
        // get from database

        // creating a cursor object of the
        // content URI
        @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(Uri.parse("content://jOS.Core.ThemeEngine.database/themes"), null, null, null, null);

        if (cursor != null) {
            // iteration of the cursor
            // to find selected theme
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    if (parseBoolean(cursor.getString(cursor.getColumnIndex("current")))) {
                        Log.i(TAGDB, cursor.getString(cursor.getColumnIndex("name")));
                        return cursor.getString(cursor.getColumnIndex("name"));
                    } else {
                        cursor.moveToNext();
                    }
                }
            }
        }
        Log.i(TAGDB, "No Records Found");
        return "none";
    }

}
