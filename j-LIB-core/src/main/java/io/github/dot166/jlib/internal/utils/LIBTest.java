package io.github.dot166.jlib.internal.utils;

import static io.github.dot166.jlib.utils.VersionUtils.getAndroidVersion;
import static io.github.dot166.jlib.jos.Build.jOS_RELEASE;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import io.github.dot166.jlib.flags.Flags;
import io.github.dot166.jlib.internal.LIBTestBottomSheet;
import io.github.dot166.jlib.R;
import io.github.dot166.jlib.rusthelper.Test;
import io.github.dot166.jlib.utils.VersionUtils;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LIBTest {

    public static void Test(AppCompatActivity context) {
        Test.rustIntegrationTest();
        if (context.getSupportActionBar() != null) {
            String str;
            if (Flags.testFlag()) {
                str = "flag_flag";
            } else {
                str = "AAAA";
            }
            context.getSupportActionBar().setSubtitle(str);
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
                Uri webpage = Uri.parse("https://github.com/dot166/jOS_j-lib/commits/v" + VersionUtils.getLibVersion(v.getContext()));
                CustomTabsIntent intent = new CustomTabsIntent.Builder()
                        .build();
                intent.launchUrl(v.getContext(), webpage);
            }
        });
    }
}
