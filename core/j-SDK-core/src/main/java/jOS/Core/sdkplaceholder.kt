package jOS.Core

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class sdkplaceholder : jActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        configure(R.layout.sdkplaceholder, false)
        super.onCreate(savedInstanceState)
        alertdialog()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun alertdialog() {
        supportActionBar!!.subtitle = "AAAA"
        val text = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val android_sdk_test = "Android: " + androidver()
        val j_sdk_test = "jOS: " + Build.jOS_RELEASE
        val j_verify_test = "is j Device: " + Build.j_DEVICE()
        val all = "$android_sdk_test $j_sdk_test $j_verify_test"
        text.text = all
        button.setOnClickListener { finish() }
        button2.setOnClickListener {
            startActivity(
                Intent().setComponent(
                    ComponentName(
                        applicationInfo.packageName,
                        "jOS.Core.SDKChangelogActivity"
                    )
                )
            )
        }
    }

    private fun androidver(): String {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY
        }
        return android.os.Build.VERSION.RELEASE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_settings) {
            startActivity(
                Intent(Intent.ACTION_APPLICATION_PREFERENCES).setPackage(
                    packageName
                )
            )
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

