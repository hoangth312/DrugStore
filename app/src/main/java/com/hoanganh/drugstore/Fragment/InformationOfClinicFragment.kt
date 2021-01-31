package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.hoanganh.drugstore.Adapter.BannerAdapter
import com.hoanganh.drugstore.Adapter.CommentAdapter
import com.hoanganh.drugstore.Adapter.FacultyAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CustomEllipsize
import com.hoanganh.drugstore.model.Banner
import com.hoanganh.drugstore.model.Comment
import com.hoanganh.drugstore.model.Comment1
import com.hoanganh.drugstore.model.clinic.ClinicModel
import com.hoanganh.drugstore.model.clinic.ClinicWorkTime
import com.hoanganh.drugstore.model.clinic.Faculty
import com.hoanganh.drugstore.preference.SharedPrefManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_fragments.view.*
import kotlinx.android.synthetic.main.fragment_information_of_clinic.*
import kotlinx.android.synthetic.main.fragment_information_of_clinic.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class InformationOfClinicFragment : Fragment() {
    lateinit var viewOfLayout: View

    private var args by Delegates.notNull<LatLng>()
    private var latitudeClinic: Double = 0.0
    private var longitudeClinic: Double = 0.0
    private var token = ""
    private var type = ""
    private var listFaculties = ArrayList<Faculty>()
    private var listBanner = ArrayList<Banner>()
    private var listCommentClinics = ArrayList<Comment1>()
    private var listWorkingTime = ArrayList<ClinicWorkTime>()
    private val createDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_information_of_clinic, container, false)
        viewOfLayout.toolbar_title.text = getString(R.string.detailOfClinic)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        setLatLng()
        getDataClinic()

        return viewOfLayout

    }

    private fun setLatLng() {
        args = InformationOfClinicFragmentArgs.fromBundle(requireArguments()).latlngClinic
        latitudeClinic = args.latitude
        longitudeClinic = args.longitude

    }

    private fun getDataBanner(idClinic: Int) {
        RetrofitClient.getApiService().getBannerClinic("$type  $token", idClinic).enqueue(object : Callback<List<Banner>> {
            override fun onResponse(call: Call<List<Banner>>, response: Response<List<Banner>>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread() {
                        listBanner = response.body() as ArrayList<Banner>
                        val adapter = BannerAdapter(context, listBanner)
                        viewOfLayout.viewpagerClinic.adapter = adapter
                        viewOfLayout.circleIndicator1.setViewPager(viewpagerClinic)
                    }
                }
            }

            override fun onFailure(call: Call<List<Banner>>, t: Throwable) {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDataClinic() {
        RetrofitClient.getApiService().getClinic("$type  $token", latitudeClinic, longitudeClinic).enqueue(object : Callback<ClinicModel> {
            override fun onResponse(call: Call<ClinicModel>, response: Response<ClinicModel>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        var idClinic = response.body()!!.id
                        getDataBanner(idClinic)
                        viewOfLayout.txtNameClinic.text = response.body()!!.name
                        viewOfLayout.ratingbarClinic.rating = (response.body()!!.vote).toFloat()
                        viewOfLayout.addressClinic.text = response.body()!!.apartmentNumber + " " + response.body()!!.street + "," + response.body()!!.district + "," + response.body()!!.city
                        viewOfLayout.phoneClinic.text = response.body()!!.phoneNumber
                        viewOfLayout.txtLanguage.text = response.body()!!.languages
                        viewOfLayout.txtWebsite.text = response.body()!!.websiteUrl
                        viewOfLayout.timeWorkingMorning.text = response.body()!!.workTimeMorning
                        viewOfLayout.timeWorkingAfternoon.text = response.body()!!.workTimeAfternoon
                        listWorkingTime = response.body()!!.clinicWorkTimes as ArrayList<ClinicWorkTime>
                        listWorkingTime.apply {
                            getDateWorking()
                        }

                        viewOfLayout.txtDetailClinic.apply {
                            txtDetailClinic.text = response.body()!!.information
                            CustomEllipsize(txtDetailClinic, 1, "...")
                        }

                        if (txtDetailClinic.length() < 79) {
                            viewOfLayout.txtSeeMore.visibility = GONE
                        } else {
                            viewOfLayout.txtSeeMore.visibility = VISIBLE
                        }

                        listCommentClinics = response.body()!!.evaluates as ArrayList<Comment1>
                        if (listCommentClinics.size > 0) {
                            getAllComment()
                        }
                        listFaculties = response.body()!!.faculties as ArrayList<Faculty>
                        if (listFaculties.size > 0) {
                            getAllFaculties()
                        } else {
                        }
                        setDataComment(idClinic)
                    }
                }
            }

            override fun onFailure(call: Call<ClinicModel>, t: Throwable) {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDateWorking() {
        for (time in listWorkingTime) {
            when (time.weekDay) {
                "1" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorMon.visibility = VISIBLE
                        false -> viewOfLayout.checkMorMon.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterMon.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterMon.visibility = INVISIBLE

                    }
                }
                "2" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorTue.visibility = VISIBLE
                        false -> viewOfLayout.checkMorTue.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterTue.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterTue.visibility = INVISIBLE

                    }
                }
                "3" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorWed.visibility = VISIBLE
                        false -> viewOfLayout.checkMorWed.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterWed.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterWed.visibility = INVISIBLE

                    }
                }
                "4" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorThu.visibility = VISIBLE
                        false -> viewOfLayout.checkMorThu.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterThu.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterThu.visibility = INVISIBLE

                    }
                }
                "5" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorFri.visibility = VISIBLE
                        false -> viewOfLayout.checkMorFri.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterFri.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterFri.visibility = INVISIBLE

                    }
                }
                "6" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorSat.visibility = VISIBLE
                        false -> viewOfLayout.checkMorSat.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterSat.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterSat.visibility = INVISIBLE

                    }
                }
                "7" -> {
                    when (time.workingMorning) {
                        true -> viewOfLayout.checkMorSun.visibility = VISIBLE
                        false -> viewOfLayout.checkMorSun.visibility = INVISIBLE

                    }
                    when (time.workingAfternoon) {
                        true -> viewOfLayout.checkAfterSun.visibility = VISIBLE
                        false -> viewOfLayout.checkAfterSun.visibility = INVISIBLE

                    }
                }
            }
        }


    }


    private fun getAllFaculties() {
        val listFacultyAdapter = FacultyAdapter(listFaculties)
        viewOfLayout.rcFacultiesClinic.layoutManager = LinearLayoutManager(context)
        viewOfLayout.rcFacultiesClinic.adapter = listFacultyAdapter
    }

    private fun getAllComment() {
        val listCommentAdapter = CommentAdapter(listCommentClinics)
        viewOfLayout.reCommentClinic.layoutManager = LinearLayoutManager(context)
        viewOfLayout.reCommentClinic.adapter = listCommentAdapter
        listCommentAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.finish()
    }

    private fun setDataComment(idClinic: Int) {
        viewOfLayout.txtDateCommentClinic.text = createDate
        viewOfLayout.reviewCommentClinic.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }
        viewOfLayout.btnCommentClinic.setOnClickListener {
            when {
                edtCommentClinic.text.toString() == "" -> {

                    Toasty.error(requireContext(), "Pls Enter Text", Toast.LENGTH_SHORT, true).show()
                }
                reviewCommentClinic.rating.toDouble() == 0.0 -> {
                    Toasty.error(requireContext(), "Pls Enter Rating for you", Toast.LENGTH_SHORT, true).show()
                }
                else -> {
                    val cmt = edtCommentClinic.text.toString()
                    val currentDate = txtDateCommentClinic.text.toString()
                    val rateValue = reviewCommentClinic.rating.toDouble()
                    val user = SharedPrefManager.getInstance(requireContext()).getID()
                    RetrofitClient.getApiService()
                            .addCommentClinic("$type  $token", Comment(cmt, null, idClinic, user, rateValue))
                            .enqueue(object : Callback<Comment1> {
                                override fun onResponse(call: Call<Comment1>, response: Response<Comment1>) {
                                    if (response.isSuccessful) {
                                        activity!!.runOnUiThread() {
                                            val username = response.body()!!.user
                                            listCommentClinics.add(0, Comment1(cmt, currentDate, idClinic, username, rateValue))
                                            viewOfLayout.edtCommentClinic.text.clear()
                                            getAllComment()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Comment1>, t: Throwable) {
                                }
                            })

                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }
}
