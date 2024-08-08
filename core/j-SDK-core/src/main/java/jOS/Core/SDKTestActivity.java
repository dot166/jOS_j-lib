package jOS.Core;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import jOS.Core.utils.IconUtils;
import jOS.Core.utils.SDKTest;

public class SDKTestActivity extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.sdkplaceholder, false);
        super.onCreate(savedInstanceState);
        SDKTest.Test(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_favorite).setIcon(IconUtils.getActivityIcon(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, jConfigActivity.class));
            return true;
        } else if (itemId == R.id.action_favorite) {
            startActivity(new Intent(this, SDKAboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

