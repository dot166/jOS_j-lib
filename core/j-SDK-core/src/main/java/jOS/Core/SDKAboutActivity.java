package jOS.Core;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SDKAboutActivity extends jAboutActivity {

    @NonNull
    @Override
    public Intent versionIntent(@NonNull Context context) {
        return new Intent(context, SDKTestActivity.class);
    }

    @NonNull
    @Override
    public List<jAboutActivity.Contributor> product() {
        return new ArrayList<>() {{
            add(new Contributor("._______166", Role.LeadDev, "https://avatars.githubusercontent.com/u/62702353", "https://github.com/dot166"));
            add(new Contributor("bh916", Role.Dev, "https://avatars.githubusercontent.com/u/138221251", "https://github.com/bh196"));
        }};
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
