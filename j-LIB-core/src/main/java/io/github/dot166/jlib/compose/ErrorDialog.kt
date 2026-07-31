package io.github.dot166.jlib.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.android.settingslib.spa.framework.compose.rememberDrawablePainter
import com.android.settingslib.spa.framework.theme.SettingsTheme
import com.android.settingslib.spa.widget.dialog.getDialogWidth
import io.github.dot166.jlib.R
import io.github.dot166.jlib.utils.ErrorUtils

@Composable
fun ErrorDialog(
    error: Throwable?,
    message: String = "",
    onDismiss: () -> Unit,
) {
    if (error != null) {
        SettingsTheme {
            val context = LocalContext.current
            val stackTrace = remember(error) {
                ErrorUtils.buildStackTrace(error)
            }

            var showStackTrace by remember { mutableStateOf(false) }

            ErrorUtils.log(error)

            if (showStackTrace) {
                AlertDialog(
                    modifier = Modifier.width(getDialogWidth()),
                    onDismissRequest = {},
                    title = {
                        Text(
                            stringResource(R.string.dialog_fail_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    text = {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            SelectionContainer {
                                Text(stackTrace)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showStackTrace = false
                                onDismiss()
                            }
                        ) {
                            Text(stringResource(android.R.string.ok))
                        }
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                )
            } else {
                AlertDialog(
                    modifier = Modifier.width(getDialogWidth()),
                    onDismissRequest = {},
                    icon = {
                        Icon(
                            painter = rememberDrawablePainter(
                                context.packageManager.getApplicationIcon(
                                    context.packageName
                                )
                            ),
                            contentDescription = null
                        )
                    },
                    title = {
                        Text(
                            stringResource(R.string.dialog_fail_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    text = {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            Text(
                                message.ifBlank { stringResource(R.string.default_dialog_fail_message) }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showStackTrace = true
                            }
                        ) {
                            Text(stringResource(R.string.dialog_stacktrace))
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                Log.i("jLib Error Handler", "IGNORING ERROR")
                                onDismiss()
                            }
                        ) {
                            Text(stringResource(android.R.string.ok))
                        }
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                )
            }
        }
    }
}
