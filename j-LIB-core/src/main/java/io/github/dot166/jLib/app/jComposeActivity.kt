package io.github.dot166.jLib.app

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import io.github.dot166.jLib.ThemeEngine.ThemeEngine.GetComposeTheme

open class jComposeActivity : jActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        configure() // compose overrides view content (i think)
        super.onCreate(savedInstanceState)
        setContent {
            GetComposeTheme(context = this) {
                Surface {
                    MyComposeContent(this)
                }
            }
        }
    }

    @Composable
    fun MyComposeContent(context: Context) {}
}
