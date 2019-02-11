package com.example.aalap.news.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.SafeBrowsingResponse
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import es.dmoral.toasty.Toasty
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_webview.*

class Webview : BaseActivity() {
    override fun layoutResID(): Int {
        return R.layout.activity_webview
    }

    override fun getToolbar(): Toolbar {
        return webview_toolbar
    }

    override fun getToolbarTitle(): String {
        return intent.getStringExtra("title")
    }

    var url: String = ""
    var currentArticle: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.getStringExtra("url")
        currentArticle = Realm.getDefaultInstance().where(Article::class.java).equalTo("url", url).findFirst()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        webviewSettings()

        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
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
                    Toasty.error(view.context, "Unsafe web page blocked.").show()
                }

            }
        }

        webview.loadUrl(url)
    }

    private fun webviewSettings() {
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
