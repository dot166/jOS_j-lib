package io.github.dot166.jlib.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
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
        return new jLIBSettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setSupportActionBar(findViewById(R.id.actionbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeActionContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, preferenceFragment()).commit();
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


    /**
     * This fragment shows the preferences.
     * @deprecated please use {@link PreferenceFragmentCompat} instead
     */
    @Deprecated
    public static class jLIBSettingsFragment extends PreferenceFragmentCompat {

        @Deprecated
        public boolean hideLIB() {
            return false;
        }
        @Deprecated
        public int preferenceXML() {
            return R.xml.launcher_preferences;
        }

        @Deprecated
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(preferenceXML(), rootKey);
            addPreferencesFromResource(R.xml.lib_preference);

            PreferenceScreen screen = getPreferenceScreen();
            for (int i = screen.getPreferenceCount() - 1; i >= 0; i--) {
                Preference preference = screen.getPreference(i);
                if (Objects.equals(preference.getKey(), "lib_category")) {
                    PreferenceCategory category = (PreferenceCategory) preference;
                    category.setOrder(999999999); // put at bottom of screen because the user added prefs are more important
                    for (int j = category.getPreferenceCount() - 1; j >= 0; j--) {
                        Preference preference2 = category.getPreference(j);
                        if (!configPreference(preference2)) {
                            category.removePreference(preference2);
                        }
                    }
                    if (category.getPreferenceCount() == 0) {
                        screen.removePreference(category);
                    }
                }
                if (!configPreference(preference)) {
                    screen.removePreference(preference);
                }
            }
            if (getActivity() != null && !TextUtils.isEmpty(getPreferenceScreen().getTitle())) {
                getActivity().setTitle(getPreferenceScreen().getTitle());
            }
        }

        /**
         * Initializes a preference. This is called for every preference. Returning false here
         * will remove that preference from the list.
         */
        @Deprecated
        private boolean configPreference(Preference preference) {
            Log.i("Preference Logging", preference.getKey());
            switch (preference.getKey()) {
                case "LIBVer":
                    Log.i("Preference Logging", "LIBVer Found!!!!");
                    preference.setSummary(String.valueOf(VersionUtils.getLibVersion()));
                    preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(@NonNull Preference preference) {
                            startActivity(new Intent(preference.getContext(), LIBAboutActivity.class));
                            return !hideLIB();
                        }
                    });
                    return !hideLIB();
            }
            return extraPrefs(preference);
        }

        @Deprecated
        protected boolean extraPrefs(Preference preference) {
            return true;
        }

        @Deprecated
        protected void recreateActivityNow() {
            Activity activity = getActivity();
            if (activity != null) {
                activity.recreate();
            }
        }
    }
}
