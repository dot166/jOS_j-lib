package io.github.dot166.jlib.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

import io.github.dot166.jlib.R;
import io.github.dot166.jlib.LIBAboutActivity;
import io.github.dot166.jlib.utils.VersionUtils;

/**
 * jLib Settings activity.
 */
public class jConfigActivity extends jActivity {

    public PreferenceFragmentCompat preferenceFragment() {
        return new PreferenceFragmentCompat() {
            @Override
            public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
                PreferenceScreen screen = getPreferenceScreen();
                Preference disclaimer = new Preference(requireContext());
                disclaimer.setTitle("This is an example preference screen, please override the preferenceFragment() function to use your own preference fragment");
                screen.addPreference(disclaimer);
                Preference libPref = new Preference(requireContext());
                libPref.setIcon(android.R.drawable.sym_def_app_icon);
                libPref.setTitle(R.string.jlib_version);
                libPref.setSummary(String.valueOf(VersionUtils.getLibVersion()));
                libPref.setOnPreferenceClickListener(preference -> {
                    startActivity(new Intent(preference.getContext(), LIBAboutActivity.class));
                    return true;
                });
                screen.addPreference(libPref);
            }
        };
    }

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setSupportActionBar(findViewById(R.id.actionbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, preferenceFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
