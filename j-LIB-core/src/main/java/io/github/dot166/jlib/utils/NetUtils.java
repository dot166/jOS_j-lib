package io.github.dot166.jlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {

    public static String getDataRaw(String urlString, @NonNull Context context) {
        return getDataRaw(urlString, context, 2000);
    }

    public static String getDataRaw(String urlString, @NonNull Context context, int timeoutMs) {
        if (isNetworkAvailable(context)) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
                connection.setConnectTimeout(timeoutMs);
                InputStream in_stream = connection.getInputStream();
                return inputSteramToString(in_stream);
            } catch (Exception e) {
                ErrorUtils.handle(e, context);
                return "";
            }
        } else {
            return "";
        }
    }

    public static String inputSteramToString(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();
        return builder.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());

        if (capabilities!= null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return isDataEnabled(context);
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDataEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("is_data_enabled", false);
    }
}
