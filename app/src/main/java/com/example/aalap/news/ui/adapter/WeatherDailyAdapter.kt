package com.example.aalap.news.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aalap.news.R
import com.example.aalap.news.models.weathermodels.DailyData
import org.jetbrains.anko.AnkoLogger

class WeatherDailyAdapter(var context: Context, var list: List<DailyData>) :
        RecyclerView.Adapter<WeatherDailyAdapter.DailyHolder>(), AnkoLogger{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        return DailyHolder(LayoutInflater.from(context).inflate(R.layout.weather_item_daily, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DailyHolder, position: Int) {
        val dailyItem = list[position]
        holder.dayName.text = dailyItem.getDayOfTheWeek()
//        info { dailyItem.getIconRes(dailyItem.icon) }
        holder.icon.setImageResource(dailyItem.getIconRes())
        holder.tempMax.text = dailyItem.temperatureMax().toString()
        holder.tempMin.text = dailyItem.temperatureMin().toString()

    }

    inner class DailyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tempMin : TextView = itemView.findViewById<View>(R.id.weather_daily_temp_min).findViewById(R.id.weather_temperature_value)
        var tempMax : TextView = itemView.findViewById<View>(R.id.weather_daily_temp_max).findViewById(R.id.weather_temperature_value)
        var icon: ImageView = itemView.findViewById(R.id.weather_daily_icon)
        var dayName: TextView = itemView.findViewById(R.id.weather_daily_day_name)
    }

}