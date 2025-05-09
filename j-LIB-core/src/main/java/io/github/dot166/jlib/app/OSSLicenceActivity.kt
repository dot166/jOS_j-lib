package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.util.withContext
import com.mikepenz.aboutlibraries.util.withJson
import io.github.dot166.jlib.R
import io.github.dot166.jlib.themeengine.ThemeEngine.GetComposeTheme

class OSSLicenceActivity : jActivity() {
    @SuppressLint("DiscouragedApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ossactivity)
        setSupportActionBar(findViewById<Toolbar?>(R.id.actionbar))
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            Log.e("ActionBar2", "no actionbar found")
        }
        val librariesBlock: (Context) -> Libs =
            if (resources.getIdentifier("aboutlibraries", "raw", packageName) == 0) {
            { context ->
                Libs.Builder().withJson(context, R.raw.aboutlibraries_jlib).build()
            }
        } else {
            { context ->
                Libs.Builder().withContext(context).build()
            }
        }
        findViewById<ComposeView>(R.id.my_composable)?.setContent {
            GetComposeTheme(context = this) {
                Surface {
                    LibrariesContainer(
                        modifier = Modifier.fillMaxSize(),
                        librariesBlock = librariesBlock
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
