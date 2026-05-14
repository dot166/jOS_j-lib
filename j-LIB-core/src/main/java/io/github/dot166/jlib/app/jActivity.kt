package io.github.dot166.jlib.app

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Display
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.android.settingslib.collapsingtoolbar.EdgeToEdgeUtils
import com.google.android.material.color.DynamicColors

open class jActivity : AppCompatActivity {

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        EdgeToEdgeUtils.enable(this)
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
    }

    override fun startActivity(intent: Intent) {
        val options = ActivityOptions.makeBasic()
        options.setLaunchDisplayId(Display.DEFAULT_DISPLAY) // Forces it to the main screen
        startActivity(intent, options.toBundle())
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        val options = ActivityOptions.makeBasic()
        options.setLaunchDisplayId(Display.DEFAULT_DISPLAY) // Forces it to the main screen
        startActivityForResult(intent, requestCode, options.toBundle())
    }
}

