package io.github.dot166.jlib.jos;

import static io.github.dot166.jlib.app.jLIBCoreApp.TAG;

import android.util.Log;

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

    private static String getCodeNameInternal() {
        String version = jOS_RELEASE.split("-")[0].replace("-", "").split("\\.")[0].replace(".", "");
        switch (version) {
            case "0" -> {
                Log.e(TAG, "Device is not running jOS, returning '0'");
                return "0";
            }
            case "15" -> {
                return "Obsidian";
            }
            case "16" -> {
                return "Plasma";
            }
            default -> {
                Log.i(TAG, "this version of jLib is too old for this jOS version, quick return the jOS Version");
                return version;
            }
        }
    }

    public static String jOS_CODENAME = getCodeNameInternal();

    private static String getShortCodeNameInternal() {
        if (isNumber(jOS_CODENAME)) {
            // must have a number, just return it
            return jOS_CODENAME;
        } else {
            return jOS_CODENAME.substring(0, 1);
        }
    }

    private static boolean isNumber(String str) {
        if (str.length() == 1) {
            return str.matches("[0-9]");
        } else if (str.length() == 2) {
            return str.matches("[0-9][0-9]");
        } else if (str.length() == 3) {
            return str.matches("[0-9][0-9][0-9]");
        } else {
            return false; // not bothering for any larger numbers just yet
        }
    }

    public static String jOS_CODENAME_SHORT = getShortCodeNameInternal();
}
