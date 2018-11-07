package com.example.aalap.news.ui.activities

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.aalap.news.R
import com.example.aalap.news.ui.adapter.CategoryPagerAdapter
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.category_tabs_activity.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.info
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.listItems
import com.example.aalap.news.models.weathermodels.Currently
import com.example.aalap.news.models.weathermodels.DailyData
import com.example.aalap.news.models.weathermodels.HourlyData
import com.example.aalap.news.models.weathermodels.WeatherModel
import com.example.aalap.news.presenter.WeatherPresenter
import com.example.aalap.news.ui.adapter.WeatherDailyAdapter
import com.example.aalap.news.ui.adapter.WeatherHourlyAdapter
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_weather_layout.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


const val LOCATION_PERMISSION = 1

class CategoryTabActivity : BaseActivity(), MainView {

    private lateinit var locationRequest: LocationRequest
    lateinit var manager: LocationManager
    private lateinit var dailyAdapter: WeatherDailyAdapter
    private lateinit var weatherPresenter: WeatherPresenter
    private lateinit var locationProvider: ReactiveLocationProvider
    private var compositeDisposable = CompositeDisposable()
    private var dailyScaleUp = false
    private lateinit var hourlyList: List<HourlyData>

    override fun hourlyData(data: List<HourlyData>) {
        hourlyList = data
    }

    override fun layoutResID(): Int {
        return R.layout.category_tabs_activity
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "Categories"
    }

    override fun showCurrentWeather(current: Currently) {
        weather_current_feels_like.text = "Feels like ${current.feelsLike()}"
        weather_current_icon.setImageResource(current.getIconRes())
        weather_temperature_layout_current.findViewById<TextView>(R.id.weather_temperature_value).text = current.currentTemperature().toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category_view_pager.adapter = CategoryPagerAdapter(supportFragmentManager)
        category_tab.setupWithViewPager(category_view_pager)

        weather_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weatherPresenter = WeatherPresenter(this, WeatherModel())
        locationProvider = ReactiveLocationProvider(this)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animationDrawable = tab_screen_root.backgroundDrawable as AnimationDrawable
            animationDrawable.setEnterFadeDuration(4000)
            animationDrawable.setExitFadeDuration(4000)
            animationDrawable.start()
        }

        locationRequest = LocationRequest
                .create()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        requestLocation()

        weather_city_name.setOnClickListener { animateRecycler(!dailyScaleUp) }
        weather_current_icon.setOnClickListener{ hourlyWeather() }

    }

    private fun hourlyWeather() {
        val material = MaterialDialog(this)
                .customView(R.layout.weather_hourly, scrollable = true)

        val customView = material.getCustomView()
        customView?.setBackgroundResource(R.drawable.gradient_1)
        val recycler : RecyclerView? = customView?.findViewById(R.id.weather_recycler_hourly)
        recycler?.layoutManager = LinearLayoutManager(this)
        val adapter = WeatherHourlyAdapter(this, hourlyList)
        recycler?.adapter = adapter
        material.show()
    }

    private fun animateRecycler(isScaleUp: Boolean) {
        dailyScaleUp = isScaleUp

        if (isScaleUp)
            weather_recycler.visibility = View.VISIBLE

        val top: Int = weather_recycler.top
        val bottom: Int

        bottom = if (isScaleUp) weather_recycler.bottom else top

        val objectAnimator = ObjectAnimator.ofInt(weather_recycler, "translationX", top, bottom)
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.start()

        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!isScaleUp)
                    weather_recycler.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        val searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        searchEditText.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        searchView.queryHint = "Search Anything"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@CategoryTabActivity, NewsEverything::class.java)
                intent.putExtra("title", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> startActivity(Intent(this, NewsSettings::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_PERMISSION)
            else
                getLocationRX()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION && grantResults.isNotEmpty())
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocationRX()
    }

    override fun showDailyData(list: List<DailyData>) {
        dailyAdapter = WeatherDailyAdapter(this, list)
        weather_recycler.adapter = dailyAdapter
    }

    override fun error(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }


    @SuppressLint("MissingPermission")
    fun getLocationRX() {

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            compositeDisposable.add(locationProvider.getUpdatedLocation(locationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ t -> weatherPresenter.getCurrentWeather(t.latitude, t.longitude) },
                            { t -> Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show() })
            )
        else
            compositeDisposable.add(locationProvider.lastKnownLocation
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ t -> weatherPresenter.getCurrentWeather(t.latitude, t.longitude) },
                            { t -> Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show() })
            )
    }
}