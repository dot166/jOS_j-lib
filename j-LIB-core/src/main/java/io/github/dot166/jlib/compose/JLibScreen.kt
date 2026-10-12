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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.dot166.jlib.app.LocalNavController
import io.github.dot166.jlib.app.jLibPreferenceStore

@Composable
fun JLibScreen(
    title: String,
    navigationIconVisible: Boolean = true,
    translucency: Translucency = Translucency.PREFERENCE,
    actions: @Composable RowScope.() -> Unit = {},
    bottomBar: (@Composable () -> Unit)? = {},
    fab: @Composable () -> Unit = {},
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    content: @Composable (PaddingWithTranslucency) -> Unit,
) {
    val navController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val prefs by jLibPreferenceStore
    val translucent = translucency != Translucency.FORCE_OFF && (translucency == Translucency.FORCE_ON || prefs.theme.translucent.asFlow().collectAsState().value)

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
                translucent = translucent,
                actions,
                scrollBehavior,
            ) },
        bottomBar = if (bottomBar != null) {
            {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface.conditionalCopy(translucent, alpha = 0.75f),
                ) {
                    bottomBar()
                }
            }
        } else {
            {}
        },
        floatingActionButton = fab,
    ) { innerPadding ->
        if (translucent) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    )
                    .florisVerticalScroll(),
            ) {
                Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))

                content(PaddingWithTranslucency(PaddingValues.Zero, true))

                Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
            }
        } else {
            content(PaddingWithTranslucency(innerPadding, false))
        }
    }
}

@Composable
fun JLibAppBar(
    title: String,
    navigationIcon: (@Composable () -> Unit)?,
    translucent: Boolean,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface.conditionalCopy(translucent, alpha = 0.75f),
        scrolledContainerColor = MaterialTheme.colorScheme.surface.conditionalCopy(translucent, alpha = 0.75f),
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
        modifier = Modifier.clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
    )
}