package com.example.aalap.news.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.aalap.news.models.newsmodels.Category
import layout.NewsListFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private var categories = Category.values()

    override fun getItem(position: Int): Fragment {
        return NewsListFragment.newInstance(categories[position].name)
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].name.toUpperCase()
    }
}