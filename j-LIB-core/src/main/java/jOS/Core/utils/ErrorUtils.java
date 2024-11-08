package jOS.Core.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;

import jOS.Core.R;

public class ErrorUtils {
    static String TAG = "jLib Error Handler";

    /**
     * jLib Error Handler
     * @param e Exception to attempt to handle
     * @param activity Activity to pass into the error handler
     */
    public static void handle(Exception e, Activity activity) {
        handle(e, activity, true);
    }

    /**
     * jLib Error Handler
     * @param e Exception to attempt to handle
     * @param activity Activity to pass into the error handler
     * @param recoverable Boolean to select if the handled Exception is recoverable
     */
    public static void handle(Exception e, Activity activity, Boolean recoverable) {
        e.printStackTrace();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(activity.getString(R.string.dialog_fail_message))
                .setTitle(R.string.dialog_fail_title)
                .setIcon(R.mipmap.icon_error)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_stacktrace, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);

                        builder2.setMessage(e + Arrays.toString(e.getStackTrace()))
                                .setTitle(R.string.dialog_fail_title)
                                .setIcon(R.mipmap.icon_error)
                                .setCancelable(false)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (recoverable) {
                                            dialog.dismiss();
                                        } else {
                                            activity.finish();
                                        }
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder2.create();
                        alert.show();
                    }
                });
        if (recoverable) {
            builder.setNegativeButton(R.string.dialog_ignore, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "IGNORING ERROR");
                }
            });
        } else {
            builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.finish();
                }
            });
        }
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }
}
