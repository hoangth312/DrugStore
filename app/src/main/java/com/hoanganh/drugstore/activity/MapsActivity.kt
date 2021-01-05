package com.hoanganh.drugstore.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hoanganh.drugstore.Adapter.FlagAdapter
import com.hoanganh.drugstore.Model.FlagsModel
import com.hoanganh.drugstore.R
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapGG: GoogleMap
    lateinit var sharedPerfFlags: SharedPreferences
    lateinit var sharedPerfHistory: SharedPreferences

    lateinit var editorFlags: SharedPreferences.Editor

    var loadNN = 0
    private var latitude: Double = 21.027786029944146
    private var longitude: Double = 105.83550446759239

    var languageToLoad123: String? = ""
    var textHistory: String? = ""
    var currentActivity = 0

    private val testStore = LatLng(21.049666, 105.789118)
    private val testStore2 = LatLng(21.045172, 105.798254)
    private val testStore3 = LatLng(21.009181, 105.805162)
    private val testCnilic = LatLng(21.043219, 105.801465)
    private val testCnilic2 = LatLng(21.047120, 105.800037)
    private val testCnilic3 = LatLng(21.010538, 105.804881)

    private var historySearch: ArrayList<String> = arrayListOf()

    private var myLocation: LatLng? = null
    private var circleOptions = CircleOptions()
    private val markersStores: MutableList<Marker> = mutableListOf()
    private val listAddressStores: MutableList<LatLng> = mutableListOf()
    private val addressStoreNearbyPlaces: MutableList<LatLng> = mutableListOf()
    private val markersClinics: MutableList<Marker> = mutableListOf()
    private val listAddressClinics: MutableList<LatLng> = mutableListOf()
    private val addressClinicsNearbyPlaces: MutableList<LatLng> = mutableListOf()
    var isCheckDrugStore = true
    var isCheckClinic = true
    private lateinit var markerStore: Marker
    private lateinit var markerClinic: Marker


    val PREFS_NAME = "PingBusPrefs"
    val PREFS_SEARCH_HISTORY = "SearchHistory"
    private var settingss: SharedPreferences? = null
    private var history: MutableSet<String> = mutableSetOf()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePerferencesFlags()
        changeLanguage()

        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setUpSpinnerFlags()
        setUpGooogleMap()

        btnBack.setOnClickListener { finish() }
        btnSearchDrugStore.setOnCheckedChangeListener() { _, _ -> searchDrugStore() }




        btnSearchClinic.setOnCheckedChangeListener() { _: CompoundButton, _: Boolean -> searchClinics() }

        searchDrugInfo.setOnClickListener() {
            val intent = Intent(this, ScanBarCodeActivity::class.java)
            intent.putExtra("EXTRA", "openFragmentSearchDrug")
            startActivity(intent)
            finish()
        }



        edtSearchAddress.setOnTouchListener(View.OnTouchListener { v, event ->
            setAutoCompleteSource()
            edtSearchAddress.showDropDown()

            false
        })
        btnClearTextSeach.setOnClickListener{ edtSearchAddress.text.clear()}


        getArrayList("History")
    }

    override fun onStop() {
        super.onStop()
      saveArrayList(historySearch,"History")
    }



    fun saveArrayList(list: ArrayList<String>, key: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String?): ArrayList<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        historySearch = gson.fromJson(json, type)
        return historySearch
    }

    private fun sharePerferencesFlags() {
        sharedPerfFlags = getSharedPreferences("Language", Context.MODE_PRIVATE)
        editorFlags = sharedPerfFlags.edit()
        currentActivity = sharedPerfFlags.getInt("ItemSpiner", 0)
        loadNN = sharedPerfFlags.getInt("ItemSpiner", 0)
    }



    private fun setAutoCompleteSource() {

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, historySearch)


        edtSearchAddress.setAdapter(adapter)


    }


    @SuppressLint("ResourceType")
    override fun onMapReady(googleMap: GoogleMap) {
        mapGG = googleMap

        val hanoiCappital = LatLng(latitude, longitude)

//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoiCappital, 10F))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mapGG.isMyLocationEnabled = false
        mapGG.uiSettings.isMyLocationButtonEnabled = false;


        fab.setOnClickListener() {
            mapGG.clear()
            btnSearchDrugStore.isChecked = false
            btnSearchClinic.isChecked = false

            mapGG.isMyLocationEnabled = true
            if (mapGG.myLocation != null) {
                latitude = mapGG.myLocation.latitude
                longitude = mapGG.myLocation.longitude
                myLocation = LatLng(latitude, longitude)
                addCricle()
            } else {
                Toast.makeText(this, "Taking positioning, ,Wait 5 seconds", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (mapGG.myLocation != null) {
                        latitude = mapGG.myLocation.latitude
                        longitude = mapGG.myLocation.longitude
                        myLocation = LatLng(latitude, longitude)
                        addCricle()
                    } else {
                        Toast.makeText(this@MapsActivity, "Can't update Your Location", Toast.LENGTH_SHORT).show()

                    }
                }, 5000)

            }
        }


        mapGG.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            val intent = Intent(this, ScanBarCodeActivity::class.java)

            when (marker.tag) {
                "store" -> {
                    intent.putExtra("EXTRA", "openFragmentDrugStore")
                }

                "clinic" -> {
                    intent.putExtra("EXTRA", "openFragmentClinic")
                }
            }
            startActivity(intent)
            false
        })

        searchByEditText()


    }

    private fun searchDrugStore() {
        if (myLocation != null) {

            addCricle()
            val distance = FloatArray(2)
            listAddressStores.add(testStore)
            listAddressStores.add(testStore2)
            listAddressStores.add(testStore3)

            for (i in listAddressStores) {
                Location.distanceBetween(i.latitude,
                        i.longitude, circleOptions.center.latitude,
                        circleOptions.center.longitude, distance)

                if (distance[0] < circleOptions.radius) {
                    addressStoreNearbyPlaces.add(i)
                }
            }


            if (isCheckDrugStore && addressStoreNearbyPlaces.size > 0) {
                isCheckDrugStore = false

                //  Toast.makeText(this, "123", Toast.LENGTH_SHORT).show()
                for (j in addressStoreNearbyPlaces) {

                    markerStore = mapGG.addMarker(MarkerOptions()
                            .position(LatLng(j.latitude, j.longitude))
                            .title("tìm thấy$j")

                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_store_on))))
                    markerStore.tag = "store"
                    markersStores.add(markerStore)
                }
            } else if (isCheckDrugStore && addressStoreNearbyPlaces.size == 0) {
                isCheckDrugStore = false
                Toast.makeText(this, "No Drug stores near you", Toast.LENGTH_SHORT).show()
            } else {
                isCheckDrugStore = true
                for (marker in markersStores)
                    marker.remove()
            }
        } else {
            Toast.makeText(this, "Store null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchClinics() {
        if (myLocation != null) {

            addCricle()
            val distance = FloatArray(2)
            listAddressClinics.add(testCnilic)
            listAddressClinics.add(testCnilic2)
            listAddressClinics.add(testCnilic3)

            for (i in listAddressClinics) {
                Location.distanceBetween(i.latitude,
                        i.longitude, circleOptions.center.latitude,
                        circleOptions.center.longitude, distance)

                if (distance[0] < circleOptions.radius) {
                    addressClinicsNearbyPlaces.add(i)
                }
            }


            if (isCheckClinic && addressClinicsNearbyPlaces.size > 0) {
                isCheckClinic = false

                for (j in addressClinicsNearbyPlaces) {

                    markerClinic = mapGG.addMarker(MarkerOptions()
                            .position(LatLng(j.latitude, j.longitude))
                            .title("tìm thấy$j")
                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_clinic_on))))

                    markerClinic.tag = "clinic"
                    markersClinics.add(markerClinic)

                }
            } else if (isCheckClinic && addressClinicsNearbyPlaces.size == 0) {
                isCheckClinic = false
                Toast.makeText(this, "No Drug stores near you", Toast.LENGTH_SHORT).show()
            } else {
                isCheckClinic = true
                for (marker in markersClinics)
                    marker.remove()
            }
        } else {
            Toast.makeText(this, "Clinic null", Toast.LENGTH_SHORT).show()
        }

    }


    private fun addCricle() {
        //  mapGG.clear()
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16F))
        mapGG.addCircle(circleOptions
                .center(LatLng(latitude, longitude))
                .radius(500.0)
                .strokeWidth(0F)
                .fillColor(Color.argb(0, 0, 0, 0))
                .clickable(true))

    }

    private fun setUpGooogleMap() {

        val locationButton = (mapGoogle?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 500, 200)
        rlp.width = 200
        rlp.height = 200


    }


    private fun setUpSpinnerFlags() {
        val flagAdapter = FlagAdapter(this, FlagsModel.Conuntries.list!!)
        spinnerLanguage.adapter = flagAdapter
        spinnerLanguage.setSelection(loadNN)
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var tesst = ""
                when (position) {

                    0 -> {
                        if (position != currentActivity) {
                            tesst = "ja_JP"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }


                    }
                    1 -> {
                        if (position != currentActivity) {
                            tesst = "vi_VN"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()


                        }
                    }
                    2 -> {
                        if (position != currentActivity) {
                            tesst = "en_001"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }

                    }
                    3 -> {
                        if (position != currentActivity) {
                            tesst = "ca_ES"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    4 -> {
                        if (position != currentActivity) {
                            tesst = "ko_KR"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    5 -> {
                        if (position != currentActivity) {
                            tesst = "it_IT"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    6 -> {
                        if (position != currentActivity) {
                            tesst = "de_DE"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    7 -> {
                        if (position != currentActivity) {
                            tesst = "fr_FR"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    8 -> {
                        if (position != currentActivity) {
                            tesst = "zh_CN"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                    9 -> {
                        if (position != currentActivity) {
                            tesst = "en_US"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
//
                        }
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }


    }

    private fun changeLanguage() {
        languageToLoad123 = sharedPerfFlags.getString("NN", "ja_JP")
        val locale = Locale(languageToLoad123)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,
                baseContext.resources.displayMetrics)
    }

    private fun resetApp() {
        val nextIntent = Intent(this, MapsActivity::class.java)
        ProcessPhoenix.triggerRebirth(this, nextIntent)
    }

    private fun searchByEditText() {

        edtSearchAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    || event.action == KeyEvent.KEYCODE_ENTER) {
                geoLocateByEditText()

            }



            false
        }

    }

    private fun geoLocateByEditText() {

        mapGG.clear()
        btnSearchDrugStore.isChecked = false
        btnSearchClinic.isChecked = false
        val searchString = edtSearchAddress.text.toString()
        if (searchString != ""){
            historySearch.add(0, searchString)
        }

        val geoCoder = Geocoder(this)
        var list: List<Address> = ArrayList()
        try {
            list = geoCoder.getFromLocationName(searchString, 1)
        } catch (e: IOException) {
        }

        if ((list.size) > 0) {

            val address = list[0]

            latitude = address.latitude
            longitude = address.longitude
            myLocation = LatLng(latitude, longitude)
            mapGG.addMarker(MarkerOptions().position(myLocation!!).title(address.toString()))
            mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16F))




        }
    }

    private fun getMarkerBitmapFromStore(@DrawableRes resId: Int): Bitmap? {
        val customMarkerView: View = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_view_stores_marker, null)
        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image_stores) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

}


