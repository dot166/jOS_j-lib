package io.github.dot166.jlib.internal.utils

import android.widget.Button
import android.widget.TextView
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import io.github.dot166.jlib.R
import io.github.dot166.jlib.internal.LIBTestBottomSheet
import io.github.dot166.jlib.jos.Build.jOS_RELEASE
import io.github.dot166.jlib.utils.VersionUtils.androidVersion
import io.github.dot166.jlib.utils.VersionUtils.libVersion

@RestrictTo(RestrictTo.Scope.LIBRARY)
object LIBTest {
    @JvmStatic
    fun Test(context: AppCompatActivity) {
        if (context.supportActionBar != null) {
            context.supportActionBar!!.subtitle = "flag_flag"
        }
        val text = context.findViewById<TextView>(R.id.textView)
        val button = context.findViewById<Button>(R.id.button)
        val button2 = context.findViewById<Button>(R.id.button2)
        val all = "Android: $androidVersion jOS: $jOS_RELEASE"
        text.text = all
        button.setOnClickListener {
            LIBTestBottomSheet().show(
                context.supportFragmentManager,
                LIBTestBottomSheet.TAG
            )
        }
        button2.setOnClickListener { v ->
            val webpage = ("https://github.com/dot166/jOS_j-lib/commits/$libVersion").toUri()
            val intent = CustomTabsIntent.Builder()
                .build()
            intent.launchUrl(v.context, webpage)
        }
    }
}
