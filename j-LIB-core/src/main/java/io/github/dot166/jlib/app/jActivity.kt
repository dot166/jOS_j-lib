package io.github.dot166.jlib.app

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes

@Deprecated(
    message = "Renamed to CoreActivity",
    replaceWith = ReplaceWith("CoreActivity"),
    level = DeprecationLevel.ERROR
)
open class jActivity : CoreActivity {
    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)
}

