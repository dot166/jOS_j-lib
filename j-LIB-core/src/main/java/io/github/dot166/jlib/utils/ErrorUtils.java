package io.github.dot166.jlib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;

import io.github.dot166.jlib.R;

public class ErrorUtils {
    static String TAG = "jLib Error Handler";

    /**
     * jLib Error Handler
     * WARNING!! When this is used this assumes that the error might be recoverable
     * do not use this when the error is not recoverable
     * @param e Throwable to attempt to handle
     * @param context Context to pass into the error handler
     */
    public static void handle(Throwable e, Context context) {
        e.printStackTrace();
        try {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

            builder.setMessage(R.string.dialog_fail_message)
                    .setTitle(R.string.dialog_fail_title)
                    .setIcon(R.mipmap.icon_error)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_stacktrace, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(context);

                            builder2.setMessage(e + "\n" + Arrays.toString(e.getStackTrace()).replaceAll(", ", "\n"))
                                    .setTitle(R.string.dialog_fail_title)
                                    .setIcon(R.mipmap.icon_error)
                                    .setCancelable(false)
                                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            //Creating dialog box
                            builder2.show();
                        }
                    });
            builder.setNegativeButton(R.string.dialog_ignore, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "IGNORING ERROR");
                }
            });
            //Creating dialog box
            builder.show();
        } catch (Exception de1) {
            Log.e(TAG, "Error handler Broke!!");
            de1.printStackTrace();
            Toast.makeText(context, "an error has occurred and the error handler is not available, please check the logs", Toast.LENGTH_SHORT).show();
        }
    }
}
