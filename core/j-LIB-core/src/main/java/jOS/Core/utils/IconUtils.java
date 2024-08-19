package jOS.Core.utils;

import static jOS.Core.utils.LabelUtils.getExternalActivityLabel;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public static Drawable getExternalActivityIcon(Context context, String IconPackPackageName, ComponentName componentName) throws XmlPullParserException, IOException, PackageManager.NameNotFoundException {
        Resources res = context.getResources();
        ArrayList<Icon> icons = ParseIconPack(context, IconPackPackageName, res);
        if (icons != null) {
            for (int i = 0; i < icons.size(); i++) {
                Log.i(TAG, icons.get(i).getComponentName().toString());
                if (icons.get(i).getComponentName().equals(componentName)) {
                    return icons.get(i).getImage();
                }
            }
        }
        return getExternalActivityDefaultIcon(context, componentName);
    }

    public static ArrayList<Icon> ParseIconPack(Context context, String IconPackPackageName, Resources res) throws XmlPullParserException, IOException, PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        int resId = res.getIdentifier("appfilter", "xml", IconPackPackageName);
        ArrayList<Icon> icons = new ArrayList<Icon>();
        if (resId != 0) {
            XmlResourceParser parseXml = pm.getXml(IconPackPackageName, resId, null);
            while (parseXml.next() != XmlPullParser.END_DOCUMENT) {
                if (parseXml.getEventType() == XmlPullParser.START_TAG) {
                    switch (parseXml.getName()) {
                        case "item":
                            addItem(parseXml, icons, context, pm, res, IconPackPackageName);
                            break;
                        case "calendar":
                            addCalendar(parseXml, icons, context, pm, res, IconPackPackageName);
                            break;
                    }
                }
            }
            return icons;
        }
        return null;
    }

    public static class Icon {
        private String title;
        private Drawable image;
        private ComponentName componentName;

        public Icon(String title, Drawable image, ComponentName componentName, Context context) {
            this.title = getExternalActivityLabel(title, context, componentName);
            this.image = image;
            this.componentName = componentName;
        }

        public String getTitle() {
            return title;
        }

        public Drawable getImage() {
            return image;
        }

        public ComponentName getComponentName() {
            return componentName;
        }
    }

    private static void addItem(XmlResourceParser parseXml, List<Icon> icons, Context context, PackageManager pm, Resources res, String IconPackPackageName) throws PackageManager.NameNotFoundException {
        String component = parseXml.getAttributeValue(null, "component");
        String drawable = parseXml.getAttributeValue(null, "drawable");
        String name = parseXml.getAttributeValue(null, "name");
        if (component != null && drawable != null) {
            if (name == null) {
                name = component;
            }
            Log.i(TAG, component);
            Log.i(TAG, drawable);
            Log.i(TAG, name);
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
                    icons.add(new Icon(name, pm.getDrawable(IconPackPackageName, res.getIdentifier(drawable, "drawable", IconPackPackageName), pm.getApplicationInfo(IconPackPackageName, 0)), componentName, context));
                }
            }
        }
    }

    private static void addCalendar(XmlResourceParser parseXml, List<Icon> icons, Context context, PackageManager pm, Resources res, String IconPackPackageName) throws PackageManager.NameNotFoundException {
        String component = parseXml.getAttributeValue(null, "component");
        String prefix = parseXml.getAttributeValue(null, "prefix");
        String name = parseXml.getAttributeValue(null, "name");
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(calendar.getTime());
        if (component != null && prefix != null) {
            if (name == null) {
                name = component;
            }
            Log.i(TAG, component);
            Log.i(TAG, prefix);
            Log.i(TAG, date);
            Log.i(TAG, name);
            ComponentName componentName = parseComponent(component);
            if (componentName != null) {
                calendars.add(componentName);
                icons.add(new Icon(name, pm.getDrawable(IconPackPackageName, res.getIdentifier(prefix + date, "drawable", IconPackPackageName), pm.getApplicationInfo(IconPackPackageName, 0)), componentName, context));
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
