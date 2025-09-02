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
        if (selection == null) {
            selection = "81"; // default to the last version to not send ThemeEngine its version number (81 is 4.2.7 in the new versioning system)
        } else if (selection == "4.3.0") {
            selection = "82";
        } else if (selection == "4.3.1") {
            selection = "83";
        } else if (selection == "4.3.2") {
            selection = "84";
        } else if (selection == "4.3.3") {
            selection = "85";
        } else if (selection == "4.4.0") {
            selection = "86";
        } else if (selection == "4.4.1") {
            selection = "86";
        } else if (selection == "4.4.2") {
            selection = "87";
        }
        //never mind the details of the query; we always just want to
        //return the same set of data
        return getConfig(Integer.parseInt(selection));
    }

    private Cursor getConfig(int libVersion)
    {
        //create a cursor from a predefined set of key/value pairs
        MatrixCursor mc = new MatrixCursor(new String[] {"key","value"}, 1);
        mc.addRow(new Object[] {"Theme", getTheme(getContext(), libVersion)});
        if (checkForTEUpdate(Objects.requireNonNull(getContext()))) {
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            TEUpdateNotifier teUpdateNotifier = new TEUpdateNotifier(notificationManager, getContext());
            teUpdateNotifier.showNotification();
        }
        return mc;
    }

    public static String getTheme(Context context, int libVersion)
    {
        //access your shared preference or whatever else you're using here
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context));

        return ensureCompatibleTheme(prefs.getString("pref_theme", "jLib"), libVersion);
    }

    private static String ensureCompatibleTheme(String themeFromPrefs, int libVersion) {
        Log.i(TAG, String.valueOf(libVersion));
        String theme;
        if (themeFromPrefs == "jLib-Classic" && !(libVersion >= 82)) {
            theme = "jLib"; // Classic theme IS the main theme on 4.2.27 and older
        } else {
            theme = themeFromPrefs;
        }

        return theme;
    }

    private Boolean checkForTEUpdate(@NonNull Context context) {
        String latest_ver = NetUtils.getDataRaw("https://raw.githubusercontent.com/dot166/jOS_j-lib/refs/heads/main/ver", context).replaceAll("\n", "");
        if (!latest_ver.isEmpty()) {
            return Integer.parseInt(latest_ver) > VersionUtils.getLibVersion();
        } else {
            Log.e(TAG, "Unable to check for update");
            return false;
        }
    }
}
