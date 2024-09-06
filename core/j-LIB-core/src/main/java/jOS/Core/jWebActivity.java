package jOS.Core;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class jWebActivity extends jActivity {

    String fragment;

    protected void configure(String fragment, boolean home, boolean actionbar, boolean app_name)
    {
        this.fragment = fragment;
        super.configure(R.layout.jwebactivity, home, actionbar, app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FragmentManager fm = getSupportFragmentManager();
        final Fragment f = fm.getFragmentFactory().instantiate(getClassLoader(),
                fragment);
        // Display the fragment as the main content.
        fm.beginTransaction().replace(R.id.content_frame, f).commit();
    }
}
