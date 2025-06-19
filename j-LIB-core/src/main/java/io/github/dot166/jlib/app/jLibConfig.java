package io.github.dot166.jlib.app;

import android.content.Context;

import io.github.dot166.jlib.utils.NetUtils;

/// @deprecated please use {@link NetUtils}.isDataEnabled(context) instead, since that is the only function left that works
@Deprecated(since = "4.2.13", forRemoval = true)
public class jLibConfig {

    /// @deprecated just check against false (that is what this value is)
    @Deprecated(since = "4.2.13", forRemoval = true)
    public static boolean data_enabled_default_value = false;

    /// @deprecated please use {@link NetUtils}.isDataEnabled(context) instead
    @Deprecated(since = "4.2.13", forRemoval = true)
    public static boolean isDataEnabled() {
        return NetUtils.isDataEnabled(jLIBCoreApp.getInstance());
    }

    @Deprecated(since = "4.2.13", forRemoval = true)
    public static void init_values(Context context) {
    }
}
