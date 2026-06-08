package io.github.dot166.jlib.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.settingslib.spa.framework.theme.SettingsTheme
import com.android.settingslib.spa.widget.dialog.AlertDialogButton
import com.android.settingslib.spa.widget.dialog.rememberAlertDialogPresenter
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.SettingsLibAlertDialogBuilder

object ErrorUtils {

    /**
     * jLib Error Handler
     * if the app cannot recover from the error, set action to [android.app.Activity.finishAffinity] or [android.app.Activity.finish]
     * @param e [Throwable] to attempt to handle
     * @param context [Context] to pass into the error handler
     * @param message [String] the message to display in the dialog, use [Context.getString] if your message is a resource
     * @param action The action to be run once the dialog has been dismissed
     */
    @JvmOverloads
    @JvmStatic
    fun handle(e: Throwable, context: Context, message: String = "", action: () -> Unit = {}) {
        val errorMessage = StringBuilder()
        errorMessage.append(
            e.toString() + "\n" + e.stackTrace.contentToString()
                .replace(", ".toRegex(), "\n")
                .replace("\\[".toRegex(), "")
                .replace("]".toRegex(), "")
        )
        if (e.cause != null) {
            errorMessage.append(
                "\n\nCaused by: " + e.cause!!.toString() + "\n" + e.cause!!.stackTrace.contentToString()
                    .replace(", ".toRegex(), "\n")
                    .replace("\\[".toRegex(), "")
                    .replace("]".toRegex(), "")
            )
            if (e.cause!!.cause != null) {
                errorMessage.append(
                    "\n\nCaused by: " + e.cause!!.cause!!.toString() + "\n" + e.cause!!.cause!!.stackTrace.contentToString()
                        .replace(", ".toRegex(), "\n")
                        .replace("\\[".toRegex(), "")
                        .replace("]".toRegex(), "")
                ) // only show 2 causes as I don't want to overwhelm the dialog
            }
        }
        Log.e("jLib Error Handler", errorMessage.toString())
        try {
            val content: String = message.ifEmpty {
                context.getString(R.string.default_dialog_fail_message)
            }
            SettingsLibAlertDialogBuilder(context)
                .setMessage(content)
                .setTitle(R.string.dialog_fail_title)
                .setIcon(context.packageManager.getApplicationIcon(context.packageName))
                .setCancelable(false)
                .setPositiveButton(
                    R.string.dialog_stacktrace
                ) { _, _ ->
                    SettingsLibAlertDialogBuilder(context)
                        .setMessage(errorMessage.toString())
                        .setTitle(R.string.dialog_fail_title)
                        .setIcon(context.packageManager.getApplicationIcon(context.packageName))
                        .setCancelable(false)
                        .setNegativeButton(
                            android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            action()
                        }
                        .show()
                }
                .setNegativeButton(
                    android.R.string.ok
                ) { _, _ ->
                    Log.i("jLib Error Handler", "IGNORING ERROR")
                    action()
                }
                .show()
        } catch (de1: Exception) {
            Log.e("jLib Error Handler", "Error handler Broke!!")
            de1.printStackTrace()
            Toast.makeText(
                context,
                "an error has occurred and the error handler is not available, please check the logs",
                Toast.LENGTH_SHORT
            ).show()
            action()
        }
    }

    /**
     * jLib Error Handler for compose
     * if the app cannot recover from the error, set action to [android.app.Activity.finishAffinity] or [android.app.Activity.finish]
     * @param e [Throwable] to attempt to handle
     * @param message [String] the message to display in the dialog, use [stringResource] if your message is a resource
     * @param action The action to be run once the dialog has been dismissed
     */
    @Composable
    fun Handle(e: Throwable, message: String = "", action: () -> Unit = {}) {
        val errorMessage = StringBuilder()
        errorMessage.append(
            e.toString() + "\n" + e.stackTrace.contentToString()
                .replace(", ".toRegex(), "\n")
                .replace("\\[".toRegex(), "")
                .replace("]".toRegex(), "")
        )
        if (e.cause != null) {
            errorMessage.append(
                "\n\nCaused by: " + e.cause!!.toString() + "\n" + e.cause!!.stackTrace.contentToString()
                    .replace(", ".toRegex(), "\n")
                    .replace("\\[".toRegex(), "")
                    .replace("]".toRegex(), "")
            )
            if (e.cause!!.cause != null) {
                errorMessage.append(
                    "\n\nCaused by: " + e.cause!!.cause!!.toString() + "\n" + e.cause!!.cause!!.stackTrace.contentToString()
                        .replace(", ".toRegex(), "\n")
                        .replace("\\[".toRegex(), "")
                        .replace("]".toRegex(), "")
                ) // only show 2 causes as I don't want to overwhelm the dialog
            }
        }
        Log.e("jLib Error Handler", errorMessage.toString())
        val content: String = message.ifEmpty {
            stringResource(R.string.default_dialog_fail_message)
        }
        SettingsTheme {
            val stackTraceDialog = rememberAlertDialogPresenter(
                dismissButton = AlertDialogButton(
                    stringResource(android.R.string.ok), onClick = { action() }),
                title = stringResource(R.string.dialog_fail_title),
                text = { Text(errorMessage.toString()) }
            )
            val entryDialog = rememberAlertDialogPresenter(
                confirmButton = AlertDialogButton(
                    stringResource(R.string.dialog_stacktrace),
                    onClick = { stackTraceDialog.open() }),
                dismissButton = AlertDialogButton(
                    stringResource(android.R.string.ok), onClick = {
                        Log.i("jLib Error Handler", "IGNORING ERROR")
                        action()
                    }),
                title = stringResource(R.string.dialog_fail_title),
                text = { Text(content) }
            )
            entryDialog.open()
        }
    }
}
