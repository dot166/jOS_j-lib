package io.github.dot166.jlib.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.material.chip.Chip;
import com.google.android.material.color.MaterialColors;

import java.util.Arrays;
import java.util.List;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.internal.utils.ContributorRow;
import io.github.dot166.jlib.utils.AppUtils;
import io.github.dot166.jlib.utils.ErrorUtils;

public class jAboutActivity extends jActivity {

    public Intent versionIntent(Context context) {
        return new Intent();
    }

    public Drawable getAppIcon(Context context) {
        return AppUtils.getAppIcon(context);
    }

    public String getAppLabel(Context context) {
        return AppUtils.getAppLabel(context);
    }

    public interface Roles {
        int descriptionResId();
    }

    public enum Role implements Roles {
        Maintainer(R.string.maintainer),
        Contributor(R.string.contributor),
        Example(R.string.example_info);

        private final int descriptionResId;

        Role(int descriptionResId) {
            this.descriptionResId = descriptionResId;
        }

        @Override
        public int descriptionResId() {
            return this.descriptionResId;
        }
    }

    public record Contributor(String name, Roles role, String photoUrl, String socialUrl) {
    }

    public record Link(@DrawableRes int iconResId, @StringRes int labelResId, String url) {
            public Link(int iconResId, int labelResId, String url) {
                this.iconResId = iconResId;
                this.labelResId = labelResId;
                this.url = url;
            }
        }

    public boolean showOnlyContributors(Context context) {
        return false;
    }

    public List<Contributor> product() {
        return defaultProduct;
    }

    private final List<Contributor> defaultProduct = Arrays.asList(
            new Contributor(
                    "._______166",
                    Role.Maintainer,
                    "https://avatars.githubusercontent.com/u/62702353",
                    "https://github.com/jf916"
            ),
            new Contributor(
                    "bh916",
                    Role.Contributor,
                    "https://avatars.githubusercontent.com/u/138221251",
                    "https://github.com/bh916"
            ),
            new Contributor(
                    "Put your main devs or contributors here",
                    Role.Example,
                    "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png",
                    "https://example.com"
            )
    );

    public List<Link> links() {
        return defaultLinks;
    }

    private final List<Link> defaultLinks = Arrays.asList(
            new Link(
                    R.drawable.ic_github,
                    R.string.github,
                    "https://github.com/dot166/jOS_j-LIB"
            )
    );

    public String versionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName != null ? pInfo.versionName : "";
        } catch (PackageManager.NameNotFoundException e) {
            ErrorUtils.handle(e, context);
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutactivity);
        final Context context = this;

        if (!showOnlyContributors(context)) {
            ((ImageView) findViewById(R.id.app_icon)).setImageDrawable(getAppIcon(context));
            ((TextView) findViewById(R.id.app_name)).setText(getAppLabel(context));
            TextView versionView = findViewById(R.id.app_version);
            versionView.setText(versionName(this));
            versionView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        startActivity(versionIntent(context));
                    } catch (Exception ignored) {
                        Log.i("LibTest", "Test Activity Disabled");
                        Log.i("GLaDOS", "you have completed all available tests, you will now receive cake");
                    }
                    return true;
                }
            });

            LinearLayout linkContainer = findViewById(R.id.link_container);
            linkContainer.removeAllViews();
            for (final Link link : links()) {
                Chip button = new Chip(context);
                button.setChipIcon(AppCompatResources.getDrawable(context, link.iconResId));
                button.setText(context.getString(link.labelResId));
                button.setChipIconTint(ColorStateList.valueOf(MaterialColors.getColor(button, com.google.android.material.R.attr.colorOnSurface)));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params.setMarginStart(4);
                params.setMarginEnd(4);
                button.setLayoutParams(params);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(link.url);
                        CustomTabsIntent intent = new CustomTabsIntent.Builder().build();
                        intent.launchUrl(context, webpage);
                    }
                });
                linkContainer.addView(button);
            }
        }

        LinearLayout contributorsContainer = findViewById(R.id.contributors_container);
        for (Contributor contributor : product()) {
            ContributorRow row = new ContributorRow(context);
            row.setName(contributor.name);
            row.setDescription(context.getString(contributor.role.descriptionResId()));
            row.setUrl(contributor.socialUrl);
            row.setPhotoUrl(contributor.photoUrl);
            contributorsContainer.addView(row);
        }

        if (!showOnlyContributors(context)) {
            Button licencesButton = findViewById(R.id.licences_button);
            licencesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(context, OSSLicenceActivity.class));
                    } catch (Exception e) {
                        ErrorUtils.handle(e, context);
                    }
                }
            });
        }
        setSupportActionBar(findViewById(R.id.actionbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description);
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
