/*
 * Copyright 2021, Lawnchair
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jOS.Core.utils

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun PreferenceTemplate(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit = {},
    startWidget: (@Composable () -> Unit)? = null,
    endWidget: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    applyPaddings: Boolean = true,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 16.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    val contentAlphaDisabled = ContentAlpha.disabled
    Column {
        Row(
            verticalAlignment = verticalAlignment,
            modifier = modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .addIf(applyPaddings) {
                    padding(horizontal = horizontalPadding, vertical = verticalPadding)
                }
        ) {
            startWidget?.let {
                startWidget()
                if (applyPaddings) {
                    Spacer(modifier = Modifier.requiredWidth(16.dp))
                }
            }
            Row(
                modifier = contentModifier
                    .weight(1f)
                    .addIf(!enabled) {
                        alpha(contentAlphaDisabled)
                    },
                verticalAlignment = verticalAlignment
            ) {
                Column(Modifier.weight(1f)) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
                        LocalTextStyle provides MaterialTheme.typography.bodyLarge
                    ) {
                        title()
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium),
                        LocalTextStyle provides MaterialTheme.typography.bodyMedium
                    ) {
                        description()
                    }
                }
            }
            endWidget?.let {
                if (applyPaddings) {
                    Spacer(modifier = Modifier.requiredWidth(16.dp))
                }
                endWidget()
            }
        }
    }
}

@Composable
fun ContributorRow(name: String, description: String, photoUrl: String, url: String) {
    val context = LocalContext.current

    PreferenceTemplate(
        title = { Text(text = name) },
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = {
                    val webpage = Uri.parse(url)
                    val intent = CustomTabsIntent.Builder()
                        .build()
                    intent.launchUrl(context, webpage)
                    }
            ),
        description = { Text(text = description) },
        startWidget = {
            SubcomposeAsyncImage(
                model = photoUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Green)
                    )
                }
            )
        },
    )
}
