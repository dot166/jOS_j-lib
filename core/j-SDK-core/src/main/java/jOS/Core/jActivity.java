package jOS.Core;

import static jOS.Core.ThemeEngine.currentTheme;
import static jOS.Core.ThemeEngine.getSystemTheme;
import static jOS.Core.ThemeEngine.getThemeFromDB1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class jActivity extends AppCompatActivity {

    String app_name;
    int layout;
    int icon;
    boolean home;
    int Theme;
    boolean configured = false;
    protected jSDKCoreApp mSDKApp;

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     */
    protected void configure(String app_name, int layout, boolean home)
    {
        configure(app_name, layout, home, R.drawable.ic_launcher_j);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param icon int, drawable or mipmap resource. commonly R.mipmap.ic_launcher or R.drawable.ic_launcher_j
     */
    protected void configure(String app_name, int layout, boolean home, int icon)
    {
        configure(app_name, layout, home, icon, getSystemTheme(this));
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param layout int, app layout. commonly R.layout.activitymain
     * @param home boolean, tells system if this is the first activity/home page
     * @param icon int, drawable or mipmap resource. commonly R.mipmap.ic_launcher or R.drawable.ic_launcher_j
     */
    protected void configure(String app_name, int layout, boolean home, int icon, int Theme)
    {
        this.app_name = app_name;
        this.layout = layout;
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