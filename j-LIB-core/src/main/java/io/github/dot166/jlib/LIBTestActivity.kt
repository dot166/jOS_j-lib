package io.github.dot166.jlib

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import dev.patrickgold.jetpref.datastore.ui.Preference
import dev.patrickgold.jetpref.datastore.ui.SwitchPreference
import io.github.dot166.jlib.app.CoreActivity
import io.github.dot166.jlib.app.LocalNavController
import io.github.dot166.jlib.app.jLibPreferenceStore
import io.github.dot166.jlib.compose.JLibAppTheme
import io.github.dot166.jlib.compose.JLibScreen
import io.github.dot166.jlib.compose.conditional
import io.github.dot166.jlib.compose.preference.ScrollableScreenColumn
import io.github.dot166.jlib.compose.preference.SegmentedListColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LIBTestActivity : CoreActivity() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs by jLibPreferenceStore
        scope.launch {
            prefs.tests.testBool.asFlow().collect { testBoolean ->
                if (testBoolean) {
                    prefs.tests.test.set("TestPreference")
                } else {
                    prefs.tests.test.set("null")
                }
            }
        }
        setContent {
            JLibAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CompositionLocalProvider(
                        LocalNavController provides rememberNavController(),
                    ) {
                        Column(
                            modifier = Modifier
                                //.statusBarsPadding()
                                .navigationBarsPadding()
                                .conditional(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                    displayCutoutPadding()
                                }
                                .imePadding(),
                        ) {
                            JLibScreen(
                                title = stringResource(R.string.jlib),
                                navigationIconVisible = false,
                            ) { contentPadding ->
                                ScrollableScreenColumn(contentPadding) {
                                    SegmentedListColumn {
                                        val count = 2
                                        val testTitle = prefs.tests.test.asFlow().collectAsState().value
                                        SwitchPreference(
                                            pref = prefs.tests.testBool,
                                            shapes = ListItemDefaults.segmentedShapes(0, count),
                                            icon = Icons.Default.Language,
                                            title = testTitle,
                                        )
                                        Preference(
                                            shapes = ListItemDefaults.segmentedShapes(1, count),
                                            icon = Icons.Outlined.Palette,
                                            title = stringResource(R.string.devtools__test_crash_report__label),
                                            onClick = {
                                                throw Exception(
                                                    "GLaDOS: you have completed all available tests, you will now receive cake",
                                                    Exception("Success! The app crashed purposely to display this beautiful screen we all love :3")
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

