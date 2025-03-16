package io.github.dot166.jlib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import io.github.dot166.jlib.app.jLIBCoreApp;

public class AppUtils {

    public static class Icon {

        public static Drawable getActivityIcon(Context context) {
            PackageManager pm = context.getPackageManager();

            Intent intent = ((jLIBCoreApp)context.getApplicationContext()).getCurrentActivity().getIntent();
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

            if (resolveInfo != null) {
                resolveInfo.loadLabel(pm);
                return resolveInfo.loadIcon(pm);
            } else {
                return AppCompatResources.getDrawable(context, android.R.mipmap.sym_def_app_icon);
            }
        }
    }

    public static class Label {

        public static String getActivityLabel(Context context) {
            PackageManager pm = context.getPackageManager();

            Intent intent = ((jLIBCoreApp)context.getApplicationContext()).getCurrentActivity().getIntent();
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

            if (resolveInfo != null) {
                resolveInfo.loadLabel(pm);
                return resolveInfo.loadLabel(pm).toString();
            } else {
                return "";
            }
        }

        public static String getAppLabel(Context context) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
        }
    }
}
