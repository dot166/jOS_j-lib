package io.github.dot166.jlib.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import io.github.dot166.jlib.R

object ErrorUtils {

    /**
     * jLib Error Handler
     * if the app cannot recover from the error, set action to [android.app.Activity.finishAffinity] or [android.app.Activity.finish]
     * @param e [Throwable] to attempt to handle
     * @param context [Context] to pass into the error handler
     * @param message [String] the message to display in the dialog, use [Context.getString] if your message is a resource
     * @param action The action to be run once the dialog has been dismissed
     */
    @Deprecated("Please use the new compose API, see LibExample for implementation details")
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
            AlertDialog.Builder(context)
                .setMessage(content)
                .setTitle(R.string.dialog_fail_title)
                .setIcon(context.packageManager.getApplicationIcon(context.packageName))
                .setCancelable(false)
                .setPositiveButton(
                    R.string.dialog_stacktrace
                ) { _, _ ->
                    AlertDialog.Builder(context)
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

    fun buildStackTrace(e: Throwable): String {
        val builder = StringBuilder()

        fun appendThrowable(t: Throwable) {
            builder.appendLine(t.toString())
            t.stackTrace.forEach {
                builder.appendLine(it.toString())
            }
        }

        appendThrowable(e)

        e.cause?.let {
            builder.appendLine()
            builder.appendLine("Caused by:")
            appendThrowable(it)

            it.cause?.let { cause2 ->
                builder.appendLine()
                builder.appendLine("Caused by:")
                appendThrowable(cause2)
            }
        }

        return builder.toString()
    }

    fun log(e: Throwable) {
        Log.e("jLib Error Handler", buildStackTrace(e))
    }
}
