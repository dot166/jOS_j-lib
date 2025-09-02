package io.github.dot166.jlib.rss;

import static io.github.dot166.jlib.internal.rss.CoroutineBridgeKt.parseFeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof18.rssparser.RssParser;
import com.prof18.rssparser.RssParserBuilder;
import com.prof18.rssparser.model.RssChannel;
import com.prof18.rssparser.model.RssItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RSSViewModel extends ViewModel {

    private MutableLiveData<RssChannel> articleListLive = null;

    private MutableLiveData<String> snackbar = new MutableLiveData<>();

    public MutableLiveData<RssChannel> getChannel() {
        if (articleListLive == null) {
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    public void setChannel(RssChannel channel) {
        this.articleListLive.postValue(channel);
    }

    public LiveData<String> getSnackbar() {
        return snackbar;
    }

    public void onSnackbarShowed() {
        snackbar.setValue(null);
    }

    public void fetchFeed(String urlString) {
        RssChannel channel = fetchFeedWithoutViewModel(urlString);
        setChannel(channel);
    }

    public RssChannel fetchFeedWithoutViewModel(String urlString) {
        RssParser parser = new RssParserBuilder().build();
        try {
            return parseFeed(parser, urlString).get();
        } catch (Exception e) {
            List<RssItem> list = new ArrayList<>();
            list.add(new RssItem(null, "Error Handler", null, null, null,
                    e.getMessage(), e.getMessage() + Arrays.toString(e.getStackTrace()).replace(", ", "\n"), null, null, null, "jLib", null,
                    Collections.emptyList(), null, null, null, null));
            return new RssChannel("Error Handler", null, null, null, null, null,
                    list, null, null);
        }
    }
}