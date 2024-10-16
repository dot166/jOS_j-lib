package jOS.Core.utils;

import static android.os.Build.VERSION.RELEASE;
import static android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
import static jOS.Core.Build.jOS_RELEASE;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import jOS.Core.R;
import jOS.Core.jLIBCoreApp;

public class LIBTest {

    public static void Test(AppCompatActivity context, View view) {
        TextView text;
        Button button;
        text = view.findViewById(R.id.textView);
        button = view.findViewById(R.id.button);
        String android_lib_test = "Android: " + androidver();
        String j_lib_test = "jOS: " + jOS_RELEASE;
        String all = android_lib_test + " " + j_lib_test;
        text.setText(all);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }

    private static String androidver() {
        Log.i(jLIBCoreApp.TAG, String.valueOf(android.os.Build.VERSION.SDK_INT));
        if (VersionUtils.Android.isAtLeastT()) {
            return RELEASE_OR_PREVIEW_DISPLAY;
        }
        return RELEASE;
    }
}
