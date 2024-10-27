package jOS.Core.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.browser.customtabs.CustomTabsIntent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jOS.Core.R;
import jOS.Core.ThemeEngine.ThemeEngine;
import jOS.Core.jLIBCoreApp;

public class IconUtils {

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
