package io.github.dot166.jlib.rss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import io.github.dot166.jlib.time.AlarmScheduler;
import io.github.dot166.jlib.time.ReminderItem;

public class RSSAlarmScheduler implements AlarmScheduler {

    private Context context;

    public RSSAlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private AlarmManager alarmManager;

    @Override
    public PendingIntent createPendingIntent(ReminderItem reminderItem) {
        Intent intent = new Intent(context, RSSNotifAlarmReceiver.class);

        return PendingIntent.getBroadcast(context, reminderItem.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void schedule(ReminderItem reminderItem) {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderItem.time, AlarmManager.INTERVAL_HOUR, createPendingIntent(reminderItem));
    }

    @Override
    public void cancel(ReminderItem reminderItem) {
        alarmManager.cancel(createPendingIntent(reminderItem));
    }
}
