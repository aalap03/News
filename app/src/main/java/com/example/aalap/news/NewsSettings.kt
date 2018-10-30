package com.example.aalap.news

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aalap.news.models.Country
import kotlinx.android.synthetic.main.settings_screen.*


class NewsSettings: AppCompatActivity() {

    var countryList = Country.keysForSpinner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)

        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, countryList)
        settings_country.adapter = adapter

        settings_country.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val country = countryList[position]

                Toast.makeText(this@NewsSettings, country, Toast.LENGTH_SHORT)
                        .show()
            }
        }

    }
}