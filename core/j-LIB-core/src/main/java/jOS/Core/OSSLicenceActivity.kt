package jOS.Core

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import jOS.Core.ThemeEngine.ThemeEngine

class OSSLicenceActivity : jActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        configure(R.layout.ossactivity, false)
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            Log.e("ActionBar2", "no actionbar found")
        }
        findViewById<ComposeView>(R.id.my_composable).setContent {
            MaterialTheme(
                colorScheme = ThemeEngine.getColourScheme()
            ) {
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
