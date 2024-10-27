package jOS.Core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LIBAboutActivity extends jAboutActivity {

    @NonNull
    @Override
    public Intent versionIntent(@NonNull Context context) {
        return new Intent(context, LIBTestActivity.class);
    }

    @NonNull
    @Override
    public List<jAboutActivity.Contributor> product() {
        return new ArrayList<>() {{
            add(new Contributor("._______166", Role.LeadDev, "https://avatars.githubusercontent.com/u/62702353", "https://github.com/dot166"));
            add(new Contributor("bh916", Role.Dev, "https://avatars.githubusercontent.com/u/138221251", "https://github.com/bh196"));
        }};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    public enum Role implements Roles {
        LeadDev(R.string.leaddev),
        Dev(R.string.dev);

        private final int descriptionResId;

        Role(int descriptionResId) {
            this.descriptionResId = descriptionResId;
        }

        public int getDescriptionResId() {
            return descriptionResId;
        }

        @Override
        public int descriptionResId() {
            return this.descriptionResId;
        }
    }

}
