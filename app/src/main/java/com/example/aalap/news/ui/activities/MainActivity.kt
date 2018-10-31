package com.example.aalap.news.ui.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aalap.news.ui.adapter.GridArticleAdapter
import com.example.aalap.news.R
import com.example.aalap.news.models.Article
import com.example.aalap.news.models.Category
import com.example.aalap.news.models.NewsLayout
import com.example.aalap.news.models.NewsModel
import com.example.aalap.news.pref
import com.example.aalap.news.presenter.Presenter
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.info

val TAG = "MainActivity:"

//App TODO s
//1) Provide themes
//2) Provide news with linear and grid both layouts.

class MainActivity : BaseActivity(), MainView {

    override fun layoutResID(): Int {
        return R.layout.activity_main
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "News"
    }

    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(pref.getTheme())
        super.onCreate(savedInstanceState)

        new_recycler.layoutManager =
                if (pref.getRecyclerLayout() == NewsLayout.LAYOUT_LINEAR) LinearLayoutManager(this)
                else GridLayoutManager(this, 2)
        presenter = Presenter(this, NewsModel())

        refresh_layout.setOnRefreshListener {
            presenter.getAllHeadlinesByCountry()
        }
        setUpDrawer()
    }

    override fun onResume() {
        super.onResume()
        new_recycler.layoutManager = if (pref.getRecyclerLayout() == NewsLayout.LAYOUT_LINEAR) LinearLayoutManager(this) else GridLayoutManager(this, 2)
        presenter.getAllHeadlinesByCountry()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    private fun setUpDrawer() {
        val typeface: Typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            resources.getFont(R.font.lato_regular)
        else
            Typeface.createFromAsset(assets, "lato_regular.ttf")

        val drawerItemBusiness1 = PrimaryDrawerItem().withName("Categories").withTypeface(typeface)
        val drawerItemBusiness = PrimaryDrawerItem().withName(Category.BUSINESS).withSelectable(true).withTypeface(typeface)
        val drawerItemEntertainment = PrimaryDrawerItem().withName(Category.ENTERTAINMENT).withSelectable(true).withTypeface(typeface)
        val drawerItemGeneral = PrimaryDrawerItem().withName(Category.GENERAL).withSelectable(true).withTypeface(typeface)

        val drawer = DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .addDrawerItems(drawerItemBusiness1, DividerDrawerItem(), drawerItemBusiness, drawerItemEntertainment, drawerItemGeneral)
                .withSelectedItemByPosition(1)
                .build()

        drawer.setOnDrawerItemClickListener { _: View?, position: Int, _: IDrawerItem<*, *>? ->
            when (position) {
                1 -> presenter.getAllHeadlinesByCategory(Category.BUSINESS)
                2 -> presenter.getAllHeadlinesByCategory(Category.ENTERTAINMENT)
                3 -> presenter.getAllHeadlinesByCategory(Category.GENERAL)
            }
            drawer.closeDrawer()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val menuSearch = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        menuSearch.queryHint = "Search Anything"

        menuSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.getEverythingArticle(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.menu_settings -> startActivity(Intent(this, NewsSettings::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun loading(isVisible: Boolean) {
        refresh_layout.isRefreshing = isVisible
    }

    override fun showError(errorMsg: String?) {
        refresh_layout.isRefreshing = false
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun displayArticles(articles: List<Article>) {

        info { "articles: ${articles.size}" }
        refresh_layout.isRefreshing = false
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val ad =
                if (pref.getRecyclerLayout() == NewsLayout.LAYOUT_LINEAR) ArticleAdapter(this, articles)
                else GridArticleAdapter(this, articles, displayMetrics.widthPixels)
        new_recycler.adapter = ad
    }
}
