package jOS.Core;

import static jOS.Core.ThemeEngine.ThemeEngine.currentTheme;
import static jOS.Core.ThemeEngine.ThemeEngine.getSystemTheme;
import static jOS.Core.ThemeEngine.ThemeEngine.getThemeFromDB1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import jOS.Core.utils.VersionUtils;


public class jActivity extends AppCompatActivity {

    boolean actionbar;
    View layout;
    int layoutId;
    boolean home;
    boolean configured = false;
    protected jLIBCoreApp mLIBApp;

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout View, app layout.
     * @param home boolean, tells system if this is the first activity/home page
     */
    protected void configure(View layout, boolean home)
    {
        configure(layout, home, true);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout View, app layout.
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the ActionBar
     */
    protected void configure(View layout, boolean home, boolean actionbar)
    {
        this.layout = layout;
        this.layoutId = 0; // layout is added from view so id is set to null
        this.actionbar = actionbar;
        this.home = home;
        this.configured = true;
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     */
    protected void configure(int layout, boolean home)
    {
        configure(layout, home, true);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the ActionBar
     */
    protected void configure(int layout, boolean home, boolean actionbar)
    {
        this.layout = null; // layout is inflated from id so layout view is set to null
        this.layoutId = layout;
        this.actionbar = actionbar;
        this.home = home;
        this.configured = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!configured)
            throw new IllegalStateException("configure() not called prior to onCreate()");

        setTheme(getSystemTheme(this));
        super.onCreate(savedInstanceState);
        mLIBApp = (jLIBCoreApp) this.getApplicationContext();
        mLIBApp.setCurrentActivity(this);
        if (layout == null && layoutId != 0) {
            layout = LayoutInflater.from(this).inflate(layoutId, null);
        }
        if (layout != null) {
            setContentView(layout);
        }

        if (VersionUtils.isAtLeastV()) {
            // Fix A15 EdgeToEdge
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime()
                                | WindowInsetsCompat.Type.displayCutout());
                int statusBarHeight = getWindow().getDecorView().getRootWindowInsets()
                        .getInsets(WindowInsetsCompat.Type.statusBars()).top;
                // Apply the insets paddings to the view.
                v.setPadding(insets.left, statusBarHeight, insets.right, insets.bottom);

                // Return CONSUMED if you don't want the window insets to keep being
                // passed down to descendant views.
                return WindowInsetsCompat.CONSUMED;
            });
        }

        if (actionbar) {
            setSupportActionBar(findViewById(R.id.actionbar));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLIBApp.setCurrentActivity(this);
        if (!Objects.equals(currentTheme, getThemeFromDB1(this))) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mLIBApp.getCurrentActivity();
        if (this.equals(currActivity))
            mLIBApp.setCurrentActivity(null);
    }
}
