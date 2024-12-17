package io.github.dot166.jLib.app;

import static io.github.dot166.jLib.ThemeEngine.ThemeEngine.currentTheme;
import static io.github.dot166.jLib.ThemeEngine.ThemeEngine.getThemeFromDB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

import io.github.dot166.jLib.BuildConfig;
import io.github.dot166.jLib.R;
import io.github.dot166.jLib.utils.ErrorUtils;
import io.github.dot166.jLib.LIBAboutActivity;

/**
 * jLib Settings activity.
 */
public class jConfigActivity extends jActivity {

    public jLIBSettingsFragment preferenceFragment() {
        return new jLIBSettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.settings_activity, false);
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, preferenceFragment()).commit();
        }
    }


    /**
     * This fragment shows the preferences.
     */
    public static class jLIBSettingsFragment extends PreferenceFragmentCompat {

        public boolean isTEConfig() {
            return false;
        }
        public boolean hideLIB() {
            return false;
        }
        public int preferenceXML() {
            return R.xml.launcher_preferences;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(preferenceXML(), rootKey);
            if (!hideLIB()) {
                addPreferencesFromResource(R.xml.lib_preference);
            }

            PreferenceScreen screen = getPreferenceScreen();
            for (int i = screen.getPreferenceCount() - 1; i >= 0; i--) {
                Preference preference = screen.getPreference(i);
                if (Objects.equals(preference.getKey(), "lib_category")) {
                    PreferenceCategory category = (PreferenceCategory) preference;
                    for (int i2 = category.getPreferenceCount() - 1; i2 >= 0; i2--) {
                        Preference preference2 = category.getPreference(i2);
                        if (!configPreference(preference2)) {
                            category.removePreference(preference2);
                        }
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
        protected boolean configPreference(Preference preference) {
            Log.i("Preference Logging", preference.getKey());
            switch (preference.getKey()) {
                case "ThemeEngine":
                    Log.i("Preference Logging", "ThemeEngine Found!!!!");
                    preference.setOnPreferenceClickListener(p -> {
                        Intent intent = new Intent("io.github.dot166.ThemeEngine.CONFIG");
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            ErrorUtils.handle(e, preference.getContext());
                        }
                        return !isTEConfig();
                    });
                    return !isTEConfig();
                case "LIBVer":
                    Log.i("Preference Logging", "LIBVer Found!!!!");
                    preference.setSummary(BuildConfig.LIBVersion);
                    preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(@NonNull Preference preference) {
                            startActivity(new Intent(preference.getContext(), LIBAboutActivity.class));
                            return true;
                        }
                    });
                    return true;
            }
            return extraPrefs(preference);
        }

        protected boolean extraPrefs(Preference preference) {
            return true;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            View listView = getListView();
            final int bottomPadding = listView.getPaddingBottom();
            listView.setOnApplyWindowInsetsListener((v, insets) -> {
                v.setPadding(
                        v.getPaddingLeft(),
                        v.getPaddingTop(),
                        v.getPaddingRight(),
                        bottomPadding + insets.getSystemWindowInsetBottom());
                return insets.consumeSystemWindowInsets();
            });

            // Overriding Text Direction in the Androidx preference library to support RTL
            view.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        }

        @Override
        public void onResume() {
            super.onResume();

            if (!Objects.equals(currentTheme, getThemeFromDB(getPreferenceManager().getContext()))) {
                recreateActivityNow();
            }
        }

        protected void recreateActivityNow() {
            Activity activity = getActivity();
            if (activity != null) {
                activity.recreate();
            }
        }
    }
}
