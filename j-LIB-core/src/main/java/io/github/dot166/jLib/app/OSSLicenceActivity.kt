package io.github.dot166.jLib.app

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.dot166.jLib.ThemeEngine.ThemeEngine
import io.github.dot166.jLib.R
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.GetComposeTheme

class OSSLicenceActivity : jActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        configure(layoutId = R.layout.ossactivity, actionbar = true)
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            Log.e("ActionBar2", "no actionbar found")
        }
        findViewById<ComposeView>(R.id.my_composable)?.setContent {
            GetComposeTheme(context = this) {
                Surface {
                    LibrariesContainer(
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
