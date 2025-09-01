package io.github.dot166.jlib.rss

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import io.github.dot166.jlib.utils.DateUtils.convertDateToEpochSeconds
import io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class RSSViewModel : ViewModel() {
    private var articleListLive: MutableLiveData<RssChannel?>? = null

    private val snackbar = MutableLiveData<String?>()

    val channel: MutableLiveData<RssChannel?>
        get() {
            if (articleListLive == null) {
                articleListLive = MutableLiveData<RssChannel?>()
            }
            return articleListLive!!
        }

    fun getSnackbar(): LiveData<String?> {
        return snackbar
    }

    fun onSnackbarShowed() {
        snackbar.setValue(null)
    }

    fun fetchFeed(urlString: String, context: Context?) {
        viewModelScope.launch(Dispatchers.IO) {
            val parser = RssParserBuilder().build()
            val channel = parser.getRssChannel(urlString)
            this@RSSViewModel.channel.postValue(channel)
        }
    }

    fun loadAllFeedsConcurrently(urls: Array<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val parser = RssParserBuilder().build()
            val deferredChannels = urls.map { url ->
                async { parser.getRssChannel(url) }
            }
            val channels = deferredChannels.awaitAll()
            val allItems = channels.flatMap { it.items }
                .sortedByDescending { it.pubDate?.let { date -> convertDateToEpochSeconds(convertFromCommonFormats(date)) } }
            val combinedChannel = RssChannel("All Feeds", null, null, null, null, null, allItems, null, null)
            channel.postValue(combinedChannel)
        }
    }
}