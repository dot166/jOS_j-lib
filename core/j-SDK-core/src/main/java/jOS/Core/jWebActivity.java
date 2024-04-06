package jOS.Core;

import static jOS.Core.ThemeEngine.getSystemTheme;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class jWebActivity extends jActivity {

    WebView webView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    String uri;
    boolean js;
    boolean zoom;
    boolean DOM;

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param home boolean, tells system if this is the first activity/home page
     */
    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, String app_name, boolean home)
    {
        configure(uri, js, zoom, DOM, app_name, home, R.drawable.ic_launcher_j);
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param home boolean, tells system if this is the first activity/home page
     * @param icon int, drawable or mipmap resource. commonly R.mipmap.ic_launcher or R.drawable.ic_launcher_j
     */
    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, String app_name, boolean home, int icon)
    {
        configure(uri, js, zoom, DOM, app_name, home, icon, getSystemTheme(this));
    }

    /**
     * Subclasses are obligated to call this before calling super.onCreate()
     * @param app_name int, string resource. commonly R.string.app_name
     * @param home boolean, tells system if this is the first activity/home page
     * @param icon int, drawable or mipmap resource. commonly R.mipmap.ic_launcher or R.drawable.ic_launcher_j
     */
    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, String app_name, boolean home, int icon, int Theme)
    {
        this.uri = uri;
        this.js = js;
        this.zoom = zoom;
        this.DOM = DOM;
        super.configure(app_name, R.layout.jwebactivity, home, icon, Theme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = findViewById(R.id.web);
        progressBar = findViewById(R.id.progress);
        swipeRefreshLayout = findViewById(R.id.swipe);


        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(js);
        webView.getSettings().setSupportZoom(zoom);
        webView.getSettings().setDomStorageEnabled(DOM);
        webView.setWebViewClient(new myWebViewclient());
        webView.loadUrl(uri);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.clearCache(true);
                        webView.loadUrl(uri);
                    }
                },  3000);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
    }


    public class myWebViewclient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.clearCache(true);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
