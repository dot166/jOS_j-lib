package io.github.dot166.jlib.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// This is originally from florisboard, its license should be preserved
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier,
): Modifier =
    if (condition) then(modifier(Modifier)) else this