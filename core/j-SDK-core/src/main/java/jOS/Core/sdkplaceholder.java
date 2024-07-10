package jOS.Core;

import static android.os.Build.VERSION.RELEASE;
import static android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
import static jOS.Core.Build.jOS_RELEASE;
import static jOS.Core.Build.j_DEVICE;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sdkplaceholder extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.sdkplaceholder, false);
        super.onCreate(savedInstanceState);
        alertdialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void alertdialog() {
        getSupportActionBar().setSubtitle("AAAA");
        TextView text = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        String android_sdk_test = "Android: " + androidver();
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
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setComponent(new ComponentName(getApplicationInfo().packageName, "jOS.Core.SDKChangelogActivity")));
            }
        });
    }

    private String androidver() {
        log.i(TAG, android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return RELEASE_OR_PREVIEW_DISPLAY;
        }
        return RELEASE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(Intent.ACTION_APPLICATION_PREFERENCES).setPackage(getPackageName()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

