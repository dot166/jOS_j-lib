package io.github.dot166.jlib.rss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class RSSNotifAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RSS", "received");
        if (context != null && PreferenceManager.getDefaultSharedPreferences(context).contains("rssUrl") && !PreferenceManager.getDefaultSharedPreferences(context).getString("rssUrl", "").isEmpty()) {
            Log.i("RSS", "notif");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            RSSNotifier rssNotifier = new RSSNotifier(notificationManager, context);
            rssNotifier.showNotification();
        }
    }
}