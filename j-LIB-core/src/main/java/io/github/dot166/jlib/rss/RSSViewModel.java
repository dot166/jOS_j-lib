package io.github.dot166.jlib.rss;

import static io.github.dot166.jlib.internal.rss.CoroutineBridgeKt.parseFeed;
import static io.github.dot166.jlib.internal.rss.CoroutineBridgeKt.parseFeedLocal;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof18.rssparser.RssParser;
import com.prof18.rssparser.RssParserBuilder;
import com.prof18.rssparser.model.RssChannel;

import io.github.dot166.jlib.utils.ErrorUtils;

public class RSSViewModel extends ViewModel {

    private MutableLiveData<RssChannel> articleListLive = null;

    private MutableLiveData<String> snackbar = new MutableLiveData<>();

    public MutableLiveData<RssChannel> getChannel() {
        if (articleListLive == null) {
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    private void setChannel(RssChannel channel) {
        this.articleListLive.postValue(channel);
    }

    public LiveData<String> getSnackbar() {
        return snackbar;
    }

    public void onSnackbarShowed() {
        snackbar.setValue(null);
    }

    public void fetchFeed(String urlString, Context context) {
        RssChannel channel = fetchFeedWithoutViewModel(urlString, context);
        setChannel(channel);
    }

    public RssChannel fetchFeedWithoutViewModel(String urlString, Context context) {
        RssParser parser = new RssParserBuilder().build();
        try {
            return parseFeed(parser, urlString).get();
        } catch (Exception e) {
            ErrorUtils.handle(e, context);
            try {
                return parseFeedLocal(parser, """
                        <rss version="2.0">
                        <channel>
                        <title>RSSActivity</title>
                        <item>
                        <title>
                        Crash Handler
                        </title>
                        <description>Something failed and to keep the app running this is displayed</description>
                        <pubDate>1970-01-01T00:00:00+00:00</pubDate>
                        </item>
                        </channel>
                        </rss>""").get();
            } catch (Exception e1) {
                ErrorUtils.handle(e1, context);
                return null; // this will be good
            }
        }
    }
}