package com.example.aalap.news.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.presenter.Presenter
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.EverythingView
import com.example.aalap.news.view.NewsFragmentView
import kotlinx.android.synthetic.main.news_list_frag.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.backgroundColor

class NewsEverything : BaseActivity(), NewsFragmentView {

    lateinit var presenter: Presenter
    lateinit var adapter: ArticleAdapter
    var screenWidth: Int = 0
    var currentTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        currentTitle = intent.getStringExtra("title")
        getToolbar().backgroundColor = ContextCompat.getColor(this, R.color.primary)
        getToolbar().title = currentTitle

        new_recycler.layoutManager = LinearLayoutManager(this)

        presenter = Presenter(this, NewsModel())
        presenter.getEverythingArticle(currentTitle, 0, 0)

        refresh_layout.setOnRefreshListener { presenter.getEverythingArticle(currentTitle, 0, 0) }
    }


    override fun loading(isVisible: Boolean) {
        refresh_layout.isRefreshing = isVisible
    }

    override fun showError(errorMsg: String?) {
        loading(false)
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun displayArticles(articles: List<Article>) {
        loading(false)
        adapter = ArticleAdapter(this, articles, screenWidth)
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