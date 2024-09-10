package jOS.Core;

import static java.lang.Boolean.parseBoolean;

import static jOS.Core.ThemeEngineKT.getDarkColourScheme;
import static jOS.Core.ThemeEngineKT.getLightColourScheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.compose.material3.ColorScheme;

public class ThemeEngine {

    public static String currentTheme;
    static AlertDialog.Builder builder;
    static String TAG = "jOS Theme Engine";
    static String TAGDB1 = TAG + " - DB1";
    private static ColorScheme ColourScheme;

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
        switch (Theme) {
            case "Holo":
                Log.i(TAG, "jOS.Core.R.style.jOS_Theme");
                ColourScheme = getDarkColourScheme(context);
                return R.style.jOS_Theme;
            case "M3 Dark":
                Log.i(TAG, "com.google.android.material.R.style.Theme_Material3_DynamicColors_Dark_NoActionBar");
                ColourScheme = getDarkColourScheme(context);
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Dark_NoActionBar;
            case "M3 Light":
                Log.i(TAG, "com.google.android.material.R.style.Theme_Material3_DynamicColors_Light_NoActionBar");
                ColourScheme = getLightColourScheme(context);
                return com.google.android.material.R.style.Theme_Material3_DynamicColors_Light_NoActionBar;
            case "M2 Dark":
                Log.i(TAG, "com.google.android.material.R.style.Theme_MaterialComponents_NoActionBar");
                ColourScheme = getDarkColourScheme(context);
                return com.google.android.material.R.style.Theme_MaterialComponents_NoActionBar;
            case "M2 Light":
                Log.i(TAG, "com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar");
                ColourScheme = getLightColourScheme(context);
                return com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar;
        }
        if (!Theme.equals("none")) {
            Log.i(TAG, "Unrecognised Theme '" + currentTheme + "'");
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!");
            missingThemeEngine(context);
        }
        ColourScheme = getDarkColourScheme(context);
        return R.style.jOS_Theme;
    }

    public static ColorScheme getColourScheme() {
        return ColourScheme;
    }

    private static void missingThemeEngine(Context context) {
        builder = new AlertDialog.Builder(context);

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

    @SuppressLint({"Range", "LongLogTag"})
    public static StringBuilder getAllThemes(jActivity context) {

        // creating a cursor object of the
        // content URI
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://jOS.Core.ThemeEngine.database/themes"), null, null, null, null);

        if (cursor != null) {
            // iteration of the cursor
            // to print whole table
            if (cursor.moveToFirst()) {
                StringBuilder strBuild = new StringBuilder();
                while (!cursor.isAfterLast()) {
                    Log.i("Theme Engine - Load Data", cursor.getString(cursor.getColumnIndex("id")) + "-" + cursor.getString(cursor.getColumnIndex("name")) + "-" + cursor.getString(cursor.getColumnIndex("current")));
                    strBuild.append("\n" + cursor.getString(cursor.getColumnIndex("id")) + "-" + cursor.getString(cursor.getColumnIndex("name")) + "-" + cursor.getString(cursor.getColumnIndex("current")));
                    cursor.moveToNext();
                }
                return strBuild;
            } else {
                return new StringBuilder(context.getString(R.string.no_records_found));
            }
        } else {
            return new StringBuilder(context.getString(R.string.no_records_found));
        }
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
