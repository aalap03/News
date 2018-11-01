package com.example.aalap.news.ui.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.aalap.news.R
import com.example.aalap.news.ui.adapter.CategoryPagerAdapter
import kotlinx.android.synthetic.main.category_tabs_activity.*
import kotlinx.android.synthetic.main.toolbar_template.*

class CategoryTabActivity: BaseActivity() {

    override fun layoutResID(): Int {
        return R.layout.category_tabs_activity
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "Categories"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category_view_pager.adapter = CategoryPagerAdapter(supportFragmentManager)
        category_tab.setupWithViewPager(category_view_pager)

    }
}