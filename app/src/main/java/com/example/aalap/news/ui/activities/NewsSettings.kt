package com.example.aalap.news.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.example.aalap.news.R
import com.example.aalap.news.models.Country
import com.example.aalap.news.models.NewsLayout
import com.example.aalap.news.pref
import kotlinx.android.synthetic.main.settings_screen.*
import kotlinx.android.synthetic.main.toolbar_template.*


class NewsSettings : BaseActivity() {

    override fun layoutResID(): Int {
        return R.layout.settings_screen
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "News Settings"
    }

    var countryList = Country.keysForSpinner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, countryList)
        settings_country.adapter = adapter

        settings_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val country = countryList[position]

                Toast.makeText(this@NewsSettings, country, Toast.LENGTH_SHORT)
                        .show()
            }
        }

        switch_theme.isChecked = pref.isDarkTheme()
        switch_articles_layout.isChecked = pref.isLayoutGrid()

        switch_theme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                pref.saveTheme(R.style.AppTheme_Dark)
            else
                pref.saveTheme(R.style.AppTheme)

            this.recreate()
        }

        switch_articles_layout.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                pref.saveLayout(NewsLayout.LAYOUT_GRID)
            else
                pref.saveLayout(NewsLayout.LAYOUT_LINEAR)
        }


    }
}