package io.github.dot166.jlib.rss

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RSSNotifAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("RSS", "received")
        if (context != null) {
            Log.i("RSS", "notif")
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val rssNotifier = RSSNotifier(notificationManager, context)
            rssNotifier.showNotification()
        }
    }
}