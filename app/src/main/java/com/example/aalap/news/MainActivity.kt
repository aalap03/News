package com.example.aalap.news

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aalap.news.models.Article
import com.example.aalap.news.models.Category
import com.example.aalap.news.models.NewsModel
import com.example.aalap.news.presenter.Presenter
import com.example.aalap.news.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem

val TAG = "MainActivity:"

class MainActivity : AppCompatActivity(), MainView {
    override fun showError(errorMsg: String?) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun addNews(articles: List<Article>?) {
        val adapter = articles?.let { ArticleAdapter(this, it) }
        new_recycler.adapter = adapter
    }

    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        new_recycler.layoutManager = LinearLayoutManager(this)

        presenter = Presenter(this, NewsModel())
        presenter.getAllHeadlinesByCountry()


        setUpDrawer()

    }

    private fun setUpDrawer() {

        val drawerItemBusiness1 = PrimaryDrawerItem().withName("Categories")
        val drawerItemBusiness = PrimaryDrawerItem().withName(Category.BUSINESS).withSelectable(true)
        val drawerItemEntertainment = PrimaryDrawerItem().withName(Category.ENTERTAINMENT).withSelectable(true)
        val drawerItemGeneral = PrimaryDrawerItem().withName(Category.GENERAL).withSelectable(true)

        val drawer = DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withTranslucentNavigationBar(true)
                .addDrawerItems(drawerItemBusiness1, drawerItemBusiness, drawerItemEntertainment, drawerItemGeneral)
                .withSelectedItemByPosition(1)
                .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.menu_settings -> startActivity(Intent(this, NewsSettings::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
