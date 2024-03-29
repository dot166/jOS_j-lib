package jOS.Core;

import static jOS.Core.ThemeEngine.currentTheme;
import static jOS.Core.ThemeEngine.getSystemTheme;
import static jOS.Core.ThemeEngine.getThemeFromDB1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartScreenCallback;
import androidx.preference.PreferenceScreen;

import java.util.Objects;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
public class jConfigActivity extends jActivity
        implements OnPreferenceStartFragmentCallback, OnPreferenceStartScreenCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    public static final String SAVE_HIGHLIGHTED_KEY = "android:preference_highlighted";

    @VisibleForTesting
    static final String EXTRA_FRAGMENT = ":settings:fragment";
    @VisibleForTesting
    static final String EXTRA_FRAGMENT_ARGS = ":settings:fragment_args";
    public int appName() {
        return R.string.settings_name;
    }
    public int appIcon() {
        return R.drawable.ic_launcher_j;
    }
    public int appTheme(Context context) {
        if (getAppTheme(context) != 0) {
            return getAppTheme(context);
        }
        return getSystemTheme(context);
    }
    public String appDB1(Context context) {
        if (!Objects.equals(getAppDB1(context), "")) {
            return getAppDB1(context);
        }
        return getThemeFromDB1(context);
    }
    public String appCurrentTheme() {
        if (!Objects.equals(getAppCurrentTheme(), "")) {
            return getAppCurrentTheme();
        }
        return currentTheme;
    }
    public int getAppTheme(Context context) {
        return 0;
    }
    public String getAppDB1(Context context) {
        return "";
    }
    public String getAppCurrentTheme() {
        return "";
    }
    public int preferenceFragmentValue() {
        return R.string.settings_fragment_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(appName(), R.layout.settings_activity, false, appIcon(), "", appTheme(this));
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Intent intent = getIntent();

        if (savedInstanceState == null) {
            Bundle args = intent.getBundleExtra(EXTRA_FRAGMENT_ARGS);
            if (args == null) {
                args = new Bundle();
            }

            String prefKey = intent.getStringExtra(EXTRA_FRAGMENT_ARG_KEY);
            if (!TextUtils.isEmpty(prefKey)) {
                args.putString(EXTRA_FRAGMENT_ARG_KEY, prefKey);
            }

            final FragmentManager fm = getSupportFragmentManager();
            final Fragment f = fm.getFragmentFactory().instantiate(getClassLoader(),
                    getPreferenceFragment());
            f.setArguments(args);
            // Display the fragment as the main content.
            fm.beginTransaction().replace(R.id.content_frame, f).commit();
        }

    }

    public static void reboot(AppCompatActivity context){
        Intent intent = context.getIntent();
        context.finish();
        context.startActivity(intent);
    }

    /**
     * Obtains the preference fragment to instantiate in this activity.
     *
     * @return the preference fragment class
     * @throws IllegalArgumentException if the fragment is unknown to this activity
     */
    private String getPreferenceFragment() {
        String preferenceFragment = getIntent().getStringExtra(EXTRA_FRAGMENT);
        String defaultFragment = getString(preferenceFragmentValue());

        if (TextUtils.isEmpty(preferenceFragment)) {
            return defaultFragment;
        } else if (!preferenceFragment.equals(defaultFragment)) {
            throw new IllegalArgumentException(
                    "Invalid fragment for this activity: " + preferenceFragment);
        } else {
            return preferenceFragment;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
                    .putExtra(EXTRA_FRAGMENT, fragment)
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
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        return startPreference(getString(preferenceFragmentValue()), args, pref.getKey());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        if (!Objects.equals(appCurrentTheme(), appDB1(this))) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {
        super.addMenuProvider(provider, owner, state);
    }

    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends PreferenceFragmentCompat {

        private String mHighLightKey;
        private boolean mPreferenceHighlighted = false;
        public boolean isSDKConfig() {
            return false;
        }
        public boolean hideSDK() {
            return false;
        }
        public int preferenceXML() {
            return R.xml.launcher_preferences;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            final Bundle args = getArguments();
            mHighLightKey = args == null ? null : args.getString(EXTRA_FRAGMENT_ARG_KEY);
            if (rootKey == null && !TextUtils.isEmpty(mHighLightKey)) {
                rootKey = getParentKeyForPref(mHighLightKey);
            }

            if (savedInstanceState != null) {
                mPreferenceHighlighted = savedInstanceState.getBoolean(SAVE_HIGHLIGHTED_KEY);
            }
            initPreference(rootKey);
        }

        public void initPreference(String rootKey){
            setPreferencesFromResource(preferenceXML(), rootKey);
            if (!hideSDK()) {
                addPreferencesFromResource(R.xml.sdk_preference);
            }
            PreferenceScreen screen = getPreferenceScreen();
            for (int i = screen.getPreferenceCount() - 1; i >= 0; i--) {
                Preference preference = screen.getPreference(i);
                if (Objects.equals(preference.getKey(), "sdk_category")) {
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
        }

        /**
         * Initializes a preference. This is called for every preference. Returning false here
         * will remove that preference from the list.
         */
        protected boolean configPreference(Preference preference) {
            Log.i("Preference Logging", preference.getKey());
            switch (preference.getKey()) {
                case "SDK":
                    Log.i("Preference Logging", "SDK Found!!!!");
                    preference.setOnPreferenceClickListener(p -> {
                        Intent intent = new Intent("jOS.System.SDKConfig");
                        startActivity(intent);
                        return !isSDKConfig();
                    });
                    return !isSDKConfig();
                case "SDKVer":
                    Log.i("Preference Logging", "SDKVer Found!!!!");
                    preference.setSummary(BuildConfig.SDKVersion);
                    return true;
            }
            return extraPrefs(preference);
        }

        protected boolean extraPrefs(Preference preference) {
            return true;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
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
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBoolean(SAVE_HIGHLIGHTED_KEY, mPreferenceHighlighted);
        }

        protected String getParentKeyForPref(String key) {
            return null;
        }
    }
}
