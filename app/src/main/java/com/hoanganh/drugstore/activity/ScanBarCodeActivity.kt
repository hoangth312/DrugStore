package com.hoanganh.drugstore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.hoanganh.drugstore.Fragment.ScanBarCodeFragmentDirections
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CompanionObject.Companion.BUNDLE
import com.hoanganh.drugstore.extension.CompanionObject.Companion.EXTRA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.LATLNG
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_scan_bar_code.*
import kotlinx.android.synthetic.main.app_bar_scan_barcode.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class ScanBarCodeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var currentFm = FRAGMENT_HOME
    private lateinit var navView: NavigationView

    companion object {
        private var FRAGMENT_HOME = 1
        private var FRAGMENT_INFO_USER = 2


    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bar_code)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_informationUser), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val headerView: View = navView.getHeaderView(0)
        selectToolBar()
        getFmFromMap()
        logOutAccount()


        val firstName = SharedPrefManager.getInstance(this).getFirstName()
        val lastName = SharedPrefManager.getInstance(this).getLastName()
        val avatarUser = SharedPrefManager.getInstance(this).getAvatarUrl()

        headerView.tvUserName.text = "$firstName $lastName"
        headerView.tvEmailUser.text = SharedPrefManager.getInstance(this).getEmail().toString()
        Picasso.get().load(avatarUser).into(headerView.imvAvatarUser)



    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getFmFromMap() {
        when (intent.getStringExtra(EXTRA)) {
            "openFragmentSearchDrug" -> {
                //   supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SearchDrugsFragment()).commit()
                navController.navigate(R.id.action_nav_home_to_fmSearchDugs)

            }
            "openFragmentDrugStore" -> {
//                supportFragmentManager.beginTransaction()
//                        .replace(R.id.nav_host_fragment, InformationDrugStoreFragment()).commit()
                val bundle = intent.getParcelableExtra<Bundle>(BUNDLE)
                val latLngStore = bundle!!.getParcelable<LatLng>(LATLNG)
                navController.navigate(R.id.action_nav_home_to_fmInfoDrugStore)
                val action = ScanBarCodeFragmentDirections.actionNavHomeToFmInfoDrugStore(latLngStore!!)
                navController.navigateUp()
                navController.navigate(action)


            }
            "openFragmentClinic" -> {
//                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InformationOfClinicFragment())
//                        .commit()

                val bundle = intent.getParcelableExtra<Bundle>(BUNDLE)
                val latLngClinic = bundle!!.getParcelable<LatLng>(LATLNG)
                navController.navigate(R.id.action_nav_home_to_fmInfoClinic)
                val action = ScanBarCodeFragmentDirections.actionNavHomeToFmInfoClinic(latLngClinic!!)
                navController.navigateUp()
                navController.navigate(action)
            }

        }
    }


    private fun selectToolBar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_home || destination.id == R.id.nav_informationUser) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (FRAGMENT_HOME != currentFm) {
                    currentFm = FRAGMENT_HOME
                }
            }
            R.id.nav_informationUser -> {
                if (FRAGMENT_INFO_USER != currentFm) {
                    currentFm = FRAGMENT_INFO_USER

                }
            }
            R.id.btnLogout -> {
//
            }
            else -> NavigationUI.onNavDestinationSelected(item, navController)
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logOutAccount() {
        var logout = navView.menu.findItem(R.id.btnLogout)
        logout.setOnMenuItemClickListener {
            RetrofitClient.getApiService().userLogout()
            SharedPrefManager.getInstance(this).logOutShare()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
            true
        }

    }

}


