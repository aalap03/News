package com.example.aalap.news.ui.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

class MainActivity : BaseActivity(), MainView {

    private lateinit var presenter: Presenter
    private var widthScreen: Int = 0

    override fun layoutResID(): Int {
        return R.layout.activity_main
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "News"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(pref.getTheme())
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthScreen = displayMetrics.widthPixels

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

        val drawerItemTitle = PrimaryDrawerItem().withName("Categories").withTypeface(typeface)

        val drawerItemBusiness = PrimaryDrawerItem().withName(Category.BUSINESS).withSelectable(true).withTypeface(typeface)
        val drawerItemEntertainment = PrimaryDrawerItem().withName(Category.ENTERTAINMENT).withSelectable(true).withTypeface(typeface)
        val drawerItemGeneral = PrimaryDrawerItem().withName(Category.GENERAL).withSelectable(true).withTypeface(typeface)
        val drawerItemTechnology = PrimaryDrawerItem().withName(Category.TECHNOLOGY).withSelectable(true).withTypeface(typeface)
        val drawerItemScience = PrimaryDrawerItem().withName(Category.SCIENCE).withSelectable(true).withTypeface(typeface)
        val drawerItemSports = PrimaryDrawerItem().withName(Category.SPORTS).withSelectable(true).withTypeface(typeface)
        val drawerItemHealth = PrimaryDrawerItem().withName(Category.HEALTH).withSelectable(true).withTypeface(typeface)

        val drawer = DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .addDrawerItems(drawerItemTitle, DividerDrawerItem(),
                        drawerItemBusiness,
                        drawerItemEntertainment,
                        drawerItemGeneral,
                        drawerItemTechnology,
                        drawerItemScience,
                        drawerItemSports,
                        drawerItemHealth
                )
                .withSelectedItemByPosition(1)
                .build()

        drawer.setOnDrawerItemClickListener { _: View?, position: Int, _: IDrawerItem<*, *>? ->
            when (position) {
                2 -> presenter.getAllHeadlinesByCategory(Category.BUSINESS)
                3 -> presenter.getAllHeadlinesByCategory(Category.ENTERTAINMENT)
                4 -> presenter.getAllHeadlinesByCategory(Category.GENERAL)
                5 -> presenter.getAllHeadlinesByCategory(Category.TECHNOLOGY)
                6 -> presenter.getAllHeadlinesByCategory(Category.SCIENCE)
                7 -> presenter.getAllHeadlinesByCategory(Category.SPORTS)
                8 -> presenter.getAllHeadlinesByCategory(Category.HEALTH)
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
                presenter.getEverythingArticle(query, 1, 10)
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

        val ad =
                if (pref.getRecyclerLayout() == NewsLayout.LAYOUT_LINEAR)
                    ArticleAdapter(this, articles)
                else
                    GridArticleAdapter(this, articles, widthScreen)

        new_recycler.adapter = ad
    }
}
