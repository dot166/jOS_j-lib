package io.github.dot166.jlib.rss;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.prof18.rssparser.model.RssChannel;
import com.prof18.rssparser.model.RssItem;

import java.util.List;
import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.app.Notifier;
import io.github.dot166.jlib.web.jWebIntent;

public class RSSNotifier extends Notifier {
    private final Context context;
    private final String notificationChannelId = "rss_channel_id";
    private final String notificationChannelName = "RSS Feed Notifications";
    private final int notificationId = 200;
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
        jWebIntent webIntent = new jWebIntent(context);
        webIntent.setUrl(rssChannel.getItems().get(0).getLink());
        webIntent.configureWebView(true, true);
        return new NotificationCompat.Builder(context, getNotificationChannelId())
                .setContentTitle(getNotificationTitle())
                .setContentText(getNotificationMessage())
                .setSmallIcon(R.drawable.outline_rss_feed_24)
                .setContentIntent(PendingIntent.getActivity(context, 0, webIntent.getIntent(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
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
        String rssUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("rssUrl", "");
        Log.i("RSS", rssUrl);
        rssChannel = (new RSSViewModel()).fetchFeedWithoutViewModel(rssUrl, context);
        if (Objects.equals(rssChannel.getItems().get(0).getDescription(), "Something failed and to keep the app running this is displayed")) { // should only trigger on error handler
            Log.e("RSS", "cannot display notification as RSS reader is in fallback mode");
            return;
        }
        int rssCount = 0;
        List<RssItem> articles = rssChannel.getItems();
        rssCount = articles.size(); // should be safe to do this, there is no way on earth that anyone has Integer.MAX_VALUE rss items (2^31 - 1)
        Log.i("RSS", String.valueOf(rssCount));

        int savedRssCount = PreferenceManager.getDefaultSharedPreferences(context).getInt("savedRssCount", 0);
        if (savedRssCount < rssCount) {
            super.showNotification();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("savedRssCount", rssCount).apply(); // use shared prefs to not annoy user, just hope nobody has a preference with savedRssCount as the key and uses the rss feature
        }
    }
}

