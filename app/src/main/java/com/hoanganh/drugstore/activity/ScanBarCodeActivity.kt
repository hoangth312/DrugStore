package com.hoanganh.drugstore.activity

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.android.material.navigation.NavigationView
import com.hoanganh.drugstore.Fragment.InformationDrugStoreFragment
import com.hoanganh.drugstore.Fragment.InformationOfClinicFragment
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.preference.SharedPrefManager
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


        var firstName = SharedPrefManager.getInstance(this).getFirstName()
        var lastName = SharedPrefManager.getInstance(this).getLastName()
        headerView.tvUserName.text =  "$firstName $lastName"
        headerView.tvEmailUser.text = SharedPrefManager.getInstance(this).getEmail().toString()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.scan_bar_code, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getFmFromMap() {
        when (intent.getStringExtra("EXTRA")) {
            "openFragmentSearchDrug" -> {
                //   supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SearchDrugsFragment()).commit()
                navController.navigate(R.id.action_nav_home_to_fmSearchDugs)
            }
            "openFragmentDrugStore" -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, InformationDrugStoreFragment()).commit()

                //navController.navigate(R.id.action_nav_home_to_fmInfoDrugStore)
            }
            "openFragmentClinic" -> {
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InformationOfClinicFragment())
                        .commit()
                //navController.navigate(R.id.action_nav_home_to_fmInfoClinic)
            }

        }
    }


    private fun selectToolBar() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
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


