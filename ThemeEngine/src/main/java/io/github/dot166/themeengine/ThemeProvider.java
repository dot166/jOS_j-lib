package io.github.dot166.themeengine;

import static io.github.dot166.themeengine.TEBroadcastReceiver.isInSystemImage;
import static io.github.dot166.jlib.app.jLIBCoreApp.TAG;

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

import java.util.Objects;

import io.github.dot166.jlib.BuildConfig;
import io.github.dot166.jlib.utils.NetUtils;

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
        return getConfig();
    }

    private Cursor getConfig()
    {
        //create a cursor from a predefined set of key/value pairs
        MatrixCursor mc = new MatrixCursor(new String[] {"key","value"}, 3);
        mc.addRow(new Object[] {"Theme", getTheme(getContext())});
        mc.addRow(new Object[] {"UpdateAvailable", checkForTEUpdate(Objects.requireNonNull(getContext()))});
        mc.addRow(new Object[] {"isInSystem", isInSystemImage(Objects.requireNonNull(getContext()))});
        return mc;
    }

    public static String getTheme(Context context)
    {
        //access your shared preference or whatever else you're using here
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context));

        if (prefs.getBoolean("pref_enableThemeEngine", true)) { // this check is only here for the legacy ui, once that is removed the disabled setting will be in the preference normally so this would not be needed
            return prefs.getString("pref_theme", "jLib");
        } else {
            return "Disabled";
        }
    }

    private Boolean checkForTEUpdate(@NonNull Context context) {
        String latest_ver = NetUtils.getDataRaw("https://raw.githubusercontent.com/dot166/jOS_j-lib/refs/heads/main/ver", context).replaceAll("\n", "");
        if (!latest_ver.isEmpty()) {
            return !BuildConfig.LIBVersion.equals(latest_ver);
        } else {
            Log.e(TAG, "Unable to check for update");
            return false;
        }
    }
}
