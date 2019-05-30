package com.example.aalap.news.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.transition.Explode
import com.example.aalap.news.Pref
import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Layout
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

abstract class BaseActivity : AppCompatActivity(), AnkoLogger {

    abstract fun layoutResID(): Int

    abstract fun getToolbar(): Toolbar

    abstract fun getToolbarTitle(): String

    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = Pref(this.applicationContext)
        setTheme(pref.getTheme())
        super.onCreate(savedInstanceState)
        setContentView(layoutResID())
        getToolbar().title = getToolbarTitle()
        getToolbar().setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title_color))
        setSupportActionBar(getToolbar())

        info { "Compact: ${Layout.COMPACT.ordinal}" }
        info { "Grid: ${Layout.GRID.ordinal}" }
    }

}