package com.example.aalap.news

import android.content.Context
import android.content.SharedPreferences
import com.example.aalap.news.models.newsmodels.NewsLayout

const val KEY_THEME = "theme"
const val KEY_LAYOUT = "layout"
const val KEY_LATITUDE = "latitude"
const val KEY_LONGITUDE = "longitude"

class Pref(appContext: Context) {

    var pref: SharedPreferences = appContext.getSharedPreferences("News", Context.MODE_PRIVATE)

    fun saveLayout(layout: Int) {
        pref.edit().putInt(KEY_LAYOUT, layout).apply()
    }

    fun getRecyclerLayout(): Int {
        return pref.getInt(KEY_LAYOUT, NewsLayout.LAYOUT_LINEAR)
    }

    fun saveTheme(themeRes: Int) {
        pref.edit().putInt(KEY_THEME, themeRes).apply()
    }

    fun getTheme(): Int {
        return pref.getInt(KEY_THEME, R.style.AppTheme)
    }

    fun isDarkTheme(): Boolean {
        return (getTheme() == R.style.AppTheme_Dark)
    }

    fun isLayoutGrid(): Boolean {
        return (getRecyclerLayout() == NewsLayout.LAYOUT_GRID)
    }

    fun saveLastCoOrdinates(latitude: String, longitude: String) {
        pref.edit().putString(KEY_LATITUDE, latitude).apply()
        pref.edit().putString(KEY_LONGITUDE, longitude).apply()
    }

    fun getLatitude(): Double{
        return pref.getString(KEY_LATITUDE, "0.0").toDouble()
    }

    fun getLongitude(): Double{
        return pref.getString(KEY_LONGITUDE, "0.0").toDouble()
    }

}