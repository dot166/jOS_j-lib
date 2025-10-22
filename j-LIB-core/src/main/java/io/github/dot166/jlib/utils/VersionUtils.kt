package io.github.dot166.jlib.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import io.github.dot166.jlib.internal.utils.LibVer

object VersionUtils {
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.O_MR1)
    val isAtLeastO_1: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.O_MR1

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.P)
    val isAtLeastP: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.P

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.Q)
    val isAtLeastQ: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.Q

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.R)
    val isAtLeastR: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.R

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S)
    val isAtLeastS: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S_V2)
    val isAtLeastS_1: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S_V2

    @JvmStatic
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.TIRAMISU)
    val isAtLeastT: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.TIRAMISU

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE)
    val isAtLeastU: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE

    @JvmStatic
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM)
    val isAtLeastV: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.BAKLAVA)
    val isAtLeastBaklava: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.BAKLAVA

    @JvmStatic
    val androidVersion: String?
        get() {
            if (isAtLeastT) {
                return Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY
            }
            return Build.VERSION.RELEASE
        }

    @JvmStatic
    val libVersion: String
        get() = LibVer.VER
}

