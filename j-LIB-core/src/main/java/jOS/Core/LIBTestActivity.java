package jOS.Core;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.github.dot166.jLib.R;
import jOS.Core.utils.ErrorUtils;
import jOS.Core.utils.IconUtils;
import io.github.dot166.jLib.internal.utils.LIBTest;

public class LIBTestActivity extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.libplaceholder, false);
        super.onCreate(savedInstanceState);
        LIBTest.Test(this);
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
            try {
                startActivity(new Intent(this, jConfigActivity.class));
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
            }
            return true;
        } else if (itemId == R.id.action_favorite) {
            startActivity(new Intent(this, LIBAboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

