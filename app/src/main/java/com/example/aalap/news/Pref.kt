package com.example.aalap.news

import android.content.Context
import android.content.SharedPreferences
import com.example.aalap.news.models.newsmodels.Layout
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

const val KEY_THEME = "theme"
const val KEY_LAYOUT = "layout"
const val KEY_LATITUDE = "latitude"
const val KEY_LONGITUDE = "longitude"
const val KEY_COUNTRY: String = "Key_country"

class Pref(appContext: Context): AnkoLogger {

    var pref: SharedPreferences = appContext.getSharedPreferences("News", Context.MODE_PRIVATE)

    fun saveLayout(layout: Int) {
        pref.edit().putInt(KEY_LAYOUT, layout).apply()
    }

    private fun getRecyclerLayout(): Int {
        return pref.getInt(KEY_LAYOUT, Layout.COMPACT.ordinal)
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

    fun isLayoutCompact(): Boolean {
        return (getRecyclerLayout() == Layout.COMPACT.ordinal)
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

    fun saveCountry(country: String) {
        info { "Country: saved $country" }
        pref.edit().putString(KEY_COUNTRY, country).apply()
    }

    fun getCountry(): String {
        info { "Country: get ${pref.getString(KEY_COUNTRY, "USA")}" }
        return pref.getString(KEY_COUNTRY, "USA")
    }


    //login test

    fun saveToken(token: String){
        pref.edit().putString("token", token).apply()
    }

    fun getToken() = pref.getString("token", "")


}