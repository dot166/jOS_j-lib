package jOS.Core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;

import jOS.Core.R;

public class ErrorUtils {
    static String TAG = "jLib Error Handler";

    public static void handle(Exception e, Activity context) {
        handle(e, context, true);
    }

    public static void handle(Exception e, Activity context, Boolean recoverable) {
        e.printStackTrace();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.dialog_fail_message))
                .setTitle(R.string.dialog_fail_title)
                .setIcon(R.mipmap.icon_error)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_stacktrace, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

                        builder2.setMessage(e + Arrays.toString(e.getStackTrace()))
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
        if (recoverable) {
            builder.setNegativeButton(R.string.dialog_ignore, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i(TAG, "IGNORING ERROR");
                }
            });
        } else {
            builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.finish();
                }
            });
        }
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }
}
