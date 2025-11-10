package io.github.dot166.jlib

import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import io.github.dot166.jlib.app.jActivity
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
        val all = "Android: $androidVersion Lib: $libVersion"
        text.text = all
        button.setOnClickListener { v ->
            Toast.makeText(v.context, "Exiting...", Toast.LENGTH_SHORT).show()
            finish()
        }
        button2.setOnClickListener { v ->
            val webpage = ("https://github.com/dot166/jOS_j-lib/commits/v$libVersion").toUri()
            val intent = CustomTabsIntent.Builder()
                .build()
            intent.launchUrl(v.context, webpage)
        }
    }
}

