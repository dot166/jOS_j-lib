package jOS.Core;

import static jOS.Core.ThemeEngine.currentTheme;
import static jOS.Core.ThemeEngine.getSystemTheme;
import static jOS.Core.ThemeEngine.getThemeFromDB1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;


public class jActivity extends AppCompatActivity {

    boolean app_name;
    boolean actionbar;
    int layout;
    boolean icon;
    boolean home;
    int Theme;
    boolean configured = false;
    protected jSDKCoreApp mSDKApp;

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
     * @param actionbar boolean, tells system if you would like to show the actionbar
     */
    protected void configure(int layout, boolean home, boolean actionbar)
    {
        configure(layout, home, actionbar, true);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the actionbar
     * @param app_name boolean, tells the system if you would like to show the activity label
     */
    protected void configure(int layout, boolean home, boolean actionbar, boolean app_name)
    {
        configure(layout, home, actionbar, app_name, true);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the actionbar
     * @param app_name boolean, tells the system if you would like to show the activity label
     * @param icon boolean, tells the system if you would like to show the activity icon
     */
    protected void configure(int layout, boolean home, boolean actionbar, boolean app_name, boolean icon)
    {
        configure(layout, home, actionbar, app_name, icon, getSystemTheme(this));
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the actionbar
     * @param app_name boolean, tells the system if you would like to show the activity label
     * @param icon boolean, tells the system if you would like to show the activity icon
     * @param Theme int, selected theme. commonly themeengine call or R.style.{insert theme here}
     */
    protected void configure(int layout, boolean home, boolean actionbar, boolean app_name, boolean icon, int Theme)
    {
        this.app_name = app_name;
        this.layout = layout;
        this.actionbar = actionbar;
        this.icon = icon;
        this.home = home;
        this.Theme = Theme;
        this.configured = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!configured)
            throw new IllegalStateException("configure() not called prior to onCreate()");

        setTheme(Theme);
        super.onCreate(savedInstanceState);
        mSDKApp = (jSDKCoreApp) this.getApplicationContext();
        mSDKApp.setCurrentActivity(this);
        setContentView(layout);
        if (actionbar) {
            setSupportActionBar(findViewById(R.id.actionbar));
        }
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!home);
            getSupportActionBar().setDisplayShowTitleEnabled(app_name);
            actionBar2((ActionBar2) toolbar);
        } else {
            Log.e("ActionBar2", "no actionbar found");
        }
    }

    /**
     * ActionBar2 exclusive features
     * @param toolbar ActionBar2
     */
    private void actionBar2(ActionBar2 toolbar) {
        toolbar.fixLogo(icon);
        toolbar.fixParameters();
        toolbar.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSDKApp.setCurrentActivity(this);
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
        Activity currActivity = mSDKApp.getCurrentActivity();
        if (this.equals(currActivity))
            mSDKApp.setCurrentActivity(null);
    }
}