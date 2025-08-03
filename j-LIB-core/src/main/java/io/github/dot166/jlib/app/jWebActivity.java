package io.github.dot166.jlib.app;

import static io.github.dot166.jlib.app.jLIBCoreApp.TAG;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.github.dot166.jlib.R;

/**
 * @deprecated please use {@link androidx.browser.customtabs.CustomTabsIntent} instead
 */
@Deprecated(forRemoval = true, since = "4.3.0")
public class jWebActivity extends jActivity {

    protected WebView webView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    OnBackPressedCallback callback;

    String uri;
    boolean js;
    boolean DOM;
    public boolean useWebView;
    boolean allowIntentsWithUrl = true;
    protected boolean useWebPageTitleAsActivityTitle = true;

    protected void setUri(String uri)
    {
        this.uri = uri;
        allowIntentsWithUrl = false;
    }

    /**
     * note: when you call this function, the WebView is enabled, only call this function if you are using the WebView
     * @param js boolean, use javascript
     * @param DOM boolean, use WebStorage(DOM Storage) API
     */
    protected void configureWebView(boolean js, boolean DOM) {
        if (uri == null) {
            throw new IllegalStateException("setUri() not called prior to configureWebView()");
        }
        this.js = js;
        this.DOM = DOM;
        useWebView = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        configureFromIntent(getIntent().getExtras());
        if (uri == null) {
            throw new IllegalStateException("setUri() not called prior to onCreate()");
        }
        super.onCreate(savedInstanceState);
        if (useWebView) {
            setContentView(R.layout.jwebactivity);
            setSupportActionBar(findViewById(R.id.actionbar));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // turn on the back button

            webView = findViewById(R.id.web);
            progressBar = findViewById(R.id.progress);
            swipeRefreshLayout = findViewById(R.id.swipe);


            webView.clearCache(true);
            webView.getSettings().setJavaScriptEnabled(js);
            webView.getSettings().setSupportZoom(true);// Support zoom by default for accessibility
            webView.getSettings().setDomStorageEnabled(DOM);
            webView.setWebViewClient(new myWebViewclient());
            webView.loadUrl(uri);
            webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
                //checking Runtime permissions
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Do this, if permission granted
                    //getting file name from url
                    String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                    //Alertdialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //title for AlertDialog
                    builder.setTitle(R.string.download);
                    //message of AlertDialog
                    builder.setMessage(getString(R.string.do_you_want_to_save) + filename);
                    //if YES button clicks
                    builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                        //DownloadManager.Request created with url.
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        //cookie
                        String cookie = CookieManager.getInstance().getCookie(url);
                        //Add cookie and User-Agent to request
                        request.addRequestHeader("Cookie", cookie);
                        request.addRequestHeader("User-Agent", userAgent);
                        //file scanned by MediaScanner
                        request.allowScanningByMediaScanner();
                        //Download is visible and its progress, after completion too.
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        //DownloadManager created
                        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        //Saving file in Download folder
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                        //download queued
                        downloadmanager.enqueue(request);
                    });
                    //If Cancel button clicks
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        //cancel the dialog if Cancel clicks
                        dialog.cancel();
                    });
                    AlertDialog dialog = builder.create();
                    //alertdialog shows
                    dialog.show();
                } else {
                    //Do this, if there is no permission
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1
                    );
                }
            });

            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSurface}).getColor(0, 0));

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
                    }, 3000);
                }
            });

            swipeRefreshLayout.setColorSchemeColors(
                    obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorPrimary}).getColor(0, 0)
            );
            callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event.
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);

        } else {
            Uri webpage = Uri.parse(uri);
            CustomTabsIntent intent = new CustomTabsIntent.Builder()
                    .build();
            intent.intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.launchUrl(this, webpage);
            finish();
        }
    }


    public class myWebViewclient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            webView.clearCache(true);
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (request.isForMainFrame()) {
                Toast.makeText(getApplicationContext(), error.getDescription().toString(), Toast.LENGTH_LONG).show();
            }
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
            if (useWebPageTitleAsActivityTitle) {
                setTitle(webView.getTitle());
            }
            callback.setEnabled(webView.canGoBack());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureFromIntent(@Nullable Bundle extras) {
        if (extras != null) {
            String intentUrl = extras.getString("uri");
            if (intentUrl != null && allowIntentsWithUrl && !intentUrl.isEmpty()) {
                uri = intentUrl;
            }
            boolean intentJs = extras.getBoolean("js", false);
            boolean intentDOM = extras.getBoolean("DOM", false);
            boolean intentUseWebView = extras.getBoolean("useWebView", false);
            if (allowIntentsWithUrl && intentUseWebView) {
                js = intentJs;
                DOM = intentDOM;
                useWebView = true;
            }
        } else {
            Log.e(TAG, "intent extras is equal to null");
        }
    }
}
