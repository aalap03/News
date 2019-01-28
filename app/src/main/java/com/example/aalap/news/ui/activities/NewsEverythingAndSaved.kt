package com.example.aalap.news.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.presenter.NewsPresenter
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.NewsListView
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.news_list_frag.*
import kotlinx.android.synthetic.main.toolbar_template.*

class NewsEverythingAndSaved : BaseActivity(), NewsListView {

    lateinit var presenter: NewsPresenter
    lateinit var adapter: ArticleAdapter
    var screenWidth: Int = 0
    var currentTitle = ""
    var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        isSaved = intent.getBooleanExtra("saved", false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        new_recycler.layoutManager = LinearLayoutManager(this)

        if (isSaved) {
            getToolbar().title = "Saved News"
            displayArticlesR(Realm.getDefaultInstance().where(Article::class.java).equalTo("isSaved", true).sort("publishedAt", Sort.DESCENDING).findAll())
        } else {
            currentTitle = intent.getStringExtra("title")
            getToolbar().title = currentTitle
            presenter = NewsPresenter(this, NewsModel())
            presenter.getEverythingNews(currentTitle)
        }

        if (!isSaved)
            refresh_layout.setOnRefreshListener { presenter.getEverythingNews(currentTitle) }
    }

    override fun loading(isVisible: Boolean) {
        refresh_layout.isRefreshing = isVisible
    }

    override fun showError(errorMsg: String?) {
        loading(false)
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun displayArticlesR(articles: List<Article>?) {
        loading(false)
        adapter = articles?.let { ArticleAdapter(this, it, screenWidth) }!!
        new_recycler.adapter = adapter
    }

    override fun layoutResID(): Int {
        return R.layout.news_list_frag
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return intent?.getStringExtra("title") ?: "Bro..."
    }
}