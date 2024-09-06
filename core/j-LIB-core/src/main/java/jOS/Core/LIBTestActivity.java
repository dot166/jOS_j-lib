package jOS.Core;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.Navigation;

import jOS.Core.utils.ErrorUtils;
import jOS.Core.utils.IconUtils;
import jOS.Core.utils.LIBTest;

public class LIBTestActivity extends jActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configure(R.layout.libplaceholder, false);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setSubtitle("AAAA");
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.LIBTestFragment, R.id.LIBChangelogFragment)
                .build();
        BottomNavigationView nav = setupBottomNav(R.menu.placeholder_nav, true);
        NavController navController = Navigation.findNavController(this, R.id.content_frame);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(nav, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_favorite).setIcon(IconUtils.getActivityIcon(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            try {
                startActivity(new Intent(this, jConfigActivity.class));
            } catch (Exception e) {
                ErrorUtils.handle(e, this);
            }
            return true;
        } else if (itemId == R.id.action_favorite) {
            startActivity(new Intent(this, LIBAboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

