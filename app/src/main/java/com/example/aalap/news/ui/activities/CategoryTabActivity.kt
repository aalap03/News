package com.example.aalap.news.ui.activities

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.aalap.news.R
import com.example.aalap.news.ui.adapter.CategoryPagerAdapter
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.backgroundDrawable
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.aalap.news.Utils
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.weathermodels.*
import com.example.aalap.news.presenter.WeatherPresenter
import com.example.aalap.news.ui.adapter.WeatherDailyAdapter
import com.example.aalap.news.ui.adapter.WeatherHourlyAdapter
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.category_tabs_activity.*
import kotlinx.android.synthetic.main.main_weather_layout.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.lang.Exception
import java.util.*

const val LOCATION_PERMISSION = 1

class CategoryTabActivity : BaseActivity(), MainView {

    private lateinit var locationRequest: LocationRequest
    lateinit var manager: LocationManager
    private lateinit var dailyAdapter: WeatherDailyAdapter
    private lateinit var weatherPresenter: WeatherPresenter
    private lateinit var locationProvider: ReactiveLocationProvider
    private var compositeDisposable = CompositeDisposable()
    private var dailyScaleUp = false
    private var hourlyDialog: MaterialDialog? = null

    override fun layoutResID(): Int {
        return R.layout.category_tabs_activity
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "Categories"
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
        weather_current_icon.setOnClickListener { hourlyDialog?.show() }
    }

    private fun animateRecycler(isScaleUp: Boolean) {
        dailyScaleUp = isScaleUp

        if (isScaleUp)
            weather_recycler.visibility = View.VISIBLE
        //scale animator
        val scaleBegin = if (isScaleUp) 0f else 1f
        val scaleEnd = if (isScaleUp) 1f else 0f
        weather_recycler.scaleX = scaleBegin
        weather_recycler.scaleY = scaleBegin
        val animator = weather_recycler.animate()
                .scaleX(scaleEnd)
                .scaleY(scaleEnd)
                .setDuration(1000)

        animator.start()
        animator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                if (!isScaleUp)
                    weather_recycler.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {}

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
                val intent = Intent(this@CategoryTabActivity, NewsEverythingAndSaved::class.java)
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
            R.id.menu_settings -> startActivity(Intent(this, SettingsScreen::class.java))
            R.id.menu_bookmarked -> {
                val size = Realm.getDefaultInstance().where(Article::class.java).equalTo("isSaved", true).findAll().size
                Toast.makeText(this, "Saved: $size", Toast.LENGTH_SHORT)
                        .show()

                val intent = Intent(this@CategoryTabActivity, NewsEverythingAndSaved::class.java)
                intent.putExtra("saved", true)
                startActivity(intent)
            }
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

    override fun error(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun getWeatherData(weather: Weather) {

        //currently
        val current: Currently? = weather.currently
        if (current != null) {
            weather_current_feels_like.text = "Feels like ${current.feelsLike()}"
            weather_current_icon.setImageResource(current.getIconRes())
            weather_temperature_layout_current.findViewById<TextView>(R.id.weather_temperature_value).text = current.currentTemperature().toString()
            weather_city_name.text = getCityName(pref.getLatitude(), pref.getLongitude())
        }
        //daily
        dailyAdapter = weather.daily?.data?.let { WeatherDailyAdapter(this, it) }!!
        weather_recycler.adapter = dailyAdapter

        //hourly
        hourlyDialog = MaterialDialog(this).customView(R.layout.weather_hourly, scrollable = true)

        val customView = hourlyDialog?.getCustomView()
        customView?.setBackgroundResource(R.drawable.gradient_2)
        val recycler: RecyclerView? = customView?.findViewById(R.id.weather_recycler_hourly)
        recycler?.layoutManager = LinearLayoutManager(this)
        val adapter = weather.hourly?.data?.let { WeatherHourlyAdapter(this, it) }
        recycler?.adapter = adapter
        recycler?.addItemDecoration(Utils().Decorator(this))

    }

    @SuppressLint("MissingPermission")
    fun getLocationRX() {

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            compositeDisposable.add(locationProvider.getUpdatedLocation(locationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ t -> weatherPresenter.getCurrentWeather(t.latitude, t.longitude) },
                            { t -> Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show() }
                    )
            )
        else
            weatherPresenter.getCurrentWeather(pref.getLatitude(), pref.getLongitude())


    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        return try {
            val geoCoder = Geocoder(this.applicationContext, Locale.getDefault())
            geoCoder.getFromLocation(latitude, longitude, 1)[0].locality
        } catch(e: Exception){
            e.printStackTrace()
            "City Not found"
        }
    }
}