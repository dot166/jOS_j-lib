package io.github.dot166.ThemeEngine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.preference.PreferenceManager;

import java.util.Objects;

import io.github.dot166.jLib.widget.ActionBar2;
import io.github.dot166.jLib.app.jConfigActivity;

public class ConfigActivity extends jConfigActivity {
    @Override
    public jLIBSettingsFragment preferenceFragment() {
        return new jThemeEngineConfigFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar2 actionBar2 = findViewById(io.github.dot166.jLib.R.id.actionbar);
        actionBar2.setTitleCentered(true);
    }

    public static class jThemeEngineConfig {

        public static final String PREF_THEME = "pref_theme";
        public static final String PREF_THEME_ENGINE_ENABLED = "pref_enableThemeEngine";
    }

    public static class jThemeEngineConfigFragment extends jLIBSettingsFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public boolean isTEConfig() {
            return true;
        }
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            super.onCreatePreferences(savedInstanceState, rootKey);
            PreferenceManager.getDefaultSharedPreferences(requireContext()).registerOnSharedPreferenceChangeListener(this);
        }
        @Override
        public int preferenceXML() {
            return R.xml.themeengine_prefs;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            switch (Objects.requireNonNull(key)) {
                case jThemeEngineConfig.PREF_THEME_ENGINE_ENABLED:
                case jThemeEngineConfig.PREF_THEME:
                    Handler handler = new Handler();
                    handler.postDelayed(this::recreateActivityNow, 2000);
                    break;
            }
        }
    }
}
