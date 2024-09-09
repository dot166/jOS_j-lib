package jOS.Core

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import jOS.Core.utils.ErrorUtils

class OSSLicenceActivity : jActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        configure(R.layout.ossactivity, false)
        super.onCreate(savedInstanceState)
        try {
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
        } catch (e: Exception) {
            ErrorUtils.handle(e, this);
            findViewById<ComposeView>(R.id.my_composable).setContent {
                MaterialTheme(
                    colorScheme = ThemeEngine.getColourScheme()
                ) {
                    Surface {
                        Text(stringResource(R.string.no_aboutlibraries_json_file_found_in_this_app_please_contact_the_app_developer))
                    }
                }
            }
        }
    }
}
