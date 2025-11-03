package io.github.dot166.jlib

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import io.github.dot166.jlib.app.jActivity
import io.github.dot166.jlib.internal.LIBTestBottomSheet
import io.github.dot166.jlib.jos.Build.jOS_RELEASE
import io.github.dot166.jlib.utils.AppUtils.getAppIcon
import io.github.dot166.jlib.utils.ErrorUtils.handle
import io.github.dot166.jlib.utils.VersionUtils.androidVersion
import io.github.dot166.jlib.utils.VersionUtils.libVersion

class LIBTestActivity : jActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.libplaceholder)
        setSupportActionBar(findViewById(R.id.actionbar))
        supportActionBar!!.subtitle = "flag_flag"
        val text = findViewById<MaterialTextView>(R.id.textView)
        val button = findViewById<MaterialButton>(R.id.button)
        val button2 = findViewById<MaterialButton>(R.id.button2)
        val all = "Android: $androidVersion jOS: $jOS_RELEASE"
        text.text = all
        button.setOnClickListener {
            LIBTestBottomSheet().show(
                supportFragmentManager,
                LIBTestBottomSheet.TAG
            )
        }
        button2.setOnClickListener { v ->
            val webpage = ("https://github.com/dot166/jOS_j-lib/commits/v$libVersion").toUri()
            val intent = CustomTabsIntent.Builder()
                .build()
            intent.launchUrl(v.context, webpage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.lib_test_menu, menu)
        menu.findItem(R.id.action_favorite).icon = getAppIcon(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_settings) {
            try {
                startActivity(
                    Intent(Intent.ACTION_APPLICATION_PREFERENCES).setPackage(
                        packageName
                    )
                )
            } catch (e: Exception) {
                handle(e, this)
            }
            return true
        } else if (itemId == R.id.action_favorite) {
            try {
                startActivity(Intent(this, LIBAboutActivity::class.java))
            } catch (e: Exception) {
                handle(e, this)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

