package com.example.aalap.news.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.aalap.news.R
import kotlinx.android.synthetic.main.activity_webview.*

class Webview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setSupportActionBar(webview_toolbar)
        supportActionBar?.title = intent.getStringExtra("title")
        webview_toolbar.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        webviewSettings()

        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Toast.makeText(this@Webview, "Loading finished", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        webview.loadUrl(intent.getStringExtra("url"))
    }

    fun webviewSettings() {
        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.allowContentAccess = true
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = true
        webview.settings.useWideViewPort = true
    }

}
