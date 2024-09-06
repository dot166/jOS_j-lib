package jOS.Core;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LIBChangelogFragment extends jWebFragment {

    public LIBChangelogFragment() {
        configure("https://github.com/dot166/jOS_j-lib/commits/v" + BuildConfig.LIBVersion, true, true);
    }
}