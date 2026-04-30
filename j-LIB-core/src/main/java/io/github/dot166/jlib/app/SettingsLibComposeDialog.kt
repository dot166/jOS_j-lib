package io.github.dot166.jlib.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SettingsLibComposeDialog(
    onDismissRequest: () -> Unit,
    onPositivePress: () -> Unit = {},
    positiveContent: (@Composable () -> Unit)? = null,
    onNeutralPress: () -> Unit = {},
    neutralContent: (@Composable () -> Unit)? = null,
    onNegativePress: () -> Unit = onDismissRequest,
    negativeContent: (@Composable () -> Unit)? = null,
    properties: DialogProperties,
    content: @Composable () -> Unit,
) {
    SettingsLibComposeTheme {
        Dialog(onDismissRequest, properties) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    content()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if (negativeContent != null) {
                            OutlinedButton(
                                onClick = { onNegativePress() },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                negativeContent()
                            }
                        }
                        if (neutralContent != null) {
                            TextButton(
                                onClick = { onNeutralPress() },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                neutralContent()
                            }
                        }
                        if (positiveContent != null) {
                            FilledTonalButton(
                                onClick = { onPositivePress() },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                positiveContent()
                            }
                        }
                    }
                }
            }
        }
    }
}