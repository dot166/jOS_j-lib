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
     * @param actionbar boolean, tells system if you would like to show the ActionBar
     */
    protected void configure(int layout, boolean home, boolean actionbar)
    {
        configure(layout, home, actionbar, true);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param actionbar boolean, tells system if you would like to show the ActionBar
     * @param app_name boolean, tells the system if you would like to show the ActionBar Title
     */
    protected void configure(int layout, boolean home, boolean actionbar, boolean app_name)
    {
        this.app_name = app_name;
        this.layout = layout;
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
        } else {
            Log.e("ActionBar2", "no actionbar found");
        }
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