package jOS.Core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import jOS.Core.jSDKCoreApp;

public class IconUtils {

    public static Drawable getActivityIcon(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = ((jSDKCoreApp)context.getApplicationContext()).getCurrentActivity().getIntent();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

        if (resolveInfo != null) {
            resolveInfo.loadLabel(pm);
            return resolveInfo.loadIcon(pm);
        } else {
            return AppCompatResources.getDrawable(context, android.R.mipmap.sym_def_app_icon);
        }
    }
}
