package io.github.dot166.jLib.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public abstract class Notifier {
    private final NotificationManager notificationManager;

    public Notifier(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public abstract String getNotificationChannelId();
    public abstract String getNotificationChannelName();
    public abstract int getNotificationId();

    public void showNotification() {
        NotificationChannel channel = createNotificationChannel();
        notificationManager.createNotificationChannel(channel);
        Notification notification = buildNotification();
        notificationManager.notify(
                getNotificationId(),
                notification
        );
    }

    public NotificationChannel createNotificationChannel(int importance) {
        return new NotificationChannel(
                getNotificationChannelId(),
                getNotificationChannelName(),
                importance
        );
    }

    public NotificationChannel createNotificationChannel() {
        return createNotificationChannel(NotificationManager.IMPORTANCE_DEFAULT);
    }

    public abstract Notification buildNotification();

    protected abstract String getNotificationTitle();

    protected abstract String getNotificationMessage();
}


