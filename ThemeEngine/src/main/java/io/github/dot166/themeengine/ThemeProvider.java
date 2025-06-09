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

        return prefs.getString("pref_theme", "jLib");
    }

    private Boolean checkForTEUpdate(@NonNull Context context) {
        String latest_ver = NetUtils.getDataRaw("https://raw.githubusercontent.com/dot166/jOS_j-lib/refs/heads/main/ver", context).replaceAll("\n", "");
        if (!latest_ver.isEmpty()) {
            String[] te_ver_str = VersionUtils.getLibVersion(context).split("\\.");
            String[] lib_ver_str = latest_ver.split("\\.");
            int[] te_ver = new int[te_ver_str.length];
            for (int i = 0; i < te_ver_str.length; i++) {
                te_ver[i] = Integer.parseInt(te_ver_str[i]);
            }
            int[] lib_ver = new int[lib_ver_str.length];
            for (int i = 0; i < lib_ver_str.length; i++) {
                lib_ver[i] = Integer.parseInt(lib_ver_str[i]);
            }

            if (lib_ver[0] > te_ver[0]) { // major version (e.g. 4)
                return true;
            } else if (lib_ver[1] > te_ver[1]) { // minor version (e.g. 1)
                return true;
            } else return lib_ver[2] > te_ver[2]; // patch (or shame) version (e.g. 6)
        } else {
            Log.e(TAG, "Unable to check for update");
            return false;
        }
    }
}
