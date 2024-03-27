package jOS.Core;

import static android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
import static jOS.Core.Build.jOS_RELEASE;
import static jOS.Core.Build.j_DEVICE;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sdkplaceholder extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.string.sdkplaceholder, R.layout.sdkplaceholder, false);
        super.onCreate(savedInstanceState);
        alertdialog();
    }

    public void alertdialog() {
        TextView text = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        String android_sdk_test = "Android: " + RELEASE_OR_PREVIEW_DISPLAY;
        String j_sdk_test = "jOS: " + jOS_RELEASE;
        String j_verify_test = "is j Device: " + j_DEVICE();
        String all = android_sdk_test + " " + j_sdk_test + " " + j_verify_test;
        text.setText(all);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

