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

    private static final String TAG = "IconUtils";
    public static boolean HideFallback = true;
    private static final ArrayList<ComponentName> calendars = new ArrayList<ComponentName>();

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

    public static Drawable getExternalActivityDefaultIcon(Context context, ComponentName componentName) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent();
        intent.setComponent(componentName);
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

        if (resolveInfo != null) {
            resolveInfo.loadLabel(pm);
            return resolveInfo.loadIcon(pm);
        } else {
            return AppCompatResources.getDrawable(context, android.R.mipmap.sym_def_app_icon);
        }
    }

    public static Drawable getExternalActivityIcon(Context context, ComponentName componentName) throws XmlPullParserException, IOException, PackageManager.NameNotFoundException {
        Resources res = context.getResources();
        Log.i(TAG, "Checking ThemeEngine");
        ArrayList<Icon> icons = ParseThemeEngine(context, res);
        if (icons != null) {
            for (int i = 0; i < icons.size(); i++) {
                Log.i(TAG, icons.get(i).getComponentName().toString());
                if (icons.get(i).getComponentName().equals(componentName)) {
                    return icons.get(i).getImage();
                }
            }
            Log.i(TAG, "icon is not in ThemeEngine");
        } else {
            Log.e(TAG, "ThemeEngine is MISSING!!!!");
        }
        return getExternalActivityDefaultIcon(context, componentName);
    }

    public static ArrayList<Icon> ParseThemeEngine(Context context, Resources res) throws XmlPullParserException, IOException, PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        int resId = res.getIdentifier("appfilter", "xml", "jOS.ThemeEngine");
        ArrayList<Icon> icons = new ArrayList<Icon>();
        if (resId != 0) {
            XmlResourceParser parseXml = pm.getXml("jOS.ThemeEngine", resId, null);
            while (parseXml.next() != XmlPullParser.END_DOCUMENT) {
                if (parseXml.getEventType() == XmlPullParser.START_TAG) {
                    switch (parseXml.getName()) {
                        case "item":
                            addItem(parseXml, icons, pm, res);
                            break;
                        case "calendar":
                            addCalendar(parseXml, icons, pm, res);
                            break;
                    }
                }
            }
            return icons;
        }
        return null;
    }

    public static class Icon {
        private Drawable image;
        private ComponentName componentName;

        public Icon(Drawable image, ComponentName componentName) {
            this.image = image;
            this.componentName = componentName;
        }

        public Drawable getImage() {
            return image;
        }

        public ComponentName getComponentName() {
            return componentName;
        }
    }

    private static void addItem(XmlResourceParser parseXml, List<Icon> icons, PackageManager pm, Resources res) throws PackageManager.NameNotFoundException {
        String component = parseXml.getAttributeValue(null, "component");
        String drawable = parseXml.getAttributeValue(null, "drawable");
        if (component != null && drawable != null) {
            Log.i(TAG, component);
            Log.i(TAG, drawable);
            boolean STOP = false;
            ComponentName componentName = parseComponent(component);
            if (componentName != null) {
                if (HideFallback) {
                    for (int i = 0; i < calendars.size(); i++) {
                        Log.i(TAG, calendars.get(i).toString());
                        if (calendars.get(i).equals(componentName)) {
                            Log.i(TAG, "STOP!! duplicate calendar icons found and fallbacks are disabled");
                            STOP = true;
                            break;
                        }
                    }
                }
                if (!STOP) {
                    icons.add(new Icon(pm.getDrawable("jOS.ThemeEngine", res.getIdentifier(drawable, "drawable", "jOS.ThemeEngine"), pm.getApplicationInfo("jOS.ThemeEngine", 0)), componentName));
                }
            }
        }
    }

    private static void addCalendar(XmlResourceParser parseXml, List<Icon> icons, PackageManager pm, Resources res) throws PackageManager.NameNotFoundException {
        String component = parseXml.getAttributeValue(null, "component");
        String prefix = parseXml.getAttributeValue(null, "prefix");
        String date = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1);
        if (component != null && prefix != null) {
            Log.i(TAG, component);
            Log.i(TAG, prefix);
            Log.i(TAG, date);
            ComponentName componentName = parseComponent(component);
            if (componentName != null) {
                calendars.add(componentName);
                icons.add(new Icon(pm.getDrawable("jOS.ThemeEngine", res.getIdentifier(prefix + date, "drawable", "jOS.ThemeEngine"), pm.getApplicationInfo("jOS.ThemeEngine", 0)), componentName));
            }
        }
    }

    private static ComponentName parseComponent(String component) {
        if (component.startsWith("ComponentInfo{") && component.endsWith("}")) {
            component = component.substring(14, component.length() - 1);
            return ComponentName.unflattenFromString(component);
        }
        return null;
    }
}
