/*
 * Copyright (C) 2026 The FlorisBoard Contributors
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

package io.github.dot166.jlib.compose.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.dot166.jlib.compose.ScreenHorizontalPadding
import io.github.dot166.jlib.compose.SegmentedListColumnVerticalPadding
import io.github.dot166.jlib.compose.florisVerticalScroll

// This is originally from florisboard, its license should be preserved
@Composable
fun ScrollableScreenColumn(
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .florisVerticalScroll()
            .padding(
                start = ScreenHorizontalPadding,
                end = ScreenHorizontalPadding,
                bottom = SegmentedListColumnVerticalPadding,
            ),
        content = content,
    )
}