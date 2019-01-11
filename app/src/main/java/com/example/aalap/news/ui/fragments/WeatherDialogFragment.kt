package com.example.aalap.news.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aalap.news.R
import com.example.aalap.news.Utils
import com.example.aalap.news.models.weathermodels.HourlyData
import com.example.aalap.news.ui.adapter.WeatherHourlyAdapter
import kotlinx.android.synthetic.main.hourly_weather_list.*
import org.jetbrains.anko.windowManager

class WeatherDialogFragment : DialogFragment() {

    companion object {
        var list = mutableListOf<HourlyData>()
        fun newInstance(list: MutableList<HourlyData>): DialogFragment {
            this.list = list
            return WeatherDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.hourly_weather_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourly_recycler.layoutManager = LinearLayoutManager(context)
        val weatherHourlyAdapter = WeatherHourlyAdapter(requireContext(), list)
        hourly_recycler.adapter = weatherHourlyAdapter
        hourly_recycler.addItemDecoration(Utils().Decorator(requireContext()))
    }
}