package jOS.Core;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class jWebFragment extends Fragment {

    public static WebView webView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    String uri;
    boolean js;
    boolean DOM;
    boolean configured = false;

    protected void configure(String uri, boolean js, boolean DOM)
    {
        this.uri = uri;
        this.js = js;
        this.DOM = DOM;
        this.configured = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!configured) {
            uri = "https://dot166.github.io";
            js = false;
            DOM = false;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_j_web, container, false);

        webView = view.findViewById(R.id.web);
        progressBar = view.findViewById(R.id.progress);
        swipeRefreshLayout = view.findViewById(R.id.swipe);


        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(js);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(DOM);
        webView.setWebViewClient(new webViewclient());
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
        return view;
    }


    public class webViewclient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.clearCache(true);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show();
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
}