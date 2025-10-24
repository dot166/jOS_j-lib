package io.github.dot166.jlib.rss

import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import io.github.dot166.jlib.utils.DateUtils.convertDateToEpochSeconds
import io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats

internal object RSSFeedAggregator {
    fun buildAllFeeds(rssUrls: Array<String?>, viewModel: RSSViewModel): RssChannel {
        val items: MutableList<RssItem> = ArrayList()

        for (url in rssUrls) {
            if (url != null) {
                val channel = viewModel.fetchFeedWithoutViewModel(url)

                if (!channel.items.isEmpty() && channel.items[0].title == "Error Handler"
                    && channel.items[0].sourceName == "jLib"
                ) {
                    break
                }

                items.addAll(channel.items)
            }
        }

        items.sortWith(
            Comparator.comparingLong<Any?> { item: Any? ->
                convertDateToEpochSeconds(
                    convertFromCommonFormats((item as RssItem).pubDate)
                )
            }.reversed())

        return RssChannel("All Feeds", null, null, null, null, null,
            items as List<RssItem>, null, null)
    }
}
