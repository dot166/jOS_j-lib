package io.github.dot166.jlib.time;

import android.app.PendingIntent;

public interface AlarmScheduler {
    PendingIntent createPendingIntent(ReminderItem reminderItem);

    void schedule(ReminderItem reminderItem);

    void cancel(ReminderItem reminderItem);
}
