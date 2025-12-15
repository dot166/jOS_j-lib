package io.github.dot166.jlib.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import io.github.dot166.jlib.version

@Deprecated("VersionUtils is no longer used")
object VersionUtils {

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.R)
    val isAtLeastR: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.R

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S)
    val isAtLeastS: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.S_V2)
    val isAtLeastS_1: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.S_V2

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.TIRAMISU)
    val isAtLeastT: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.TIRAMISU

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE)
    val isAtLeastU: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.UPSIDE_DOWN_CAKE

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM)
    val isAtLeastV: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.VANILLA_ICE_CREAM

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to check this")
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES_FULL.BAKLAVA)
    val isAtLeastBaklava: Boolean
        get() = Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.BAKLAVA

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use the AndroidSDK to get the android version")
    val androidVersion: String?
        get() {
            if (isAtLeastT) {
                return Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY
            }
            return Build.VERSION.RELEASE
        }

    @JvmStatic
    @Deprecated("VersionUtils is no longer used, please use io.github.dot166.jlib.version for jLib version")
    val libVersion: String
        get() = version
}

