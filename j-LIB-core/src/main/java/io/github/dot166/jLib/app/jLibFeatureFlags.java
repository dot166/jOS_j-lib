package io.github.dot166.jLib.app;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class jLibFeatureFlags {

    public static boolean blink_enabled_default_value = true;
    public static int blink_speed_default_value = 500;
    static SharedPreferences prefs;

    private enum jLibFlags {
        blink_enabled,
        blink_speed,
    }

    public static int get_Blink_speed() {
        return prefs.getInt("blink_speed", blink_speed_default_value);
    }

    public static void set_Blink_speed(int value) {
        prefs.edit().putInt("blink_speed", value).apply();
    }

    public static boolean get_Blink_enabled() {
        return prefs.getBoolean("enable_blink", blink_enabled_default_value);
    }

    public static void set_Blink_enabled(boolean value) {
        prefs.edit().putBoolean("enable_blink", value).apply();
    }

    public static void init_values(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for(jLibFlags flag : jLibFlags.values()) {
            switch (flag) {
                case blink_enabled -> {
                    if (!prefs.contains("enable_blink")) {
                        prefs.edit().putBoolean("enable_blink", blink_enabled_default_value).apply();
                    }
                }
                case blink_speed -> {
                    if (!prefs.contains("blink_speed")) {
                        prefs.edit().putInt("blink_speed", blink_speed_default_value).apply();
                    }
                }
            }
        }
    }
}
