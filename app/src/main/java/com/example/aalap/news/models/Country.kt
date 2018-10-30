package com.example.aalap.news.models

import android.util.Log

class Country {


    companion object {
        var countryMaps = hashMapOf<String, String>()

        val INDIA = "INDIA"
        val USA = "USA"
        val CANADA = "CANADA"
        val FRANCE = "FRANCE"
        val ENGLAND = "UNITED KINGDOM"

        fun getCountries(): HashMap<String, String> {

            countryMaps[INDIA] = "in"
            countryMaps[USA] = "us"
            countryMaps[CANADA] = "ca"
            countryMaps[FRANCE] = "fr"
            countryMaps[ENGLAND] = "gb"

            return countryMaps
        }

        fun keysForSpinner(): MutableList<String> {
            var list = mutableListOf<String>()
            getCountries().keys.forEach {
                Log.d(TAG, it)
                list.add(it)
            }
            return list
        }

        val TAG = "Country:"
    }
}