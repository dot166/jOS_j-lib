package io.github.dot166.jLib.jOS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.dot166.jLib.utils.VersionUtils;

public class Build {

    private static String getInternalRelease() {
        Date date = new Date(android.os.Build.TIME);
        String formattedDate = new SimpleDateFormat("yyyyMMdd", Locale.UK).format(date) + "00";
        if (formattedDate.equals(android.os.Build.DISPLAY) && android.os.Build.USER.equals("jos")) {
            return VersionUtils.getAndroidVersion() + "." + android.os.Build.DISPLAY;
        }
        return "0";
    }


    /** An Integer utilized to distinguish jOS versions */
    public static final double jOS_RELEASE = Double.parseDouble(getInternalRelease());
}
