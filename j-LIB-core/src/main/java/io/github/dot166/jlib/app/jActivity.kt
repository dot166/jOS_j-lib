package io.github.dot166.jlib.app

import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dot166.jlib.utils.VersionUtils.isAtLeastV

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
        super.onCreate(savedInstanceState)

        if (isAtLeastV) {
            // Fix A15 EdgeToEdge
            ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content)
            ) { v: View?, windowInsets: WindowInsetsCompat? ->
                val insets = windowInsets!!.getInsets(
                    (WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
                            or WindowInsetsCompat.Type.displayCutout())
                )
                val statusBarHeight = window.decorView.getRootWindowInsets()
                    .getInsets(WindowInsetsCompat.Type.statusBars()).top
                // Apply the insets paddings to the view.
                v!!.setPadding(insets.left, statusBarHeight, insets.right, insets.bottom)
                WindowInsetsCompat.CONSUMED
            }
        }
    }
}

