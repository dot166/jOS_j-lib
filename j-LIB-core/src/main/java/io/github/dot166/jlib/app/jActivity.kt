package io.github.dot166.jlib.app

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.android.settingslib.collapsingtoolbar.EdgeToEdgeUtils

open class jActivity : AppCompatActivity {

    /**
     * Default constructor for jActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * [android.app.AppComponentFactory].
     */
    constructor() : super()

    /**
     * Alternate constructor that can be used to provide a default layout
     * that will be inflated as part of `super.onCreate(savedInstanceState)`.
     *
     *
     * This should generally be called from your constructor that takes no parameters,
     * as is required for API 27 and lower or when using the default
     * [android.app.AppComponentFactory].
     *
     * @see .jActivity
     */
    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        EdgeToEdgeUtils.enable(this)
        super.onCreate(savedInstanceState)
    }
}

