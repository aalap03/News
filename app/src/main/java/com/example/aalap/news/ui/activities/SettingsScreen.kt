package com.example.aalap.news.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Country
import com.example.aalap.news.models.newsmodels.NewsLayout
import kotlinx.android.synthetic.main.settings_screen.*
import kotlinx.android.synthetic.main.toolbar_template.*

var settingsChanged = false

class SettingsScreen : BaseActivity() {

    var countryList = Country.getListOfCountries()


    override fun layoutResID(): Int {
        return R.layout.settings_screen
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "News Settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(pref.getTheme())
        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, countryList)
        settings_country.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switch_theme.isChecked = pref.isDarkTheme()
        switch_articles_layout.isChecked = pref.isLayoutCompact()


        settings_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val country = countryList[position]
                pref.saveCountry(country)
            }
        }

        switch_theme.setOnCheckedChangeListener { _, isChecked ->
            pref.saveTheme(if (isChecked) R.style.AppTheme_Dark else R.style.AppTheme)
            this.recreate()
            settingsChanged = true
        }

        switch_articles_layout.setOnCheckedChangeListener { _, isChecked ->
            pref.saveLayout(if (isChecked) NewsLayout.LAYOUT_COMPACT else NewsLayout.LAYOUT_GRID)
            settingsChanged = true
        }

        root_switch_mode.setOnClickListener {
            val isChecked = switch_articles_layout.isChecked
            switch_articles_layout.isChecked = !isChecked
            pref.saveLayout(if(switch_articles_layout.isChecked) NewsLayout.LAYOUT_COMPACT else NewsLayout.LAYOUT_GRID)
            settingsChanged = true
        }

        root_switch_theme.setOnClickListener {
            val isChecked = switch_theme.isChecked
            switch_theme.isChecked = !isChecked
            pref.saveTheme(if (isChecked) NewsLayout.LAYOUT_COMPACT else NewsLayout.LAYOUT_GRID)
            settingsChanged = true
        }
    }
}