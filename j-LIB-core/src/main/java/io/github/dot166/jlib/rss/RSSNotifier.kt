package io.github.dot166.jlib.rss

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.Notifier
import io.github.dot166.jlib.registry.RegistryHelper
import androidx.core.content.edit
import androidx.core.net.toUri

class RSSNotifier(notificationManager: NotificationManager, private val context: Context) :
    Notifier(notificationManager) {
    override val notificationChannelId: String = "rss_channel_id"
    override val notificationChannelName: String = "RSS Feed Notifications"
    override var notificationId: Int = Int.Companion.MIN_VALUE
        private set
    private var rssChannel: RssChannel? = null

    override fun buildNotification(): Notification? {
        val webpage = rssChannel!!.items[0].link?.toUri()
        val intent = CustomTabsIntent.Builder()
            .build()
        val intent2 = intent.intent
        intent2.setData(webpage)
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setSmallIcon(R.drawable.outline_rss_feed_24)
            .setAutoCancel(true) // dismiss on click
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    override val notificationTitle: String?
        get() = rssChannel!!.title + " - " + rssChannel!!.items[0].title

    override val notificationMessage: String?
        get() = rssChannel!!.items[0].description

    override fun showNotification() {
        val feeds = RegistryHelper.getFromRegistry(context)
        val rssUrls = arrayOfNulls<String>(feeds.size)
        for (i in feeds.indices) {
            rssUrls[i] = feeds[i]!!.url
        }
        Log.i("RSS", rssUrls.contentToString())
        for (i in rssUrls.indices) {
            notificationId = i
            if (rssUrls[i] != null) {
                rssChannel = (RSSViewModel()).fetchFeedWithoutViewModel(rssUrls[i]!!)
                if (rssChannel!!.description == "Something failed and to keep this jLib app running this is displayed, {EXCLUDE FROM NOTIFIER}") { // should only trigger on error handler
                    Log.e("RSS", "cannot display notification as RSS reader is in fallback mode")
                    return
                }
                val articles: List<RssItem> = rssChannel!!.items

                val savedRssUrl: String = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("lastRssUrl-" + rssUrls[i], "")!!
                if (savedRssUrl != articles[0].link) {
                    super.showNotification()
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString("lastRssUrl-" + rssUrls[i], articles[0].link)
                    } // use shared prefs to not annoy user, just hope nobody has a preference with savedRssCount as the key and uses the rss feature
                }
            }
        }
    }
}

