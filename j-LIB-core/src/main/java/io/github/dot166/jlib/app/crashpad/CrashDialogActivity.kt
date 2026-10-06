package io.github.dot166.jlib.app.crashpad

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.LocalNavController
import io.github.dot166.jlib.app.devtools.Devtools
import io.github.dot166.jlib.app.devtools.LogTopics
import io.github.dot166.jlib.app.devtools.logWarning
import io.github.dot166.jlib.compose.JLibAppTheme
import io.github.dot166.jlib.compose.JLibScreen
import io.github.dot166.jlib.compose.conditional
import io.github.dot166.jlib.version

class CrashDialogActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stacktraces = CrashUtility.getUnhandledStacktraces(this)

        val errorReport = buildString {
            appendLine("#### Environment information")
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            appendLine("- ${getAppLabel()} [${packageInfo.versionName ?: packageInfo.longVersionCode}] (${packageInfo.longVersionCode})")
            appendLine("- jLib [${version}]")
            appendLine("- Device: ${Devtools.getDeviceName()}")
            appendLine("- Android: ${Devtools.getAndroidVersion()}")
            appendLine()

            appendLine("#### Attached logs and stacktrace files")

            appendCollapsibleSection(
                summary = "Detailed info (Debug log header)",
                details = Devtools.generateDebugLog(
                    this@CrashDialogActivity,
                    includeLogcat = false,
                ),
            )

            appendLine()

            if (stacktraces.isNotEmpty()) {
                stacktraces.forEach {
                    appendCollapsibleSection(it.name, it.details)
                    appendLine()
                }
            } else {
                logWarning(LogTopics.CRASH_UTILITY) {
                    "Stacktrace file list is empty."
                }
            }
        }

        val reportInstructions = getString(
            R.string.crash_dialog__report_instructions
        ).format(
            getString(R.string.crash_dialog__bug_report_template)
        )

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
                            CrashDialogScreen(
                                errorReport = errorReport,
                                reportInstructions = reportInstructions,
                                onCopyToClipboard = {
                                    copyToClipboard(errorReport)
                                },
                                onOpenBugReport = {
//                              val browserIntent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse(
//                                    getString(R.string.florisboard__issue_tracker_url)
//                                ),
//                              )
//                              startActivity(browserIntent)
                                },
                                onClose = {
                                    finish()
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAppLabel(): String {
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(stringId)
    }

    private fun copyToClipboard(errorReport: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE)

        val toastMessage = if (clipboardManager is ClipboardManager) {
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    "Crash report",
                    errorReport,
                )
            )

            getString(R.string.crash_dialog__copy_to_clipboard_success)
        } else {
            getString(R.string.crash_dialog__copy_to_clipboard_failure)
        }

        Toast.makeText(
            this,
            toastMessage,
            Toast.LENGTH_SHORT,
        ).show()
    }

    /**
     * Rules for collapsible markdown on GitHub:
     * https://gist.github.com/pierrejoubert73/902cc94d79424356a8d20be2b382e1ab
     */
    private fun StringBuilder.appendCollapsibleSection(
        summary: String,
        details: String,
    ) {
        appendLine("<details>")
        append("<summary>").append(summary).appendLine("</summary>")
        appendLine()
        appendLine("```")
        appendLine(details)
        appendLine("```")
        appendLine("</details>")
    }
}

@Composable
private fun CrashDialogScreen(
    errorReport: String,
    reportInstructions: String,
    onCopyToClipboard: () -> Unit,
    onOpenBugReport: () -> Unit,
    onClose: () -> Unit,
) {
    JLibScreen(
        title = stringResource(R.string.crash_dialog__title),
        navigationIconVisible = false,
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Button(
                        onClick = onCopyToClipboard,
                        modifier = Modifier.padding(2.dp),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.crash_dialog__copy_to_clipboard
                            )
                        )
                    }

                    Button(
                        onClick = onOpenBugReport,
                        modifier = Modifier.padding(2.dp),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.crash_dialog__open_issue_tracker
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                ) {
                    Button(
                        onClick = onClose,
                        modifier = Modifier.padding(2.dp),
                    ) {
                        Text(
                            text = stringResource(
                                R.string.crash_dialog__close
                            )
                        )
                    }
                }
            }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(
                    R.string.crash_dialog__description
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )

            Text(
                text = reportInstructions,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                    ),
                thickness = DividerDefaults.Thickness,
                color = Color.DarkGray,
            )

            Text(
                text = errorReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
        }
    }
}
