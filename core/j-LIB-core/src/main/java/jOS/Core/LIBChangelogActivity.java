package jOS.Core;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LIBChangelogActivity extends jWebActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configure("jOS.Core.LIBChangelogFragment", false, true, true);
        super.onCreate(savedInstanceState);
        setupBottomNavWithoutMenu(false);
    }
}
