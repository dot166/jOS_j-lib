package io.github.dot166.jlib.jos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.dot166.jlib.utils.VersionUtils;

public class Build {

    private static String getInternalRelease() {
        Date date = new Date(android.os.Build.TIME);
        String formattedDate = new SimpleDateFormat("yMMdd", Locale.UK).format(date) + "j";
        if (formattedDate.equals(android.os.Build.DISPLAY)) {
            return VersionUtils.getAndroidVersion() + "-" + android.os.Build.DISPLAY.replace("j", "");
        }
        return "0";
    }


    /** A String utilized to distinguish jOS versions */
    public static final String jOS_RELEASE = getInternalRelease();
    /** A boolean value utilized to determine if the device is running jOS */
    public static final boolean is_jOS = !jOS_RELEASE.equals("0");
}
