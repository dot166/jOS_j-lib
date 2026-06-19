package io.github.dot166.jlib

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.prof18.rssparser.model.RssChannel


class RssFilter : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return (clazz == RssChannel::class.java)
    }

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return false
    }
}