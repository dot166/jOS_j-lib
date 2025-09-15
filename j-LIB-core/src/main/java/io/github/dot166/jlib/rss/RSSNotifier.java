package io.github.dot166.jlib.rss;

import static io.github.dot166.jlib.registry.RegistryHelper.getFromRegistry;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.prof18.rssparser.model.RssChannel;
import com.prof18.rssparser.model.RssItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.Notifier;
import io.github.dot166.jlib.registry.RegistryHelper;
import io.github.dot166.jlib.time.ReminderItem;

public class RSSNotifier extends Notifier {
    private final Context context;
    private final String notificationChannelId = "rss_channel_id";
    private final String notificationChannelName = "RSS Feed Notifications";
    private int notificationId = Integer.MIN_VALUE;
    private RssChannel rssChannel;

    public RSSNotifier(NotificationManager notificationManager, Context context) {
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
        Uri webpage = Uri.parse(rssChannel.getItems().get(0).getLink());
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .build();
        Intent intent2 = intent.intent;
        intent2.setData(webpage);
        return new NotificationCompat.Builder(context, getNotificationChannelId())
                .setContentTitle(getNotificationTitle())
                .setContentText(getNotificationMessage())
                .setSmallIcon(R.drawable.outline_rss_feed_24)
                .setAutoCancel(true) // dismiss on click
                .setContentIntent(PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .build();
    }

    @Override
    public String getNotificationTitle() {
        return rssChannel.getTitle() + " - " + rssChannel.getItems().get(0).getTitle();
    }

    @Override
    public String getNotificationMessage() {
        return rssChannel.getItems().get(0).getDescription();
    }

    @Override
    public void showNotification() {
        List<RegistryHelper.Object> feeds = getFromRegistry(context);
        String[] rssUrls = new String[feeds.size()];
        for (int i = 0; i < feeds.size(); i++) {
            rssUrls[i] = feeds.get(i).getUrl();
        }
        Log.i("RSS", Arrays.toString(rssUrls));
        for (int i = 0; i < rssUrls.length; i++) {
            notificationId = i;
            rssChannel = (new RSSViewModel()).fetchFeedWithoutViewModel(rssUrls[i]);
            if (Objects.equals(rssChannel.getDescription(), "Something failed and to keep this jLib app running this is displayed, {EXCLUDE FROM NOTIFIER}")) { // should only trigger on error handler
                Log.e("RSS", "cannot display notification as RSS reader is in fallback mode");
                return;
            }
            List<RssItem> articles = rssChannel.getItems();

            String savedRssUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("lastRssUrl-" + rssUrls[i], "");
            if (!savedRssUrl.equals(articles.get(0).getLink())) {
                super.showNotification();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("lastRssUrl-" + rssUrls[i], articles.get(0).getLink()).apply(); // use shared prefs to not annoy user, just hope nobody has a preference with savedRssCount as the key and uses the rss feature
            }
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            ReminderItem reminderItem = new ReminderItem(cal.getTimeInMillis(), 1);
            new RSSAlarmScheduler(context).schedule(reminderItem);
        }
    }
}

