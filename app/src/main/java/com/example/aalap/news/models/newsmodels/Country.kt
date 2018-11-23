package com.example.aalap.news.models.newsmodels

class Country {


    companion object {
        var countryMaps = hashMapOf<String, String>()

        private const val INDIA = "INDIA"
        private const val USA = "USA"
        private const val CANADA = "CANADA"
        private const val FRANCE = "FRANCE"
        private const val ENGLAND = "UNITED KINGDOM"

        private fun getCountries(): HashMap<String, String> {

            countryMaps[INDIA] = "in"
            countryMaps[USA] = "us"
            countryMaps[CANADA] = "ca"
            countryMaps[FRANCE] = "fr"
            countryMaps[ENGLAND] = "gb"

            return countryMaps
        }

        fun keysForSpinner(): MutableList<String> {
            val list = mutableListOf<String>()
            getCountries().keys.forEach { list.add(it) }
            return list
        }
    }
}