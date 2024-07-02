package jOS.Core;

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

    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, boolean home, boolean actionbar)
    {
        configure(uri, js, zoom, DOM, home, actionbar, true);
    }

    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, boolean home, boolean actionbar, boolean app_name)
    {
        configure(uri, js, zoom, DOM, home, actionbar, app_name, true);
    }

    protected void configure(String uri, boolean js, boolean zoom, boolean DOM, boolean home, boolean actionbar, boolean app_name, boolean icon)
    {
        this.uri = uri;
        this.js = js;
        this.zoom = zoom;
        this.DOM = DOM;
        super.configure(R.layout.jwebactivity, home, actionbar, app_name, icon);
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

        swipeRefreshLayout.setProgressBackgroundColor(R.color.j_web_wheel_bg);

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
