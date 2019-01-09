package com.example.aalap.news.models.newsmodels

import android.util.Log
import com.example.aalap.news.pref
import org.jetbrains.anko.AnkoLogger

class Country : AnkoLogger {


    companion object {
        const val TAG = "Country:"
        var countryMaps = hashMapOf<String, String>()

        private const val INDIA = "India"
        private const val USA = "USA"
        private const val CANADA = "Canada"
        private const val FRANCE = "France"
        private const val ENGLAND = "UK"

        fun countryKeyValueMap(): HashMap<String, String> {

            countryMaps[INDIA] = "in"
            countryMaps[USA] = "us"
            countryMaps[CANADA] = "ca"
            countryMaps[FRANCE] = "fr"
            countryMaps[ENGLAND] = "gb"

            return countryMaps
        }

        fun getListOfCountries(): MutableList<String> {
            val list = mutableListOf<String>()
            val savedCountry = pref.getCountry()
            list.add(savedCountry)
            val values = countryKeyValueMap().keys
            Log.d(TAG, "Saved $savedCountry")
            Log.d(TAG, "total ${values.size}")
            values.filter {
                !it.equals(savedCountry, true)
            }.forEach {
                Log.d(TAG, "Added $it")
                list.add(it)
            }
            return list
        }
    }
}