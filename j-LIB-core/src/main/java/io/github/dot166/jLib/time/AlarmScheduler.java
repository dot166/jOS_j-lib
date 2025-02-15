package io.github.dot166.jLib.time;

import android.app.PendingIntent;

public interface AlarmScheduler {
    PendingIntent createPendingIntent(ReminderItem reminderItem);

    void schedule(ReminderItem reminderItem);

    void cancel(ReminderItem reminderItem);
}
