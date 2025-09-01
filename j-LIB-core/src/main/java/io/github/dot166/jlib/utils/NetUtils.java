package io.github.dot166.jlib.utils;

import android.content.Context;
import android.os.StrictMode;

import androidx.annotation.NonNull;

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
}
