package io.github.dot166.jlib;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.dot166.jlib.app.jAboutActivity;
import io.github.dot166.jlib.utils.VersionUtils;

public class LIBAboutActivity extends jAboutActivity {

    @NonNull
    @Override
    public Intent versionIntent(@NonNull Context context) {
        return new Intent(context, LIBTestActivity.class);
    }

    @NonNull
    @Override
    public Drawable getAppIcon(@NonNull Context context) {
        return Objects.requireNonNull(AppCompatResources.getDrawable(context, R.mipmap.ic_launcher_j));
    }

    @NonNull
    @Override
    public String getAppLabel(@NonNull Context context) {
        return getString(R.string.jlib);
    }

    @NonNull
    @Override
    public String versionName(@NonNull Context context) {
        return String.valueOf(VersionUtils.getLibVersion(context));
    }

    @NonNull
    @Override
    public List<jAboutActivity.Contributor> product() {
        return new ArrayList<>() {{
            add(new Contributor("._______166", Role.Maintainer, "https://avatars.githubusercontent.com/u/62702353", "https://github.com/dot166"));
            add(new Contributor("bh916", Role.Contributor, "https://avatars.githubusercontent.com/u/138221251", "https://github.com/bh196"));
        }};
    }

    public enum Role implements Roles {
        Maintainer(R.string.maintainer),
        Contributor(R.string.contributor);

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
