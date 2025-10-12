package io.github.dot166.jlib.utils

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.dot166.jlib.R

object ErrorUtils {
    var TAG: String = "jLib Error Handler"

    /**
     * jLib Error Handler
     * WARNING!! When this is used this assumes that the error might be recoverable
     * do not use this when the error is not recoverable
     * @param e Throwable to attempt to handle
     * @param context Context to pass into the error handler
     */
    @JvmStatic
    fun handle(e: Throwable, context: Context) {
        e.printStackTrace()
        try {
            val builder = MaterialAlertDialogBuilder(context)

            builder.setMessage(R.string.dialog_fail_message)
                .setTitle(R.string.dialog_fail_title)
                .setIcon(R.mipmap.icon_error)
                .setCancelable(false)
                .setPositiveButton(
                    R.string.dialog_stacktrace
                ) { dialog, id ->
                    val builder2 = MaterialAlertDialogBuilder(context)

                    builder2.setMessage(
                        e.toString() + "\n" + e.stackTrace.contentToString()
                            .replace(", ".toRegex(), "\n")
                    )
                        .setTitle(R.string.dialog_fail_title)
                        .setIcon(R.mipmap.icon_error)
                        .setCancelable(false)
                        .setNeutralButton(
                            R.string.ok
                        ) { dialog, id -> dialog.dismiss() }
                    //Creating dialog box
                    builder2.show()
                }
            builder.setNegativeButton(
                R.string.dialog_ignore
            ) { dialog, id -> Log.i(TAG, "IGNORING ERROR") }
            //Creating dialog box
            builder.show()
        } catch (de1: Exception) {
            Log.e(TAG, "Error handler Broke!!")
            de1.printStackTrace()
            Toast.makeText(
                context,
                "an error has occurred and the error handler is not available, please check the logs",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
