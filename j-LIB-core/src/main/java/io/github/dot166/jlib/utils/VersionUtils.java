package io.github.dot166.jlib.utils;

import android.os.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

public class VersionUtils {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.O_MR1)
    public static boolean isAtLeastO_1() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.O_MR1;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.P)
    public static boolean isAtLeastP() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.P;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.Q)
    public static boolean isAtLeastQ() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.Q;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.R)
    public static boolean isAtLeastR() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.R;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S)
    public static boolean isAtLeastS() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S_V2)
    public static boolean isAtLeastS_1() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S_V2;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.TIRAMISU)
    public static boolean isAtLeastT() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.TIRAMISU;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE)
    public static boolean isAtLeastU() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM)
    public static boolean isAtLeastV() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.BAKLAVA)
    public static boolean isAtLeastBaklava() {
        return Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.BAKLAVA;
    }

    public static String getAndroidVersion() {
        if (isAtLeastT()) {
            return Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
        }
        return Build.VERSION.RELEASE;
    }
}

