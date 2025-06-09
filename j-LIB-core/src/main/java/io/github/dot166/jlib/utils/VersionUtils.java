package io.github.dot166.jlib.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

import io.github.dot166.jlib.R;

public class VersionUtils {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    public static boolean isAtLeastO_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    public static boolean isAtLeastP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    public static boolean isAtLeastQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    public static boolean isAtLeastR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    public static boolean isAtLeastS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    public static boolean isAtLeastS_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    public static boolean isAtLeastT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public static boolean isAtLeastU() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    public static boolean isAtLeastV() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM;
    }

    // TODO: add check for Android Baklava once it has released
    // TODO: use the new SDK_INT_FULL variable for sdk checks once Android Baklava releases

    public static String getAndroidVersion() {
        if (isAtLeastT()) {
            return Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
        }
        return Build.VERSION.RELEASE;
    }

    public static String getLibVersion(Context context) {
        return context.getString(R.string.lib_ver);
    }
}

