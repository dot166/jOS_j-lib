package jOS.Core.ThemeEngine;

import static java.lang.Boolean.parseBoolean;

import static jOS.Core.ThemeEngine.ThemeEngineKT.getDarkColourScheme;
import static jOS.Core.ThemeEngine.ThemeEngineKT.getLightColourScheme;

import android.annotation.SuppressLint;
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

import jOS.Core.R;

public class ThemeEngine {

    public static String currentTheme;
    static String TAG = "jOS Theme Engine";
    static String TAGDB1 = TAG + " - DB1";
    private static ColorScheme ColourScheme;
    public static values themeClass;
    private static final int[] JOS_CHECK_ATTRS = {R.attr.isJTheme};
    private static final int[] MATERIAL_CHECK_ATTRS = {com.google.android.material.R.attr.colorPrimaryVariant};
    private static final int[] LIGHT_CHECK_ATTRS = {androidx.appcompat.R.attr.isLightTheme};
    @SuppressLint("PrivateResource")
    private static final int[] MATERIAL3_CHECK_ATTRS = {com.google.android.material.R.attr.isMaterial3Theme};

    /**
     * Set the class that contains the theme values.
     * MUST BE SET IN THE onCreate() FUNCTION IN EITHER THE APPLICATION OR ALL OF THE EXPORTED ACTIVITIES
     */
    public static void setCustomThemeClass(values customThemeClass){
        themeClass = customThemeClass;
    }

    /**
     * jOS ThemeEngine: get the theme
     *
     * @param context context
     * @return theme
     */
    public static int getSystemTheme(Context context) {
        String Theme;
        Theme = getThemeFromDB1(context);
        currentTheme = getThemeFromDB1(context);
        Log.i(TAG, Theme);
        switch (Theme) {
            case "jOS":
                ColourScheme = getDarkColourScheme(context);
                if (themeClass != null && themeClass.jOSTheme() != 0 && isjOSTheme(context, themeClass.jOSTheme()) && !isLightTheme(context, themeClass.jOSTheme())) {
                    return themeClass.jOSTheme();
                }
                return R.style.jOS_Theme;
            case "M3 Dark":
                ColourScheme = getDarkColourScheme(context);
                if (themeClass != null && themeClass.M3() != 0 && isMaterial3Theme(context, themeClass.M3()) && !isLightTheme(context, themeClass.M3())) {
                    return themeClass.M3();
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Dark_NoActionBar;
            case "M3 Light":
                ColourScheme = getLightColourScheme(context);
                if (themeClass != null && themeClass.M3L() != 0 && isMaterial3Theme(context, themeClass.M3L()) && isLightTheme(context, themeClass.M3L())) {
                    return themeClass.M3L();
                }
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Light_NoActionBar;
            case "M2 Dark":
                ColourScheme = getDarkColourScheme(context);
                if (themeClass != null && themeClass.M2() != 0 && isMaterialTheme(context, themeClass.M2()) && !isLightTheme(context, themeClass.M2())) {
                    return themeClass.M2();
                }
                return com.google.android.material.R.style.Theme_MaterialComponents_NoActionBar;
            case "M2 Light":
                ColourScheme = getLightColourScheme(context);
                if (themeClass != null && themeClass.M2L() != 0 && isMaterialTheme(context, themeClass.M2L()) && isLightTheme(context, themeClass.M2L())) {
                    return themeClass.M2L();
                }
                return com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar;
            case "Disabled":
                ColourScheme = getDarkColourScheme(context);
                Log.i(TAG, "ThemeEngine Disabled, Returning no legacy scheme and dark compose colour scheme");
                return 0;
        }
        if (!Theme.equals("none")) {
            Log.i(TAG, "Unrecognised Theme '" + currentTheme + "'");
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!");
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
        ColourScheme = getDarkColourScheme(context);
        return R.style.jOS_Theme;
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
     * checks if theme is extended from jOSTheme
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from jOSTheme
     */
    public static boolean isjOSTheme(@NonNull Context context, int theme) {
        return isThemeBoolean(context, JOS_CHECK_ATTRS, theme);
    }

    /**
     * checks if theme is extended from ThemeMaterialComponents
     * @param context context for checking the theme
     * @param theme int theme to check
     * @return boolean true if theme is extended from ThemeMaterialComponents
     */
    public static boolean isMaterialTheme(@NonNull Context context, int theme) {
        return isTheme(context, MATERIAL_CHECK_ATTRS, theme);
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
     * checks if the theme has the attributes
     * @param context Context for getting attributes to check
     * @param themeAttributes int[] attributes to check
     * @param theme int theme id
     * @return boolean true if the theme has the attributes
     */
    public static boolean isTheme(@NonNull Context context, @NonNull int[] themeAttributes, int theme) {
        TypedArray a = context.obtainStyledAttributes(theme, themeAttributes);
        for (int i = 0; i < themeAttributes.length; i++) {
            if (!a.hasValue(i)) {
                a.recycle();
                return false;
            }
        }
        a.recycle();
        return true;
    }

    /**
     * gets the corresponding colour scheme to the theme and android version for jetpack compose
     * @return the colour scheme
     */
    public static ColorScheme getColourScheme() {
        return ColourScheme;
    }

    @SuppressLint("Range")
    public static String getThemeFromDB1(Context context) {
        // get from database

        // creating a cursor object of the
        // content URI
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://jOS.Core.ThemeEngine.database/themes"), null, null, null, null);

        if (cursor != null) {
            // iteration of the cursor
            // to find selected theme
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    if (parseBoolean(cursor.getString(cursor.getColumnIndex("current")))) {
                        Log.i(TAGDB1, cursor.getString(cursor.getColumnIndex("name")));
                        return cursor.getString(cursor.getColumnIndex("name"));
                    } else {
                        cursor.moveToNext();
                    }
                }
            }
        }
        Log.i(TAGDB1, "No Records Found");
        return "none";
    }

}
