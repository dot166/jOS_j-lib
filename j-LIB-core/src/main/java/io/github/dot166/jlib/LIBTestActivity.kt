package io.github.dot166.jlib

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.settingslib.spa.framework.theme.SettingsTheme
import io.github.dot166.jlib.app.CoreActivity
import io.github.dot166.jlib.compose.ErrorDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class LIBTestActivity : CoreActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsTheme {
                var error by remember { mutableStateOf<Throwable?>(null) }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(stringResource(R.string.idiot))
                }
                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1.5.seconds)
                        try {
                            throw Exception(
                                "GLaDOS: you have completed all available tests, you will now receive cake",
                                Exception("Class no longer exists in $version")
                            )
                        } catch (e: Throwable) {
                            error = e
                        }
                    }
                }
                ErrorDialog(error = error) {
                    error = null
                    finish()
                }
            }
        }
    }
}

