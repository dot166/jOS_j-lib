package io.github.dot166.jlib.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import io.github.dot166.jlib.R
import io.github.dot166.jlib.app.SettingsLibAlertDialogBuilder

object ErrorUtils {

    /**
     * jLib Error Handler
     * if the app cannot recover from the error, set action to activity.finishAffinity() or activity.finish()
     * @param e Throwable to attempt to handle
     * @param context Context to pass into the error handler
     * @param action The action to be ran once the dialog has been dismissed
     * @param message String the message to display in the dialog, use getString() if your message is a resource
     */
    fun handle(e: Throwable, context: Context, message: String, action: () -> Unit) {
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
                ) // only show 2 causes as i don't want to overwhelm the dialog
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
                            R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            action()
                        }
                        .show()
                }
                .setNegativeButton(
                    R.string.ok
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
     * jLib Error Handler
     * this is a compatibility function for java that sets action to nothing, as java doesn't
     * support functions as a parameter like kotlin can
     * @param e Throwable to attempt to handle
     * @param context Context to pass into the error handler
     * @param message String the message to display in the dialog, use getString() if your message is a resource
     */
    @JvmStatic
    fun handle(e: Throwable, context: Context, message: String) {
        handle(e, context, message) {}
    }
}
