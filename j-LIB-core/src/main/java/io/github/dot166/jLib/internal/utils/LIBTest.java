package io.github.dot166.jLib.internal.utils;

import static io.github.dot166.jLib.utils.VersionUtils.getAndroidVersion;
import static io.github.dot166.jLib.jOS.Build.jOS_RELEASE;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;

import io.github.dot166.jLib.internal.LIBTestBottomSheet;
import io.github.dot166.jLib.R;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LIBTest {

    public static void Test(AppCompatActivity context) {
        if (context.getSupportActionBar() != null) {
            context.getSupportActionBar().setSubtitle("AAAA");
        }
        TextView text = context.findViewById(R.id.textView);
        Button button = context.findViewById(R.id.button);
        Button button2 = context.findViewById(R.id.button2);
        String android_lib_test = "Android: " + getAndroidVersion();
        String j_lib_test = "jOS: " + jOS_RELEASE;
        String all = android_lib_test + " " + j_lib_test;
        text.setText(all);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LIBTestBottomSheet().show(context.getSupportFragmentManager(), LIBTestBottomSheet.TAG);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setComponent(new ComponentName(context.getApplicationInfo().packageName, "io.github.dot166.jLib.LIBChangelogActivity")));
            }
        });
    }
}
