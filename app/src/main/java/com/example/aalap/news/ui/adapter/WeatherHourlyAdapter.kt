package com.example.aalap.news.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aalap.news.R
import com.example.aalap.news.models.weathermodels.HourlyData
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.AnkoLogger

class WeatherHourlyAdapter(var context: Context, var list: List<HourlyData>) :
        RecyclerView.Adapter<WeatherHourlyAdapter.HourlyHolder>(), AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        return HourlyHolder(LayoutInflater.from(context).inflate(R.layout.weather_item_hourly, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        val hourlyItem = list[position]
        holder.time.text = hourlyItem.getTimeAsHour()
        holder.icon.setImageResource(hourlyItem.getIconRes())
        holder.summary.text = hourlyItem.summary
        holder.temperature.text = hourlyItem.temperature().toString()
        holder.itemView.setOnClickListener { holder.bindClick(hourlyItem) }
    }

    inner class HourlyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.findViewById(R.id.weather_hourly_time)
        var temperature: TextView = itemView.findViewById<View>(R.id.weather_layout_hourly).findViewById(R.id.weather_temperature_value)
        var icon: ImageView = itemView.findViewById(R.id.weather_hourly_icon)
        var summary: TextView = itemView.findViewById(R.id.weather_hourly_summary)
        fun bindClick(hourlyItem: HourlyData) {
            Toasty.info(context, "@ ${hourlyItem.getTimeAsHour()} Wind chill will be  ${hourlyItem.feelsLike()}", Toast.LENGTH_LONG)
                    .show()
        }
    }
}