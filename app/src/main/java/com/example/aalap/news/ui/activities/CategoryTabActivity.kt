package com.example.aalap.news.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.aalap.news.R
import com.example.aalap.news.ui.adapter.CategoryPagerAdapter
import com.example.aalap.news.weatherService
import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.category_tabs_activity.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.info
import org.jetbrains.anko.textView
import android.widget.TextView
import android.widget.EditText
import androidx.core.content.ContextCompat


const val LOCATION_PERMISSION = 1

interface SendQuery {
    fun sendQuery(query: String)
}

class CategoryTabActivity : BaseActivity() {

    private lateinit var locationRequest: LocationRequest
    lateinit var sendQuery: SendQuery
    lateinit var manager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category_view_pager.adapter = CategoryPagerAdapter(supportFragmentManager)
        category_tab.setupWithViewPager(category_view_pager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animationDrawable = tab_screen_root.backgroundDrawable as AnimationDrawable
            animationDrawable.setEnterFadeDuration(4000)
            animationDrawable.setExitFadeDuration(4000)
            animationDrawable.start()
        }

        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationRequest = LocationRequest
                .create()
                .setNumUpdates(1)
                .setExpirationDuration(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        requestLocation()

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

            R.id.menu_settings -> {
                startActivity(Intent(this, NewsSettings::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun requestLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_PERMISSION)
            } else
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback(), null)
        }
    }

    override fun onPause() {
        super.onPause()

        info { "Weather: removing" }
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback())
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION && grantResults.isNotEmpty()) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback(), Looper.myLooper())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun locationCallback(): LocationCallback {

        val lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val weatherSubscription = weatherService.getCurrentWeather(lastKnownLocation.latitude, lastKnownLocation.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, t2 ->
                    info { "Weather: data: ${t1?.body()}" }
                    info { "Weather: er ${t2?.localizedMessage}" }
                }

        info { "Weather: requestingLocation..." }
        return object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)

                if (result != null) {
                    val location = if (result.locations.isNotEmpty())
                        result.locations[0]
                    else
                        result.lastLocation

                    info { "Weather: ${location.longitude}" }
                    info { "Weather: ${location.latitude}" }

                    val weatherSubscription = weatherService.getCurrentWeather(location.latitude, location.longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { t1, t2 ->
                                info { "Weather: data: ${t1?.body()}" }
                                info { "Weather: er ${t2?.localizedMessage}" }
                            }

                }
            }
        }
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

}