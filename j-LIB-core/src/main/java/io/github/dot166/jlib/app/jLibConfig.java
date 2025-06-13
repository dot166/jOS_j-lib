package io.github.dot166.jlib.app;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class jLibConfig {

    public static boolean data_enabled_default_value = false;
    static SharedPreferences prefs;

    private enum jLibConfigValues {
        data_enabled,
    }

    public static boolean isDataEnabled() {
        return prefs.getBoolean("is_data_enabled", data_enabled_default_value);
    }

    public static void init_values(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for(jLibConfigValues flag : jLibConfigValues.values()) {
            switch (flag) {
                case data_enabled -> {
                    if (!prefs.contains("is_data_enabled")) {
                        prefs.edit().putBoolean("is_data_enabled", data_enabled_default_value).apply();
                    }
                }
            }
        }
    }
}
