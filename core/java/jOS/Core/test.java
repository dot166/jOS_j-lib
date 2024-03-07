package jOS.Core;

import static android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
import static jOS.Core.Build.jOS_RELEASE;
import static jOS.Core.Build.j_DEVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import jOS.Core.R;

public class test extends Activity {

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String android_sdk_test = "Android: " + RELEASE_OR_PREVIEW_DISPLAY;
        String j_sdk_test = "jOS: " + jOS_RELEASE;
        String j_verify_test = "is j Device: " + j_DEVICE();
        String all = android_sdk_test + " " + j_sdk_test + " " + j_verify_test;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
        //Setting message manually and performing action on button click
        builder.setMessage(all)
                .setCancelable(false)
                .setNeutralButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("SDK Test Results");
        alert.show();
    }
}

