package io.github.dot166.jlib.internal.rss

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

@OptIn(DelicateCoroutinesApi::class)
fun parseFeed(parser: RssParser, url: String): CompletableFuture<RssChannel> = GlobalScope.future {
    parser.getRssChannel(url)
}

@OptIn(DelicateCoroutinesApi::class)
fun parseFeedLocal(parser: RssParser, rssString: String): CompletableFuture<RssChannel> = GlobalScope.future {
    parser.parse(rssString)
}