package jOS.Core;

import static java.lang.Boolean.parseBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Objects;

public class ThemeEngine {

    /**
     * @deprecated This method has been deprecated because it is now private.
     */
    @Deprecated(since = "v3.0.9", forRemoval = true)
    public static final String KEY_THEME = "pref_theme";
    public static String currentTheme;

    /**
     * jOS ThemeEngine: get the theme
     * @param context context
     * @return theme
     */
    public static int getSystemTheme(Context context){
        String Theme;
        Theme = getThemeFromDB1(context);
        currentTheme = getThemeFromDB1(context);
        switch (Theme) {
            case "Holo":
                Log.i("jOS Theme Engine", "jOS.Core.R.style.jOS_Theme");
                return R.style.jOS_Theme;
            case "M3 Dark":
                Log.i("jOS Theme Engine", "com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar");
                return com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar;
            case "M3 Light":
                Log.i("jOS Theme Engine", "com.google.android.material.R.style.Theme_Material3_Light_NoActionBar");
                return com.google.android.material.R.style.Theme_Material3_Light_NoActionBar;
        }
        Log.i("jOS Theme Engine", "Unrecognised Theme '" + currentTheme + "'");
        return R.style.jOS_Theme;
    }

    @SuppressLint("Range")
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
    public static String getThemeFromDB1(Context context){
        // get from database

        // creating a cursor object of the
        // content URI
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://jOS.Core.ThemeEngine.database/themes"), null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (parseBoolean(cursor.getString(cursor.getColumnIndex("current")))) {
                    Log.i("jOS Theme Engine - DB1", cursor.getString(cursor.getColumnIndex("name")));
                    return cursor.getString(cursor.getColumnIndex("name"));
                } else {
                    cursor.moveToNext();
                }
            }
        }
        Log.i("jOS Theme Engine - DB1", "No Records Found");
        return "Holo";
    }


    public static void relaunch(Activity context) {
        if (!Objects.equals(currentTheme, getThemeFromDB1(context))) {
            Intent intent = context.getIntent();
            context.finish();
            context.startActivity(intent);
        }
    }
}
