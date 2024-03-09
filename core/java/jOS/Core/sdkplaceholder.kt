package jOS.Core;


import android.app.Activity
import android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import jOS.Core.Build.jOS_RELEASE
import jOS.Core.Build.j_DEVICE


class sdkplaceholder : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sdkplaceholder)
        Build.set_content(this)
        alertdialog()
    }

    fun alertdialog() {
        val text = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val android_sdk_test = "Android: $RELEASE_OR_PREVIEW_DISPLAY"
        val j_sdk_test = "jOS: $jOS_RELEASE"
        val j_verify_test = "is j Device: " + j_DEVICE()
        val all = "$android_sdk_test $j_sdk_test $j_verify_test"
        text.setText(all)
        button.setOnClickListener(View.OnClickListener { finish() })
    }
}
