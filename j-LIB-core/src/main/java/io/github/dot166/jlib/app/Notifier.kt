package io.github.dot166.jlib.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

abstract class Notifier(private val notificationManager: NotificationManager) {
    abstract val notificationChannelId: String?
    abstract val notificationChannelName: String?
    abstract val notificationId: Int

    open fun showNotification() {
        val channel = createNotificationChannel()
        notificationManager.createNotificationChannel(channel)
        val notification = buildNotification()
        notificationManager.notify(
            this.notificationId,
            notification
        )
    }

    @JvmOverloads
    fun createNotificationChannel(importance: Int = NotificationManager.IMPORTANCE_DEFAULT): NotificationChannel {
        return NotificationChannel(
            this.notificationChannelId,
            this.notificationChannelName,
            importance
        )
    }

    abstract fun buildNotification(): Notification?

    protected abstract val notificationTitle: String?

    protected abstract val notificationMessage: String?
}


