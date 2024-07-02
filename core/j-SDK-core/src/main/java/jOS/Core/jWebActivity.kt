package jOS.Core

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

open class jWebActivity : jActivity() {
    var webView: WebView? = null
    var progressBar: ProgressBar? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null

    var uri: String? = null
    var js: Boolean = false
    var zoom: Boolean = false
    var DOM: Boolean = false

    protected fun configure(
        uri: String?,
        js: Boolean,
        zoom: Boolean,
        DOM: Boolean,
        home: Boolean,
        actionbar: Boolean,
        app_name: Boolean = true,
        icon: Boolean = true
    ) {
        this.uri = uri
        this.js = js
        this.zoom = zoom
        this.DOM = DOM
        super.configure(R.layout.jwebactivity, home, actionbar, app_name, icon)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = findViewById(R.id.web)
        progressBar = findViewById(R.id.progress)
        swipeRefreshLayout = findViewById(R.id.swipe)


        webView!!.clearCache(true)
        webView!!.getSettings().javaScriptEnabled = js
        webView!!.getSettings().setSupportZoom(zoom)
        webView!!.getSettings().domStorageEnabled = DOM
        webView!!.setWebViewClient(myWebViewclient())
        webView!!.loadUrl(uri!!)

        swipeRefreshLayout!!.setProgressBackgroundColor(R.color.j_web_wheel_bg)

        swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            swipeRefreshLayout!!.setRefreshing(true)
            Handler().postDelayed({
                swipeRefreshLayout!!.setRefreshing(false)
                webView!!.clearCache(true)
                webView!!.loadUrl(uri!!)
            }, 3000)
        })

        swipeRefreshLayout!!.setColorSchemeColors(
            resources.getColor(android.R.color.holo_blue_dark),
            resources.getColor(android.R.color.holo_red_dark)
        )
    }


    inner class myWebViewclient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            webView!!.clearCache(true)
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            Toast.makeText(applicationContext, "No internet connection", Toast.LENGTH_LONG).show()
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            handler.cancel()
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
            progressBar!!.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar!!.visibility = View.GONE
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
