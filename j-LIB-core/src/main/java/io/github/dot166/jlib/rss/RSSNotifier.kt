package io.github.dot166.jlib.rss

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.Notifier
import androidx.core.content.edit
import com.prof18.rssparser.RssParserBuilder
import io.github.dot166.jlib.utils.ErrorUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future

class RSSNotifier(notificationManager: NotificationManager?, private val context: Context) :
    Notifier(notificationManager) {
    private val notificationChannelId = "rss_channel_id"
    private val notificationChannelName = "RSS Feed Notifications"
    private var notificationId = Int.Companion.MIN_VALUE
    private var rssChannel: RssChannel? = null

    override fun getNotificationChannelId(): String {
        return notificationChannelId
    }

    override fun getNotificationChannelName(): String {
        return notificationChannelName
    }

    override fun getNotificationId(): Int {
        return notificationId
    }

    override fun buildNotification(): Notification {
        val webpage = Uri.parse(rssChannel!!.items.get(0).link)
        val intent = CustomTabsIntent.Builder()
            .build()
        val intent2 = intent.intent
        intent2.setData(webpage)
        return NotificationCompat.Builder(context, getNotificationChannelId())
            .setContentTitle(getNotificationTitle())
            .setContentText(getNotificationMessage())
            .setSmallIcon(R.drawable.outline_rss_feed_24)
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

    public override fun getNotificationTitle(): String {
        return rssChannel!!.title + " - " + rssChannel!!.items.get(0).title
    }

    public override fun getNotificationMessage(): String? {
        return rssChannel!!.items.get(0).description
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun showNotification() {
        GlobalScope.future {
            val rssUrls: Array<String?> = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("rssUrls", "")!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            Log.i("RSS", rssUrls.contentToString())
            for (i in rssUrls.indices) {
                val parser = RssParserBuilder().build()
                notificationId = i
                try {
                    rssChannel = parser.getRssChannel(rssUrls[i]!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@future
                }
                val articles: List<RssItem?> = rssChannel!!.items

                val savedRssUrl: String = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("lastRssUrl-" + rssUrls[i], "")!!
                if (savedRssUrl != articles[0]!!.link) {
                    super.showNotification()
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString("lastRssUrl-" + rssUrls[i], articles[0]!!.link)
                    }
                }
            }
        }
    }
}

