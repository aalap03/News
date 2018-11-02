package com.example.aalap.news.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import org.jetbrains.anko.info

const val LOCATION_PERMISSION = 1

class CategoryTabActivity : BaseActivity() {

    private lateinit var locationRequest: LocationRequest

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

        locationRequest = LocationRequest
                .create()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        requestLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {

            R.id.menu_settings->{
                startActivity(Intent(this, NewsSettings::class.java))
            }

            R.id.menu_search->{

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
            }else
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback(), null)
        }
    }

    override fun onPause() {
        super.onPause()

        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback())
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION && grantResults.isNotEmpty()) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback(), null)
            }
        }
    }

    private fun locationCallback(): LocationCallback {

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
}