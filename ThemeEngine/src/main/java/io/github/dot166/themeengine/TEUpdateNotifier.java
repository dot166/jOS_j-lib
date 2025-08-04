package io.github.dot166.themeengine;

import static androidx.core.app.NotificationCompat.DecoratedCustomViewStyle.getTextsFromContentView;
import static io.github.dot166.themeengine.TEBroadcastReceiver.isInSystemImage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;

import com.android.wallpaper.widget.BottomActionBar;

import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.Notifier;
import io.github.dot166.jlib.utils.NetUtils;

public class TEUpdateNotifier extends Notifier {
    private final Context context;
    private final String notificationChannelId = "te_channel_id";
    private final String notificationChannelName = "ThemeEngine Updater";
    private final int notificationId = 404; // updater not found

    public TEUpdateNotifier(NotificationManager notificationManager, Context context) {
        super(notificationManager);
        this.context = context;
    }

    @Override
    public String getNotificationChannelId() {
        return notificationChannelId;
    }

    @Override
    public String getNotificationChannelName() {
        return notificationChannelName;
    }

    @Override
    public int getNotificationId() {
        return notificationId;
    }

    @Override
    public Notification buildNotification() {
        Uri webpage = Uri.parse("https://github.com/dot166/jOS_j-lib/releases/latest");
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .build();
        Intent intent2;
        if (isInSystemImage(context)) {
            intent2 = new Intent(context, NotifierActivity.class);
        } else {
            intent2 = intent.intent;
            intent2.setData(webpage);
        }
        return new NotificationCompat.Builder(context, getNotificationChannelId())
                .setContentTitle(getNotificationTitle())
                .setContentText(getNotificationMessage())
                .setSmallIcon(io.github.dot166.themeengine.R.drawable.ic_stat_name)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .build();
    }

    @Override
    public String getNotificationTitle() {
        return context.getString(R.string.text_te_label);
    }

    @Override
    public String getNotificationMessage() {
        return NetUtils.getDataRaw("https://raw.githubusercontent.com/dot166/jOS_j-lib/refs/heads/main/ver", context).replaceAll("\n", "") + context.getString(io.github.dot166.themeengine.R.string.is_now_available);
    }

    @Override
    public void showNotification() {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        StatusBarNotification[] notificationChanels = notificationManager.getActiveNotifications();
        String[] texts = new String[5000];
        for (int i = 0; i < notificationChanels.length; i++) {
            if (BottomActionBar.contains(texts, notificationChanels[i].getNotification().extras.getString(Notification.EXTRA_TEXT))) {
                continue;
            }
            Log.i("nm", notificationChanels[i].getNotification().extras.getString(Notification.EXTRA_TEXT));
            texts[i] = notificationChanels[i].getNotification().extras.getString(Notification.EXTRA_TEXT);
        }
        if (!BottomActionBar.contains(texts, getNotificationMessage())) {
            super.showNotification();
        }
    }
}

