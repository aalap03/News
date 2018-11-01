package com.example.aalap.news.models

class Category {

    //business entertainment general health science sports technology

    companion object {
        const val BUSINESS = "Business"
        const val ENTERTAINMENT = "Entertainment"
        const val GENERAL = "General"
        const val HEALTH = "Health"
        const val SCIENCE = "Science"
        const val SPORTS = "Sports"
        const val TECHNOLOGY = "Technology"

        fun getCategories(): List<String> {
            return listOf(BUSINESS, ENTERTAINMENT, GENERAL, HEALTH, SCIENCE, SPORTS, TECHNOLOGY)
        }
    }

}