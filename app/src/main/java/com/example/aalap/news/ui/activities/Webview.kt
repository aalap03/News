package com.example.aalap.news.ui.activities

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.SafeBrowsingResponse
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_webview.*

class Webview : AppCompatActivity() {

    var url: String = ""
    var currentArticle: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setSupportActionBar(webview_toolbar)
        supportActionBar?.title = intent.getStringExtra("title")
        url = intent.getStringExtra("url")
        webview_toolbar.setTitleTextColor(Color.WHITE)

        currentArticle = Realm.getDefaultInstance().where(Article::class.java).equalTo("url", url).findFirst()

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

            override fun onSafeBrowsingHit(
                    view: WebView,
                    request: WebResourceRequest,
                    threatType: Int,
                    callback: SafeBrowsingResponse
            ) {
                // The "true" argument indicates that your app reports incidents like
                // this one to Safe Browsing.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    callback.backToSafety(true)
                    Toast.makeText(view.context, "Unsafe web page blocked.", Toast.LENGTH_LONG).show()
                }

            }
        }

        webview.loadUrl(url)
    }

    fun webviewSettings() {
        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.allowContentAccess = true
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = true
        webview.settings.useWideViewPort = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_webview, menu)
        menu?.findItem(R.id.menu_bookmark)?.setIcon(if (currentArticle?.isSaved == true) R.drawable.ic_saved else R.drawable.ic_not_saved)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_bookmark -> {
                Realm.getDefaultInstance().executeTransaction {
                    currentArticle?.isSaved = !currentArticle?.isSaved!!
                    item.setIcon(if (currentArticle?.isSaved == true) R.drawable.ic_saved else R.drawable.ic_not_saved)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
