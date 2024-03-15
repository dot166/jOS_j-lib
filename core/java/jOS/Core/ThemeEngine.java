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
    public static final String KEY_THEME = "pref_theme";
    public static String currentTheme;

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

    /**
     * @deprecated This method has been deprecated in favor of using
     * {@link #getThemeFromDB1(Context)}.
     */
    @Deprecated(since = "v3.0.8", forRemoval = true)
    public static String getSystemThemeValue(Context context, String applicationID){
        return getThemeFromDB1(context);
    }


    public static void relaunch(Activity context) {
        if (!Objects.equals(currentTheme, getThemeFromDB1(context))) {
            Intent intent = context.getIntent();
            context.finish();
            context.startActivity(intent);
        }
    }
}
