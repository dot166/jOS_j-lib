package jOS.Core;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class LIBChangelogActivity extends jWebActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configure("https://github.com/dot166/jOS_j-lib/commits/v" + BuildConfig.LIBVersion, true, true, false, true);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description);
        } else {
            Log.e("ActionBar2", "no actionbar found");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getOnBackPressedDispatcher().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
