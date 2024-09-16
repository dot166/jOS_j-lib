package jOS.Core;

import static androidx.preference.PreferenceFragmentCompat.ARG_PREFERENCE_ROOT;
import static jOS.Core.ThemeEngine.currentTheme;
import static jOS.Core.ThemeEngine.getThemeFromDB1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartScreenCallback;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

import jOS.Core.utils.ErrorUtils;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
public class jConfigActivity extends jActivity
        implements OnPreferenceStartFragmentCallback, OnPreferenceStartScreenCallback {

    public int preferenceFragmentValue() {
        return R.string.settings_fragment_name;
    }

    public static final String EXTRA_FRAGMENT_ARGS = ":settings:fragment_args";

    // Intent extra to indicate the pref-key of the root screen when opening the settings activity
    public static final String EXTRA_FRAGMENT_ROOT_KEY = ARG_PREFERENCE_ROOT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.settings_activity, false);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("ActionBar2", "no actionbar found");
        }

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Intent intent = getIntent();

        if (savedInstanceState == null) {
            Bundle args = intent.getBundleExtra(EXTRA_FRAGMENT_ARGS);
            if (args == null) {
                args = new Bundle();
            }

            String root = intent.getStringExtra(EXTRA_FRAGMENT_ROOT_KEY);
            if (!TextUtils.isEmpty(root)) {
                args.putString(EXTRA_FRAGMENT_ROOT_KEY, root);
            }

            final FragmentManager fm = getSupportFragmentManager();
            final Fragment f = fm.getFragmentFactory().instantiate(getClassLoader(),
                    getString(preferenceFragmentValue()));
            f.setArguments(args);
            // Display the fragment as the main content.
            fm.beginTransaction().replace(R.id.content_frame, f).commit();
        }
    }

    private boolean startPreference(String fragment, Bundle args, String key) {
        if (getSupportFragmentManager().isStateSaved()) {
            // Sometimes onClick can come after onPause because of being posted on the handler.
            // Skip starting new preferences in that case.
            return false;
        }
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment f = fm.getFragmentFactory().instantiate(getClassLoader(), fragment);
        if (f instanceof DialogFragment) {
            f.setArguments(args);
            ((DialogFragment) f).show(fm, key);
        } else {
            startActivity(new Intent(this, this.getClass())
                    .putExtra(EXTRA_FRAGMENT_ARGS, args));
        }
        return true;
    }

    @Override
    public boolean onPreferenceStartFragment(
            PreferenceFragmentCompat preferenceFragment, Preference pref) {
        return startPreference(pref.getFragment(), pref.getExtras(), pref.getKey());
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        Bundle args = new Bundle();
        args.putString(ARG_PREFERENCE_ROOT, pref.getKey());
        return startPreference(getString(preferenceFragmentValue()), args, pref.getKey());
    }


    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends PreferenceFragmentCompat {

        public boolean isLIBConfig() {
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
            initPreference(rootKey);
        }

        public void initPreference(String rootKey){
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
                case "LIB":
                    Log.i("Preference Logging", "LIB Found!!!!");
                    preference.setOnPreferenceClickListener(p -> {
                        Intent intent = new Intent("jOS.System.LibConfig");
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            ErrorUtils.handle(e, requireActivity());
                        }
                        return !isLIBConfig();
                    });
                    return !isLIBConfig();
                case "LIBVer":
                    Log.i("Preference Logging", "LIBVer Found!!!!");
                    preference.setSummary(BuildConfig.LIBVersion);
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

            if (!Objects.equals(currentTheme, getThemeFromDB1(getPreferenceManager().getContext()))) {
                recreateActivityNow();
            }
        }

        private void recreateActivityNow() {
            Activity activity = getActivity();
            if (activity != null) {
                activity.recreate();
            }
        }
    }
}
