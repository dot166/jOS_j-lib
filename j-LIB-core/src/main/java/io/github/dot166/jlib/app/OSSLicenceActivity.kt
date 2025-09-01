package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.dot166.jlib.R
import io.github.dot166.jlib.themeengine.ThemeEngine.GetComposeTheme

class OSSLicenceActivity : jActivity() {
    @SuppressLint("DiscouragedApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ossactivity)
        setSupportActionBar(findViewById(R.id.actionbar))
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            Log.e("ActionBar2", "no actionbar found")
        }
        findViewById<ComposeView>(R.id.my_composable)?.setContent {
            GetComposeTheme(context = this) {
                LibrariesList()
            }
        }
    }

    @Composable
    fun LibrariesList() {
        if (resources.getIdentifier("aboutlibraries", "raw", packageName) == 0) {
            val libraries by rememberLibraries(R.raw.aboutlibraries_jlib)
            LibrariesContainer(libraries, Modifier.fillMaxSize())
        } else {
            val libraries by rememberLibraries(resources.getIdentifier("aboutlibraries", "raw", packageName))
            LibrariesContainer(libraries, Modifier.fillMaxSize())
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
