package io.github.dot166.themeengine;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceManager;

import com.android.wallpaper.picker.AppbarFragment;

import java.util.Objects;

import io.github.dot166.jlib.widget.ActionBar2;
import io.github.dot166.jlib.app.jConfigActivity;

public class ConfigActivity extends jConfigActivity implements AppbarFragment.AppbarFragmentHost {
    @Override
    public jLIBSettingsFragment preferenceFragment() {
        return new jThemeEngineConfigFragment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar2 actionBar2 = findViewById(io.github.dot166.jlib.R.id.actionbar);
        actionBar2.setTitleCentered(true);
        actionBar2.setVisibility(GONE);
        addContentView(LayoutInflater.from(this).inflate(R.layout.bottom_action_bar, (ViewGroup)findViewById(io.github.dot166.jlib.R.id.content_frame).getParent()), null);
        getSupportFragmentManager().beginTransaction().replace(io.github.dot166.jlib.R.id.content_frame, new ThemeEngineFragment()).commit();
    }

    @Override
    public void onUpArrowPressed() {
        finish();
    }

    @Override
    public boolean isUpArrowSupported() {
        return true;
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
