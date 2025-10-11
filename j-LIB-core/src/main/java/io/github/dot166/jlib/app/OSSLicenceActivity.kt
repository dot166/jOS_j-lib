package io.github.dot166.jlib.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import io.github.dot166.jlib.R
import io.github.dot166.jlib.utils.ThemeUtils.jLibComposeTheme

class OSSLicenceActivity : jActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("DiscouragedApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            jLibComposeTheme(context = this) {
                Scaffold(
                    topBar = {
                        TopAppBar({
                            Text(title.toString()) },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                        "backIcon"
                                    )
                                }
                            }
                        )
                    },
                    content = { contentPadding ->
                        Surface(Modifier.padding(top = contentPadding.calculateTopPadding())) {
                            LibrariesList()
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun LibrariesList() {
        if (resources.getIdentifier("aboutlibraries", "raw", packageName) == 0) {
            val libraries by produceLibraries(R.raw.aboutlibraries_jlib)
            LibrariesContainer(libraries, Modifier.fillMaxSize())
        } else {
            val libraries by produceLibraries(resources.getIdentifier("aboutlibraries", "raw", packageName))
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
