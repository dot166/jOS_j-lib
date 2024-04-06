package jOS.Core;

import static jOS.Core.ThemeEngine.getSystemTheme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class jActivity extends AppCompatActivity {

    String app_name;
    int layout;
    int icon;
    boolean home;
    int Theme;
    boolean configured = false;

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
        setContentView(layout);
        ActionBar.actionBarConfig(app_name, icon, home, this);
    }
}