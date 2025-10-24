package io.github.dot166.jlib.rss

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.github.dot166.jlib.time.AlarmScheduler
import io.github.dot166.jlib.time.ReminderItem

class RSSAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createPendingIntent(reminderItem: ReminderItem): PendingIntent {
        val intent = Intent(context, RSSNotifAlarmReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            reminderItem.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun schedule(reminderItem: ReminderItem) {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            reminderItem.time,
            AlarmManager.INTERVAL_HOUR,
            createPendingIntent(reminderItem)
        )
    }

    override fun cancel(reminderItem: ReminderItem) {
        alarmManager.cancel(createPendingIntent(reminderItem))
    }
}
