package io.github.dot166.jlib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(R.string.dialog_fail_message)
                    .setTitle(R.string.dialog_fail_title)
                    .setIcon(R.mipmap.icon_error)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_stacktrace, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

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
                            AlertDialog alert = builder2.create();
                            alert.show();
                        }
                    });
            builder.setNegativeButton(R.string.dialog_ignore, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "IGNORING ERROR");
                }
            });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception de1) {
            Log.e(TAG, "Error handler Broke!!");
            de1.printStackTrace();
            Toast.makeText(context, "an error has occurred and the error handler is not available, please check the logs", Toast.LENGTH_SHORT).show();
        }
    }
}
