package com.example.aalap.news.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.aalap.news.models.Category
import layout.NewsListFragment

class CategoryPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    var categories = Category.getCategories()

    override fun getItem(position: Int): Fragment {
        return NewsListFragment.newInstance(categories[position])
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].toUpperCase()
    }
}