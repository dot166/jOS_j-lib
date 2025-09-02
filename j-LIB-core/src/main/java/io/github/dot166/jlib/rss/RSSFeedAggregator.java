package io.github.dot166.jlib.rss;

import static io.github.dot166.jlib.utils.DateUtils.convertDateToEpochSeconds;
import static io.github.dot166.jlib.utils.DateUtils.convertFromCommonFormats;

import com.prof18.rssparser.model.RssChannel;
import com.prof18.rssparser.model.RssItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class RSSFeedAggregator {

    static RssChannel buildAllFeeds(String[] rssUrls, RSSViewModel viewModel) {
        List<RssItem> items = new ArrayList<>();

        for (String url : rssUrls) {
            RssChannel channel = viewModel.fetchFeedWithoutViewModel(url);

            if (!channel.getItems().isEmpty()
                    && Objects.equals(channel.getItems().get(0).getTitle(), "Error Handler")
                    && Objects.equals(channel.getItems().get(0).getSourceName(), "jLib")) {
                break;
            }

            items.addAll(channel.getItems());
        }

        items.sort(Comparator.comparingLong(
                item -> convertDateToEpochSeconds(convertFromCommonFormats(((RssItem)item).getPubDate()))
        ).reversed());

        return new RssChannel("All Feeds", null, null, null, null, null, items, null, null);
    }
}
