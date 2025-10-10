package io.github.dot166.themeengine;

import static io.github.dot166.jlib.app.jLIBCoreApp.TAG;

import android.app.NotificationManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.Objects;

import io.github.dot166.jlib.utils.NetUtils;
import io.github.dot166.jlib.utils.VersionUtils;

public class ThemeProvider extends ContentProvider
{

    public ThemeProvider() { }
    @Override public int delete(@NonNull Uri uri, String selection, String[] selectionArgs){ throw new UnsupportedOperationException("Not yet implemented"); }
    @Override public String getType(@NonNull Uri uri) { throw new UnsupportedOperationException("Not yet implemented"); }
    @Override public Uri insert(@NonNull Uri uri, ContentValues values) { throw new UnsupportedOperationException("Not yet implemented"); }
    @Override public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) { throw new UnsupportedOperationException("Not yet implemented"); }

    @Override public boolean onCreate() { return false; }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        //never mind the details of the query; we always just want to
        //return the same set of data
        MatrixCursor mc = new MatrixCursor(new String[] {"key","value"}, 1);
        mc.addRow(new Object[] {"Theme", "M3"}); // force M3, this is now a stub
        return mc;
    }
}
