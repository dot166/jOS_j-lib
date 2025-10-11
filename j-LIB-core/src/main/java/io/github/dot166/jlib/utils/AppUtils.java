package io.github.dot166.jlib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

public class AppUtils {

    public static Drawable getAppIcon(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int iconId = applicationInfo.icon;
        return iconId == 0 ? AppCompatResources.getDrawable(context, android.R.drawable.sym_def_app_icon) : AppCompatResources.getDrawable(context, iconId);
    }

    public static String getAppLabel(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
