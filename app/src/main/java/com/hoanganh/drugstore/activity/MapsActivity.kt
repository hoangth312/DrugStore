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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hoanganh.drugstore.Adapter.FlagAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CompanionObject.Companion.BUNDLE
import com.hoanganh.drugstore.extension.CompanionObject.Companion.CHINA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.ENGLISH
import com.hoanganh.drugstore.extension.CompanionObject.Companion.EXTRA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.FRANCE
import com.hoanganh.drugstore.extension.CompanionObject.Companion.GERMANY
import com.hoanganh.drugstore.extension.CompanionObject.Companion.HISTORY_SEARCH
import com.hoanganh.drugstore.extension.CompanionObject.Companion.ITALIA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.ITEM_SPINER
import com.hoanganh.drugstore.extension.CompanionObject.Companion.JAPAN
import com.hoanganh.drugstore.extension.CompanionObject.Companion.KOREA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.LANGUAGE
import com.hoanganh.drugstore.extension.CompanionObject.Companion.LANGUAGE2
import com.hoanganh.drugstore.extension.CompanionObject.Companion.LATLNG
import com.hoanganh.drugstore.extension.CompanionObject.Companion.SPAIN
import com.hoanganh.drugstore.extension.CompanionObject.Companion.STORE
import com.hoanganh.drugstore.extension.CompanionObject.Companion.USA
import com.hoanganh.drugstore.extension.CompanionObject.Companion.VIETNAM
import com.hoanganh.drugstore.extension.checkRequiredPermissions
import com.hoanganh.drugstore.model.FlagsModel
import com.hoanganh.drugstore.model.clinic.ClinicModel
import com.hoanganh.drugstore.model.drugstore.DrugStoreModel
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.jakewharton.processphoenix.ProcessPhoenix
import com.nightonke.jellytogglebutton.JellyToggleButton.OnStateChangeListener
import com.nightonke.jellytogglebutton.State
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var listCityName: MutableList<Address> = mutableListOf()
    var dialog: AlertDialog? = null
    private lateinit var mapGG: GoogleMap
    lateinit var sharedPerfFlags: SharedPreferences
    lateinit var editorFlags: SharedPreferences.Editor
    var loadNN = 0
    private var latitude: Double = 21.021542
    private var longitude: Double = 105.769647
    var languageToLoad123: String? = ""
    var currentActivity = 0
    private var historySearch: ArrayList<String> = arrayListOf()
    private var myLocation: LatLng? = null
    private var searchLocation: LatLng? = null
    private var circleOptions = CircleOptions()
    private val markersStores: MutableList<Marker> = mutableListOf()
    private val listAddressStores: MutableList<DrugStoreModel> = mutableListOf()
    private val addressStoreNearbyPlaces: MutableList<DrugStoreModel> = mutableListOf()
    private val markersClinics: MutableList<Marker> = mutableListOf()
    private val listAddressClinics: MutableList<ClinicModel> = mutableListOf()
    private val addressClinicsNearbyPlaces: MutableList<ClinicModel> = mutableListOf()

    var isCheckDrugStore = false
    var isCheckClinic = false
    private lateinit var markerStore: Marker
    private lateinit var markerClinic: Marker
    var checkAllCityAndRround = 0


    private var cityName = ""
    var token: String = ""
    var type: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePerferencesFlags()
        changeLanguage()

        setContentView(R.layout.activity_maps)
        token = SharedPrefManager.getInstance(this).getToken().toString()
        type = SharedPrefManager.getInstance(this).getType().toString()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setUpSpinnerFlags()
        setUpGooogleMap()
        btnBack.setOnClickListener { finish() }
        checktbSelectOption()

        searchDrugInfo.setOnClickListener {
            val intent = Intent(this, ScanBarCodeActivity::class.java)
            intent.putExtra(EXTRA, "openFragmentSearchDrug")
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
        historySearch = Paper.book().read(HISTORY_SEARCH, ArrayList())
        diaLogLoading()

    }

    override fun onStop() {
        super.onStop()
        Paper.book().write(HISTORY_SEARCH, historySearch);
    }

    private fun checktbSelectOption() {
        btnSearchDrugStore.isEnabled
        tbSelectOption.onStateChangeListener = OnStateChangeListener { _, state, _ ->
            if (state == State.LEFT) {

                btnSearchDrugStore.isEnabled = true
                btnSearchClinic.isEnabled = true
                checkAllCityAndRround = 0
                dialog!!.dismiss()
            }
            if (state == State.RIGHT) {
                btnSearchDrugStore.isEnabled = true
                btnSearchClinic.isEnabled = true
                checkAllCityAndRround = 1
                dialog!!.dismiss()
            }
            if (state == State.LEFT_TO_RIGHT) {
                btnSearchDrugStore.isEnabled = false
                btnSearchClinic.isEnabled = false
            }
            if (state == State.RIGHT_TO_LEFT) {
                btnSearchDrugStore.isEnabled = false
                btnSearchClinic.isEnabled = false
            }
            btnSearchDrugStore.isChecked = false
            btnSearchClinic.isChecked = false

        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun sharePerferencesFlags() {
        sharedPerfFlags = getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
        editorFlags = sharedPerfFlags.edit()
        currentActivity = sharedPerfFlags.getInt(ITEM_SPINER, 0)
        loadNN = sharedPerfFlags.getInt(ITEM_SPINER, 0)
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
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(hanoiCappital, 5F))


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
            val args = Bundle()

            when (marker.tag) {
                STORE -> {
                    intent.putExtra(EXTRA, "openFragmentDrugStore")
                    args.putParcelable(LATLNG, marker.position)
                    intent.putExtra(BUNDLE, args)


                }
                "CLINICS" -> {
                    intent.putExtra(EXTRA, "openFragmentClinic")
                    args.putParcelable(LATLNG, marker.position)
                    intent.putExtra(BUNDLE, args)

                }
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
            Toasty.info(this@MapsActivity, "Taking positioning, ,Wait 3 seconds", Toast.LENGTH_SHORT, true).show()

            //  Toast.makeText(this, "Taking positioning, ,Wait 3 seconds", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (mapGG.myLocation != null) {
                    setMyLocation()
                    addCricle()
                } else {
                    Toasty.error(this, "Can't update Your Location", Toast.LENGTH_SHORT, true).show()
//                    Toast.makeText(this@MapsActivity, "Can't update Your Location", Toast.LENGTH_SHORT).show()
                }
                btnSearchDrugStore.isEnabled = true
                btnSearchClinic.isEnabled = true
            }, 3000)
        }
        btnSearchDrugStore.isChecked = false
        btnSearchClinic.isChecked = false
    }

    private fun setMyLocation() {
        if (mapGG.myLocation == null) {
            Toasty.error(this, "Can't update Your Location", Toast.LENGTH_SHORT, true).show()

        } else {
            latitude = mapGG.myLocation.latitude
            longitude = mapGG.myLocation.longitude
            myLocation = LatLng(latitude, longitude)

        }
    }

    private fun getCityName() {


        val gcd = Geocoder(this, Locale.getDefault())
        listCityName = gcd.getFromLocation(latitude, longitude, 10)
        if (listCityName.size > 0) {
            cityName = listCityName[0].adminArea
        }else{

        }

    }
    private fun searchDrugStore() {
        btnSearchDrugStore.isEnabled = false
        btnSearchClinic.isEnabled = false
        // diaLogLoading()
        dialog!!.show()
        listAddressStores.clear()
        addressStoreNearbyPlaces.clear()

        if (searchLocation == null) {
            setMyLocation()
        } else {
            myLocation = searchLocation
        }
        getDataDrugStore()

        Handler(Looper.getMainLooper()).postDelayed({
            if (checkAllCityAndRround == 0) {
                searchDrugsStoreAllCity()
            }

            if (checkAllCityAndRround == 1) {
                searchDrugstore500m()
            }
        }, 2000)

        btnSearchDrugStore.isEnabled = true
        btnSearchClinic.isEnabled = true


    }


    private fun getDataDrugStore() {
        getCityName()
        RetrofitClient.getApiService().getAllDrugStoreInCity("$type  $token", cityName).enqueue(object : Callback<List<DrugStoreModel>> {
            override fun onResponse(call: Call<List<DrugStoreModel>>, response: Response<List<DrugStoreModel>>) {

                if (response.isSuccessful) {

                    val dataList = response.body() as List<DrugStoreModel>
//
                    for (i in dataList) {
                        listAddressStores.add(i)
                    }


                } else {
                    runOnUiThread {
                        Toasty.error(this@MapsActivity, "No matching results were found", Toast.LENGTH_SHORT, true).show()

                    }
                }
            }

            override fun onFailure(call: Call<List<DrugStoreModel>>, t: Throwable) {
                runOnUiThread {
                    Toasty.error(applicationContext, t.message.toString(), Toast.LENGTH_SHORT, true).show()


                }
            }
        })
    }

    private fun searchDrugsStoreAllCity() {

        if (!isCheckDrugStore && listAddressStores.size > 0) {
            isCheckDrugStore = true
            for (i in listAddressStores) {
                markerStore = mapGG.addMarker(
                        MarkerOptions()
                                .position(LatLng(i.latitude, i.longitude))
                                .title(i.name)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_store_on)))
                )
                markerStore.tag = STORE

                markersStores.add(markerStore)
            }


        } else if (!isCheckDrugStore && listAddressStores.size == 0) {
            isCheckDrugStore = true

            Toasty.error(this@MapsActivity, "There are no stores in your city", Toast.LENGTH_SHORT, true).show()

        } else {
            isCheckDrugStore = false
            for (marker in markersStores)
                marker.remove()
        }
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
        dialog!!.dismiss()
    }

    private fun searchDrugstore500m() {
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
                                    .title(j.name)

                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_store_on)))

                    )
                    markerStore.tag = STORE
                    markersStores.add(markerStore)
                }
            } else if (!isCheckDrugStore && addressStoreNearbyPlaces.size == 0) {
                isCheckDrugStore = true

                Toasty.error(this@MapsActivity, "There are no stores in your city", Toast.LENGTH_SHORT, true).show()

            } else {
                isCheckDrugStore = false
                for (marker in markersStores)
                    marker.remove()
            }

        } else  Toasty.error(this@MapsActivity, "Something is Wrong", Toast.LENGTH_SHORT, true).show()


        dialog!!.dismiss()

    }


    private fun getDataClinic() {
        getCityName()
        RetrofitClient.getApiService().getAllClinicInCity("$type  $token", cityName).enqueue(object : Callback<List<ClinicModel>> {
            override fun onResponse(call: Call<List<ClinicModel>>, response: Response<List<ClinicModel>>) {

                if (response.isSuccessful) {

                    val dataListClinic = response.body() as List<ClinicModel>
//
                    for (i in dataListClinic) {
                        listAddressClinics.add(i)
                    }


                } else {
                    runOnUiThread {
                        Toasty.error(this@MapsActivity, "There are no clinic in your city", Toast.LENGTH_SHORT, true).show()


                    }
                }
            }

            override fun onFailure(call: Call<List<ClinicModel>>, t: Throwable) {
                runOnUiThread {
                    Toasty.error(this@MapsActivity, t.message.toString(), Toast.LENGTH_SHORT, true).show()

                    // Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        })
    }


    private fun searchClinics() {

        btnSearchDrugStore.isEnabled = false
        btnSearchClinic.isEnabled = false
        //diaLogLoading()
        dialog!!.show()
        listAddressClinics.clear()
        addressClinicsNearbyPlaces.clear()

        if (searchLocation == null) {
            setMyLocation()
        } else {
            myLocation = searchLocation
        }
        getDataClinic()

        Handler(Looper.getMainLooper()).postDelayed({
            if (checkAllCityAndRround == 0) {
                searchClinicStoreAllCity()
            }
            if (checkAllCityAndRround == 1) {
                searchClinic500m()
            }
        }, 2000)
        btnSearchDrugStore.isEnabled = true
        btnSearchClinic.isEnabled = true


    }

    private fun searchClinic500m() {


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
                                    .title(j.name)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_clinic_on)))
                    )

                    markerClinic.tag = "CLINICS"
                    markersClinics.add(markerClinic)
                }

            } else if (!isCheckClinic && addressClinicsNearbyPlaces.size == 0) {
                isCheckClinic = true
                Toasty.error(this@MapsActivity, "There are no clinic in your city", Toast.LENGTH_SHORT, true).show()

            } else {
                isCheckClinic = false
                for (marker in markersClinics)
                    marker.remove()
            }
        } else {
            //  Toast.makeText(this, "Clinic null", Toast.LENGTH_SHORT).show()
            Toasty.error(this@MapsActivity, "Something is Wrong", Toast.LENGTH_SHORT, true).show()

        }
        dialog!!.dismiss()
    }


    private fun searchClinicStoreAllCity() {

        if (!isCheckClinic && listAddressClinics.size > 0) {
            isCheckClinic = true
            for (i in listAddressClinics) {
                markerClinic = mapGG.addMarker(
                        MarkerOptions()
                                .position(LatLng(i.latitude, i.longitude))
                                .title(i.name)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromStore(R.drawable.ic__01_clinic_on)))
                )
                markerClinic.tag = "CLINICS"
                markersClinics.add(markerClinic)
            }


        } else if (!isCheckClinic && listAddressClinics.size == 0) {
            isCheckClinic = true
            Toasty.error(this@MapsActivity, "There are no clinic in your city", Toast.LENGTH_SHORT, true).show()

        } else {
            isCheckClinic = false
            for (marker in markersClinics)
                marker.remove()
        }
        mapGG.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
        dialog!!.dismiss()
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
                var ketOfCountry = ""
                when (position) {

                    0 -> {
                        if (position != currentActivity) {
                            ketOfCountry = JAPAN
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    1 -> {
                        if (position != currentActivity) {
                            ketOfCountry = VIETNAM
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    2 -> {
                        if (position != currentActivity) {
                            ketOfCountry = ENGLISH
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    3 -> {
                        if (position != currentActivity) {
                            ketOfCountry = SPAIN
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    4 -> {
                        if (position != currentActivity) {
                            ketOfCountry = KOREA
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    5 -> {
                        if (position != currentActivity) {
                            ketOfCountry = ITALIA
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    6 -> {
                        if (position != currentActivity) {
                            ketOfCountry = GERMANY
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    7 -> {
                        if (position != currentActivity) {
                            ketOfCountry = FRANCE
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    8 -> {
                        if (position != currentActivity) {
                            ketOfCountry = CHINA
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
                            resetApp()
                        }
                    }
                    9 -> {
                        if (position != currentActivity) {
                            ketOfCountry = USA
                            editorFlags.apply { putString(LANGUAGE2, ketOfCountry) }.apply()
                            editorFlags.putInt(ITEM_SPINER, position).commit()
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
        languageToLoad123 = sharedPerfFlags.getString(LANGUAGE2, JAPAN)
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

        edtSearchAddress.setOnEditorActionListener { _, actionId, event ->
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

    private fun diaLogLoading() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }
}




