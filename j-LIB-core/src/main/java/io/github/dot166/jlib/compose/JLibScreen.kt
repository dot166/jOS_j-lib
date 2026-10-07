/*
 * Copyright (C) 2021-2026 The FlorisBoard Contributors
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

package io.github.dot166.jlib.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.dot166.jlib.app.LocalNavController

typealias JLibScreenActions = @Composable RowScope.() -> Unit
typealias JLibScreenBottomBar = @Composable () -> Unit
typealias JLibScreenFab = @Composable () -> Unit

@Composable
fun JLibScreen(
    title: String,
    navigationIconVisible: Boolean = true,
    actions: JLibScreenActions = {},
    bottomBar: JLibScreenBottomBar = {},
    fab: JLibScreenFab = {},
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            JLibAppBar(
                title,
                (@Composable {
                    FilledTonalIconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        content = { Icon(navigationIcon, null) },
                    )
                }).takeIf { navigationIconVisible },
                actions,
                scrollBehavior,
            ) },
        bottomBar = bottomBar,
        floatingActionButton = fab,
        content = content,
    )
}

@Composable
fun JLibAppBar(
    title: String,
    navigationIcon: (@Composable () -> Unit)?,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
    ),
) {
    MediumFlexibleTopAppBar(
        navigationIcon = navigationIcon ?: {},
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        actions = { Row(Modifier.padding(end = 8.dp), content = actions) },
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}