package com.hoanganh.drugstore.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hoanganh.drugstore.Adapter.FlagAdapter
import com.hoanganh.drugstore.Model.FlagsModel
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.extension.checkRequiredPermissions
import com.jakewharton.processphoenix.ProcessPhoenix
import com.nightonke.jellytogglebutton.JellyToggleButton.OnStateChangeListener
import com.nightonke.jellytogglebutton.State
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_CODE_LOCATION = 11112
        const val SDK_VERSION_ANDROID_P = 28
    }

    private lateinit var mapGG: GoogleMap
    lateinit var sharedPerfFlags: SharedPreferences
    lateinit var editorFlags: SharedPreferences.Editor
    var loadNN = 0
    private var latitude: Double = 21.027786029944146
    private var longitude: Double = 105.83550446759239
    private var latitudeAddress: Double? = null
    private var longitudeAddress: Double? = null
    var languageToLoad123: String? = ""
    var currentActivity = 0
    private val testStore = LatLng(21.049666, 105.789118)
    private val testStore2 = LatLng(21.045172, 105.798254)
    private val testStore3 = LatLng(21.009181, 105.805162)
    private val testStore4 = LatLng(21.033267, 105.858277)
    private val testCnilic = LatLng(21.043219, 105.801465)
    private val testCnilic2 = LatLng(21.047120, 105.800037)
    private val testCnilic3 = LatLng(21.010538, 105.804881)
    private var historySearch: ArrayList<String> = arrayListOf()
    private var myLocation: LatLng? = null
    private var searchLocation: LatLng? = null
    private var circleOptions = CircleOptions()
    private val markersStores: MutableList<Marker> = mutableListOf()
    private val listAddressStores: MutableList<LatLng> = mutableListOf()
    private val addressStoreNearbyPlaces: MutableList<LatLng> = mutableListOf()

    private val markersClinics: MutableList<Marker> = mutableListOf()
    private val listAddressClinics: MutableList<LatLng> = mutableListOf()
    private val addressClinicsNearbyPlaces: MutableList<LatLng> = mutableListOf()

    var isCheckDrugStore = false
    var isCheckClinic = false
    private lateinit var markerStore: Marker
    private lateinit var markerClinic: Marker
    var abc = 0
    val myList = mutableListOf<Circle>()

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

        checktbSelectOption()
        searchDrugInfo.setOnClickListener {
            val intent = Intent(this, ScanBarCodeActivity::class.java)
            intent.putExtra("EXTRA", "openFragmentSearchDrug")
            startActivity(intent)
            finish()
        }


        btnSearchDrugStore.setOnCheckedChangeListener { _, _ -> searchDrugStore() }
        btnSearchClinic.setOnCheckedChangeListener { _: CompoundButton, _: Boolean -> searchClinics() }

        edtSearchAddress.setOnTouchListener { _, _ ->
            setAutoCompleteSource()
            edtSearchAddress.showDropDown()
            false
        }
        btnClearTextSeach.setOnClickListener { edtSearchAddress.text.clear() }
        Paper.init(this);
        historySearch = Paper.book().read("contacts", ArrayList())
    }

    override fun onStop() {
        super.onStop()
        Paper.book().write("contacts", historySearch);
    }

    private fun checktbSelectOption() {
        btnSearchDrugStore.isEnabled
        tbSelectOption.onStateChangeListener = OnStateChangeListener { _, state, _ ->
            if (state == State.LEFT) {
                abc = 0
            }
            if (state == State.RIGHT) {
                abc = 1
            }
            btnSearchDrugStore.isChecked = false
            btnSearchClinic.isChecked = false
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun sharePerferencesFlags() {
        sharedPerfFlags = getSharedPreferences("Language", Context.MODE_PRIVATE)
        editorFlags = sharedPerfFlags.edit()
        currentActivity = sharedPerfFlags.getInt("ItemSpiner", 0)
        loadNN = sharedPerfFlags.getInt("ItemSpiner", 0)
    }

    private fun setAutoCompleteSource() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, historySearch
        )
        edtSearchAddress.setAdapter(adapter)
    }

    @SuppressLint("ResourceType")
    override fun onMapReady(googleMap: GoogleMap) {
        mapGG = googleMap

        val hanoiCappital = LatLng(latitude, longitude)
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoiCappital, 10F))


        Handler(Looper.getMainLooper()).postDelayed({

            btnSearchDrugStore.isEnabled = true
            btnSearchClinic.isEnabled = true
        }, 3000)
        if (!this.checkRequiredPermissions()) {
            return
        }
        mapGG.isMyLocationEnabled = true
        mapGG.uiSettings.isMyLocationButtonEnabled = false

        fab.setOnClickListener() { clickFab() }


        mapGG.setOnMarkerClickListener { marker ->
            val intent = Intent(this, ScanBarCodeActivity::class.java)
            when (marker.tag) {
                "store" -> intent.putExtra("EXTRA", "openFragmentDrugStore")
                "clinic" -> intent.putExtra("EXTRA", "openFragmentClinic")
            }
            startActivity(intent)
            false
        }
        searchByEditText()
    }

    private fun clickFab() {
        mapGG.clear()
        if (mapGG.myLocation != null) {
            setMyLocation()
            addCricle()
            mapGG.clear()
        } else {
            Toast.makeText(this, "Taking positioning, ,Wait 5 seconds", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (mapGG.myLocation != null) {
                    setMyLocation()
                    addCricle()
                } else {
                    Toast.makeText(this@MapsActivity, "Can't update Your Location", Toast.LENGTH_SHORT).show()
                }
                btnSearchDrugStore.isEnabled = true
                btnSearchClinic.isEnabled = true
            }, 5000)
        }
        btnSearchDrugStore.isChecked = false
        btnSearchClinic.isChecked = false
    }

    private fun setMyLocation() {
        latitude = mapGG.myLocation.latitude
        longitude = mapGG.myLocation.longitude
        myLocation = LatLng(latitude, longitude)
    }

    private fun searchDrugStore() {

        listAddressStores.add(testStore)
        listAddressStores.add(testStore2)
        listAddressStores.add(testStore3)
        listAddressStores.add(testStore4)


        if (abc == 0) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }

        if (abc == 1) {
            searchDrugstore500m()
        }

//  if (myLocation == null && getCity == null) {
//            isCheckDrugStore = true
//            //Toast.makeText(this, "Store null", Toast.LENGTH_SHORT).show()
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return
//            }
//            mapGG.isMyLocationEnabled = true
//            Handler(Looper.getMainLooper()).postDelayed({
//                val a = mapGG.myLocation.latitude
//                val b = mapGG.myLocation.longitude
//                getCity = LatLng(a, b)
//                mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(getCity, 10F))
//
//                val gcd = Geocoder(this, Locale.getDefault())
//                var listCityName: List<Address> = gcd.getFromLocation(a, b, 1)
//
//                cityName = listCityName[0].adminArea
//                Log.d("123", cityName)
//
//                Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show()
//
//            }, 5000)
//
//        } else if (myLocation == null && getCity != null) {
//
//            isCheckDrugStore = !isCheckDrugStore
//
//            val a = mapGG.myLocation.latitude
//            val b = mapGG.myLocation.longitude
//            getCity = LatLng(a, b)
//            mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(getCity, 10F))
//
//        }
    }

    private fun searchClinics() {
        listAddressClinics.add(testCnilic)
        listAddressClinics.add(testCnilic2)
        listAddressClinics.add(testCnilic3)

        if (abc == 0) {
            Toast.makeText(this, "test Clinic", Toast.LENGTH_SHORT).show()
        }
        if (abc == 1) {
            searchDClinic500m()
        }
    }

    private fun searchDrugstore500m() {
        addressStoreNearbyPlaces.clear()
        if (searchLocation == null) {
            setMyLocation()
        } else {
            myLocation = searchLocation
        }


        if (myLocation != null) {
            addCricle()
            val distance = FloatArray(2)
            for (i in listAddressStores) {
                Location.distanceBetween(
                    i.latitude,
                    i.longitude, circleOptions.center.latitude,
                    circleOptions.center.longitude, distance
                )

                if (distance[0] < circleOptions.radius) {
                    addressStoreNearbyPlaces.add(i)
                }
            }
            if (!isCheckDrugStore && addressStoreNearbyPlaces.size > 0) {
                isCheckDrugStore = true

                for (j in addressStoreNearbyPlaces) {

                    markerStore = mapGG.addMarker(
                        MarkerOptions()
                            .position(LatLng(j.latitude, j.longitude))
                            .title("tìm thấy$j")

                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_store_on)))
                    )
                    markerStore.tag = "store"
                    markersStores.add(markerStore)
                }
            } else if (!isCheckDrugStore && addressStoreNearbyPlaces.size == 0) {
                isCheckDrugStore = true
                Toast.makeText(this, "No Drug stores near you", Toast.LENGTH_SHORT).show()
            } else {
                isCheckDrugStore = false
                for (marker in markersStores)
                    marker.remove()
            }
        } else Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
    }

    private fun searchDClinic500m() {
        addressClinicsNearbyPlaces.clear()
        if (searchLocation == null) {
            setMyLocation()
        } else {
            myLocation = searchLocation
        }

        if (myLocation != null) {

            addCricle()
            val distance = FloatArray(2)
            for (i in listAddressClinics) {
                Location.distanceBetween(
                    i.latitude,
                    i.longitude, circleOptions.center.latitude,
                    circleOptions.center.longitude, distance
                )

                if (distance[0] < circleOptions.radius) {
                    addressClinicsNearbyPlaces.add(i)
                }
            }
            if (!isCheckClinic && addressClinicsNearbyPlaces.size > 0) {
                isCheckClinic = true

                for (j in addressClinicsNearbyPlaces) {

                    markerClinic = mapGG.addMarker(
                        MarkerOptions()
                            .position(LatLng(j.latitude, j.longitude))
                            .title("tìm thấy$j")
                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_clinic_on)))
                    )

                    markerClinic.tag = "clinic"
                    markersClinics.add(markerClinic)
                }
            } else if (!isCheckClinic && addressClinicsNearbyPlaces.size == 0) {
                isCheckClinic = true
                Toast.makeText(this, "No Drug stores near you", Toast.LENGTH_SHORT).show()
            } else {
                isCheckClinic = false
                for (marker in markersClinics)
                    marker.remove()
            }
        } else {
            Toast.makeText(this, "Clinic null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCricle() {

        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16F))
        mapGG.addCircle(
            circleOptions
                .center(LatLng(latitude, longitude))
                .radius(500.0)
                .strokeWidth(0F)
                .fillColor(Color.argb(0, 0, 0, 0))
                .clickable(true)
        )
    }

    private fun setUpGooogleMap() {

        val locationButton =
            (mapGoogle?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
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
                        }
                    }
                    3 -> {
                        if (position != currentActivity) {
                            tesst = "ca_ES"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    4 -> {
                        if (position != currentActivity) {
                            tesst = "ko_KR"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    5 -> {
                        if (position != currentActivity) {
                            tesst = "it_IT"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    6 -> {
                        if (position != currentActivity) {
                            tesst = "de_DE"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    7 -> {
                        if (position != currentActivity) {
                            tesst = "fr_FR"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    8 -> {
                        if (position != currentActivity) {
                            tesst = "zh_CN"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
                        }
                    }
                    9 -> {
                        if (position != currentActivity) {
                            tesst = "en_US"
                            editorFlags.apply { putString("NN", tesst) }.apply()
                            editorFlags.putInt("ItemSpiner", position).commit()
                            resetApp()
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
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
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
                || event.action == KeyEvent.KEYCODE_ENTER
            ) {
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
        if (searchString != "") {
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
            searchLocation = LatLng(latitude, longitude)
            mapGG.addMarker(MarkerOptions().position(searchLocation!!).title(address.toString()))
            mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 16F))
        }
    }

    private fun getMarkerBitmapFromStore(@DrawableRes resId: Int): Bitmap? {
        val customMarkerView: View = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.item_view_stores_marker,
            null
        )
        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image_stores) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }
}


