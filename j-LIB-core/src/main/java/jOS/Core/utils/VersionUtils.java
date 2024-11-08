package jOS.Core.utils;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

import jOS.Core.Build;

import androidx.annotation.ChecksSdkIntAtLeast;

public class VersionUtils {
    public static class Android {

        @ChecksSdkIntAtLeast(api = VERSION_CODES.N_MR1)
        public static boolean isAtLeastN_1() {
            return VERSION.SDK_INT >= VERSION_CODES.N_MR1;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.O)
        public static boolean isAtLeastO() {
            return VERSION.SDK_INT >= VERSION_CODES.O;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.O_MR1)
        public static boolean isAtLeastO_1() {
            return VERSION.SDK_INT >= VERSION_CODES.O_MR1;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.P)
        public static boolean isAtLeastP() {
            return VERSION.SDK_INT >= VERSION_CODES.P;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.Q)
        public static boolean isAtLeastQ() {
            return VERSION.SDK_INT >= VERSION_CODES.Q;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.R)
        public static boolean isAtLeastR() {
            return VERSION.SDK_INT >= VERSION_CODES.R;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.S)
        public static boolean isAtLeastS() {
            return VERSION.SDK_INT >= VERSION_CODES.S;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.S_V2)
        public static boolean isAtLeastS_1() {
            return VERSION.SDK_INT >= VERSION_CODES.S_V2;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.TIRAMISU)
        public static boolean isAtLeastT() {
            return VERSION.SDK_INT >= VERSION_CODES.TIRAMISU;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.UPSIDE_DOWN_CAKE)
        public static boolean isAtLeastU() {
            return VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE;
        }

        @ChecksSdkIntAtLeast(api = VERSION_CODES.VANILLA_ICE_CREAM)
        public static boolean isAtLeastV() {
            return VERSION.SDK_INT >= VERSION_CODES.VANILLA_ICE_CREAM;
        }
    }

    public static class jOS {

        public static boolean isAtLeastObsidian() {
            return Build.jOS_RELEASE_INT() >= Build.jOS_O;
        }
    }
}

