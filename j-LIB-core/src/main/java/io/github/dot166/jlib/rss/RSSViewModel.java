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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RSSViewModel extends ViewModel {

    private final Map<String, MutableLiveData<RssChannel>> channelMap = new ConcurrentHashMap<>();
    private final MutableLiveData<String> snackbar = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public LiveData<RssChannel> getChannel(String url) {
        return channelMap.computeIfAbsent(url, u -> new MutableLiveData<>());
    }

    private void setChannel(String url, RssChannel channel) {
        channelMap.computeIfAbsent(url, u -> new MutableLiveData<>()).postValue(channel);
    }

    public LiveData<String> getSnackbar() {
        return snackbar;
    }

    public void onSnackbarShowed() {
        snackbar.setValue(null);
    }

    public void fetchFeedAsync(String url) {
        executor.execute(() -> {
            RssChannel result = fetchFeedWithoutViewModel(url);
            setChannel(url, result);
        });
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
            return new RssChannel("Error Handler", null, "Something failed and to keep this jLib app running this is displayed, {EXCLUDE FROM NOTIFIER}", null, null, null,
                    list, null, null);
        }
    }

    public void fetchAllFeedsAsync(String[] urls) {
        executor.execute(() -> {
            List<RssChannel> channels = new ArrayList<>();

            for (String url : urls) {
                RssChannel result = fetchFeedWithoutViewModel(url);
                channels.add(result);
                setChannel(url, result);
            }

            RssChannel aggregated = RSSFeedAggregator.buildAllFeeds(urls, this);
            setChannel("ALL", aggregated);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}