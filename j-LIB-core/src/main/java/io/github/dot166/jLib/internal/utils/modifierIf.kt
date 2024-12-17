package io.github.dot166.jLib.internal.utils

import androidx.annotation.RestrictTo
import androidx.compose.ui.Modifier

@RestrictTo(RestrictTo.Scope.LIBRARY)
inline fun Modifier.addIf(condition: Boolean, crossinline factory: Modifier.() -> Modifier): Modifier =
    if (condition) factory() else this

@RestrictTo(RestrictTo.Scope.LIBRARY)
inline fun <T> Modifier.addIfNotNull(value: T?, crossinline factory: Modifier.(T) -> Modifier): Modifier =
    if (value != null) factory(value) else this
