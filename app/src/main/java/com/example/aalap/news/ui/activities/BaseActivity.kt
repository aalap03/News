package com.example.aalap.news.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.aalap.news.R
import com.example.aalap.news.pref
import org.jetbrains.anko.AnkoLogger

abstract class BaseActivity: AppCompatActivity(), AnkoLogger {

    abstract fun layoutResID(): Int

    abstract fun getToolbar(): Toolbar

    abstract fun getToolbarTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResID())

        setSupportActionBar(getToolbar())
        getToolbar().title = getToolbarTitle()
        getToolbar().setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_title_color))

    }

}