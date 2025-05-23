package io.github.dot166.themeengine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.wallpaper.picker.AppbarFragment.AppbarFragmentHost;
import com.android.wallpaper.picker.BottomActionBarFragment;
import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.BottomActionBar.BottomActionBarHost;

import io.github.dot166.jlib.app.jActivity;
import io.github.dot166.themeengine.flags.Flags;

/**
 *  Main Activity allowing containing view sections for the user to switch between the different
 *  Fragments providing customization options.
 */
public class ThemeEngineActivity extends jActivity implements
        AppbarFragmentHost, BottomActionBarHost {

    private BottomActionBar mBottomActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theme_engine);
        mBottomActionBar = findViewById(R.id.bottom_actionbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ThemeEngineFragment.newInstance(getString(R.string.app_name)))
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof BottomActionBarFragment
                && ((BottomActionBarFragment) fragment).onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public BottomActionBar getBottomActionBar() {
        return mBottomActionBar;
    }

    @Override
    public void onUpArrowPressed() {
        // TODO(b/189166781): Remove interface AppbarFragmentHost#onUpArrowPressed.
        onBackPressed();
    }

    @Override
    public boolean isUpArrowSupported() {
        return true;
    }
}
