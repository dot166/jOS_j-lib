package io.github.dot166.jlib.rss

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import io.github.dot166.jlib.internal.rss.parseFeed
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RSSViewModel : ViewModel() {
    private val channelMap: MutableMap<String?, MutableLiveData<RssChannel?>> =
        ConcurrentHashMap<String?, MutableLiveData<RssChannel?>>()
    private val snackbar = MutableLiveData<String?>()
    private val executor: ExecutorService = Executors.newFixedThreadPool(4)

    fun getChannel(url: String?): LiveData<RssChannel?> {
        return channelMap.computeIfAbsent(url) { u: String? -> MutableLiveData<RssChannel?>() }
    }

    private fun setChannel(url: String?, channel: RssChannel?) {
        channelMap.computeIfAbsent(url) { u: String? -> MutableLiveData<RssChannel?>() }
            .postValue(channel)
    }

    fun getSnackbar(): LiveData<String?> {
        return snackbar
    }

    fun onSnackbarShowed() {
        snackbar.value = null
    }

    fun fetchFeedAsync(url: String) {
        executor.execute {
            val result = fetchFeedWithoutViewModel(url)
            setChannel(url, result)
        }
    }

    fun fetchFeedWithoutViewModel(urlString: String): RssChannel {
        val parser = RssParserBuilder().build()
        try {
            return parseFeed(parser, urlString).get()
        } catch (e: Exception) {
            val list: MutableList<RssItem> = ArrayList()
            list.add(
                RssItem(
                    null,
                    "Error Handler",
                    null,
                    null,
                    null,
                    e.message,
                    e.message + e.stackTrace.contentToString().replace(", ", "\n"),
                    null,
                    null,
                    null,
                    "jLib",
                    null,
                    mutableListOf(),
                    null,
                    null,
                    null,
                    null
                )
            )
            return RssChannel(
                "Error Handler",
                null,
                "Something failed and to keep this jLib app running this is displayed, {EXCLUDE FROM NOTIFIER}",
                null,
                null,
                null,
                list,
                null,
                null
            )
        }
    }

    fun fetchAllFeedsAsync(urls: Array<String?>) {
        executor.execute {
            val channels: MutableList<RssChannel?> = ArrayList()
            for (url in urls) {
                if (url != null) {
                    val result = fetchFeedWithoutViewModel(url)
                    channels.add(result)
                    setChannel(url, result)
                }
            }

            val aggregated = RSSFeedAggregator.buildAllFeeds(urls, this)
            setChannel("ALL", aggregated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor.shutdownNow()
    }
}