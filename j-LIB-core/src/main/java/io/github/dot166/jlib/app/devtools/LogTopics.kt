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

/**
 * This object holds all custom log topics for the [Log] utility.
 *
 * _Contributors:_ if you add a new feature which is relatively large, you can
 * add a new topic here, just make sure it is a 2^n value and does not
 * exceed the maximum value of [LogTopic].
 */
@Suppress("MemberVisibilityCanBePrivate", "Unused")
object LogTopics {
    const val NONE: LogTopic =                 Log.TOPIC_NONE
    const val OTHER: LogTopic =                Log.TOPIC_OTHER
    const val ALL: LogTopic =                  Log.TOPIC_ALL
    const val CRASH_UTILITY: LogTopic =        2048u
}
