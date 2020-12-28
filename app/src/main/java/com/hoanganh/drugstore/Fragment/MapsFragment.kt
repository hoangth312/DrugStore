package com.hoanganh.drugstore.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.hoanganh.drugstore.Adapter.FlagAdapter
import com.hoanganh.drugstore.Model.Flags
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_maps.view.*


class MapsFragment : Fragment() {
private lateinit var viewOfLayout: View
private var countryList: ArrayList<Flags> = arrayListOf()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

          googleMap.isMyLocationEnabled = true
           googleMap.uiSettings.isMyLocationButtonEnabled = true





    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout =  inflater.inflate(R.layout.fragment_maps, container, false)

        setUpSpinnerFlags()

        viewOfLayout.btnBack.setOnClickListener { this.findNavController().popBackStack() }

        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpGooogleMap()

    }


    private fun setUpGooogleMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val locationButton = (mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp =  locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 500, 200)
        rlp.width = 200
        rlp.height = 200
    }

    private fun setUpSpinnerFlags(){

   val   flagAdapter = FlagAdapter(requireContext(), Flags.Conuntries.list!!)
        viewOfLayout.spinnerLanguage.adapter = flagAdapter

        viewOfLayout.spinnerLanguage.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var clickItem = parent?.getItemAtPosition(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }




}