package io.github.dot166.jlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.jlib.utils.AppUtils;
import io.github.dot166.jlib.utils.ErrorUtils;
import io.github.dot166.jlib.internal.utils.LIBTest;

public class LIBTestActivity extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libplaceholder);
        setSupportActionBar(findViewById(R.id.actionbar));
        LIBTest.Test(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lib_test_menu, menu);
        menu.findItem(R.id.action_favorite).setIcon(AppUtils.getAppIcon(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            try {
                startActivity(new Intent(Intent.ACTION_APPLICATION_PREFERENCES).setPackage(getPackageName()));
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
            }
            return true;
        } else if (itemId == R.id.action_favorite) {
            try {
                startActivity(new Intent(this, LIBAboutActivity.class));
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

