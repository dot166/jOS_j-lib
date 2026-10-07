/*
 * Copyright (C) 2021-2025 The FlorisBoard Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.dot166.jlib.app.devtools

import io.github.dot166.jlib.app.devtools.Log.createTag
import io.github.dot166.jlib.app.devtools.Log.getStacktraceElement
import io.github.dot166.jlib.app.devtools.Log.isFloggingEnabled
import io.github.dot166.jlib.app.devtools.Log.log
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/** Type alias for a flog topic Integer. */
typealias LogTopic = UInt

/** Type alias for a flog level Integer. */
typealias LogLevel = UInt

/**
 * Logs an error message returned by [block] together with the automatically retrieved
 * calling class and method name either to the console or to a log file. The class name
 * is used for the tag, the method name prepended to the message.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Log.logTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Log.TOPIC_OTHER].
 * @param block The lambda expression to evaluate the message which is appended to the
 *  method name. Is called only if logging is enabled and the topics match. Must return
 *  a [String]. If this argument is omitted, only the calling method name will be used
 *  as the log message.
 */
inline fun logError(topic: LogTopic = Log.TOPIC_OTHER, block: () -> String = { "" }) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Log.checkShouldLog(topic, Log.LEVEL_ERROR)) {
        log(Log.LEVEL_ERROR, block())
    }
}

/**
 * Logs a warning message returned by [block] together with the automatically retrieved
 * calling class and method name either to the console or to a log file. The class name
 * is used for the tag, the method name prepended to the message.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Log.logTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Log.TOPIC_OTHER].
 * @param block The lambda expression to evaluate the message which is appended to the
 *  method name. Is called only if logging is enabled and the topics match. Must return
 *  a [String]. If this argument is omitted, only the calling method name will be used
 *  as the log message.
 */
inline fun logWarning(topic: LogTopic = Log.TOPIC_OTHER, block: () -> String = { "" }) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Log.checkShouldLog(topic, Log.LEVEL_WARNING)) {
        log(Log.LEVEL_WARNING, block())
    }
}

/**
 * Logs a info message returned by [block] together with the automatically retrieved
 * calling class and method name either to the console or to a log file. The class name
 * is used for the tag, the method name prepended to the message.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Log.logTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Log.TOPIC_OTHER].
 * @param block The lambda expression to evaluate the message which is appended to the
 *  method name. Is called only if logging is enabled and the topics match. Must return
 *  a [String]. If this argument is omitted, only the calling method name will be used
 *  as the log message.
 */
inline fun logInfo(topic: LogTopic = Log.TOPIC_OTHER, block: () -> String = { "" }) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Log.checkShouldLog(topic, Log.LEVEL_INFO)) {
        log(Log.LEVEL_INFO, block())
    }
}

/**
 * Logs a debug message returned by [block] together with the automatically retrieved
 * calling class and method name either to the console or to a log file. The class name
 * is used for the tag, the method name prepended to the message.
 *
 * This method automatically evaluates if logging is enabled and calls [block] only
 * if a log message should be generated.
 *
 * Optionally a [topic] can also be specified to allow to only partially enable
 * debug messages across the codebase. The passed [topic] is compared with the
 * currently active [Log.logTopics] variable and only if at least 1 topic match
 * is found, [block] will be called and a log message written.
 *
 * @param topic The topic of this message. To specify multiple topics, use the binary
 *  OR operator. Defaults to [Log.TOPIC_OTHER].
 * @param block The lambda expression to evaluate the message which is appended to the
 *  method name. Is called only if logging is enabled and the topics match. Must return
 *  a [String]. If this argument is omitted, only the calling method name will be used
 *  as the log message.
 */
inline fun logDebug(topic: LogTopic = Log.TOPIC_OTHER, block: () -> String = { "" }) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (Log.checkShouldLog(topic, Log.LEVEL_DEBUG)) {
        log(Log.LEVEL_DEBUG, block())
    }
}

/**
 * Helper function to evaluate if a bit flag is set in an integer value.
 *
 * @param flag The flag to check if it is set.
 *
 * @return True if the flag is set, false otherwise.
 */
private infix fun UInt.isSet(flag: UInt): Boolean {
    return (this and flag) == flag
}

/**
 * Main helper object for FlorisBoard logging (=Flog). Manages the enabled
 * state and the active topics. Provides relevant helper functions for the
 * flog methods to properly work.
 *
 * This helper object uses some parts of the Timber library to assist in
 * logging. In particular:
 *  - [createTag] (converted to Kotlin, renamed from "createStackElementTag",
 *     removed manual tagging).
 *  - [getStacktraceElement] (converted to Kotlin, renamed from "getTag",
 *     method now returns stack trace element).
 *  - [log] (converted to Kotlin).
 * Timber is licensed under the Apache 2.0 license, see the repo here:
 *  https://github.com/JakeWharton/timber
 */
// This is originally from florisboard, its license should be preserved
@Suppress("MemberVisibilityCanBePrivate")
object Log {
    const val TOPIC_NONE: LogTopic =               UInt.MIN_VALUE
    const val TOPIC_OTHER: LogTopic =              0x80000000u
    const val TOPIC_ALL: LogTopic =                UInt.MAX_VALUE

    const val LEVEL_NONE: LogLevel =               UInt.MIN_VALUE
    const val LEVEL_ERROR: LogLevel =              0x01u
    const val LEVEL_WARNING: LogLevel =            0x02u
    const val LEVEL_INFO: LogLevel =               0x04u
    const val LEVEL_DEBUG: LogLevel =              0x08u
    const val LEVEL_ALL: LogLevel =                UInt.MAX_VALUE

    /** The relevant call stack element is always on the 4th position, thus 4-1=3. */
    private const val CALL_STACK_INDEX: Int =       3

    /** The maximum log length limit. */
    private const val MAX_LOG_LENGTH: Int =         4000

    private var isFloggingEnabled: Boolean = false
    private var logTopics: LogTopic = TOPIC_NONE
    private var logLevels: LogLevel = LEVEL_NONE

    /**
     * Installs the flog utility  and sets the relevant
     * configuration variables based on the given config values.
     *
     * @param isFloggingEnabled If logging is enabled. If this value is false, all calls to
     *  the flog methods will be ignored and no logs will be written, regardless of the topics
     *  and levels set.
     * @param logTopics The enabled topics for this installation. Use [TOPIC_ALL] to enable
     *  all topics. If this value is [TOPIC_NONE], this essentially disables all logging.
     * @param logLevels The enabled levels for this installation. Use [LEVEL_ALL] to enable
     *  all levels. If this value is [LEVEL_NONE], this essentially disables all logging.
     */
    fun install(
        isFloggingEnabled: Boolean,
        logTopics: LogTopic,
        logLevels: LogLevel
    ) {
        this.isFloggingEnabled = isFloggingEnabled
        this.logTopics = logTopics
        this.logLevels = logLevels
    }

    /**
     * Checks if a log message should be evaluated by checking [isFloggingEnabled] and
     * by matching the given [topic] and [level] values with the configured settings.
     *
     * @param topic The topic(s) to check for.
     * @param level The level(s) to check for.
     *
     * @return True if a log message should be evaluated, false otherwise.
     */
    fun checkShouldLog(topic: LogTopic, level: LogLevel): Boolean {
        return isFloggingEnabled && (logTopics isSet topic) && (logLevels isSet level)
    }

    /**
     * Extract the tag which should be used for the message from the `element`.
     */
    private fun createTag(element: StackTraceElement): String {
        var tag = element.className
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        return tag
    }

    private fun createMessage(element: StackTraceElement, msg: String): String {
        return StringBuilder().run {
            append(element.methodName)
            append('(')
            append(')')
            if (msg.isNotBlank()) {
                append(' ')
                append('-')
                append(' ')
                append(msg)
            }
            toString()
        }
    }

    private fun getStacktraceElement(): StackTraceElement {
        val stackTrace = Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) {
            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
        }
        return stackTrace[CALL_STACK_INDEX]
    }

    fun log(level: LogLevel, msg: String) {
        if (msg.length < MAX_LOG_LENGTH) {
            androidLog(level, msg)
        } else {
            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length: Int = msg.length
            while (i < length) {
                var newline: Int = msg.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                    val part: String = msg.substring(i, end)
                    androidLog(level, part)
                    i = end
                } while (i < newline)
                i++
            }
        }
    }

    private fun androidLog(level: LogLevel, msg: String) {
        val ste = getStacktraceElement()
        val tag = createTag(ste)
        val message = createMessage(ste, msg)
        when {
            level isSet LEVEL_ERROR ->      android.util.Log.e(tag, message)
            level isSet LEVEL_WARNING ->    android.util.Log.w(tag, message)
            level isSet LEVEL_INFO ->       android.util.Log.i(tag, message)
            level isSet LEVEL_DEBUG ->      android.util.Log.d(tag, message)
        }
    }
}
