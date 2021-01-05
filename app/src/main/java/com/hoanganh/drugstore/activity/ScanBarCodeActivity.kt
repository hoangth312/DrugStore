package com.hoanganh.drugstore.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.hoanganh.drugstore.Fragment.InformationDrugStoreFragment
import com.hoanganh.drugstore.Fragment.InformationOfClinicFragment
import com.hoanganh.drugstore.Fragment.SearchDrugsFragment
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.app_bar_scan_barcode.*


class ScanBarCodeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bar_code)
        setSupportActionBar(toolbar)





        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        when (intent.getStringExtra("EXTRA")) {
            "openFragmentSearchDrug" -> {
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SearchDrugsFragment()).commit()

            }
            "openFragmentDrugStore" -> {
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InformationDrugStoreFragment()).commit()

            }
            "openFragmentClinic" -> {
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, InformationOfClinicFragment()).commit()

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.scan_bar_code, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}