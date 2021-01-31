package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.hoanganh.drugstore.Adapter.BannerAdapter
import com.hoanganh.drugstore.Adapter.CommentAdapter
import com.hoanganh.drugstore.Adapter.CategorydAdapter
import com.hoanganh.drugstore.Adapter.ServiceAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.*
import com.hoanganh.drugstore.model.drugstore.Category
import com.hoanganh.drugstore.model.drugstore.DrugStoreModel

import com.hoanganh.drugstore.model.drugstore.Service
import com.hoanganh.drugstore.model.product.Product
import com.hoanganh.drugstore.preference.SharedPrefManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_fragments.view.*
import kotlinx.android.synthetic.main.fragment_information_drug_store.*
import kotlinx.android.synthetic.main.fragment_information_drug_store.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class InformationDrugStoreFragment : Fragment() {
    private var args by Delegates.notNull<LatLng>()
    private var latitudeStore: Double = 0.0
    private var longitudeStore: Double = 0.0
    lateinit var viewOfLayout: View
    private var token = ""
    private var type = ""
    private var listBanner = ArrayList<Banner>()
    private var listService = ArrayList<Service>()
    private var listCategory = ArrayList<Category>()
    private var listCommentDS = ArrayList<Comment1>()
    private val createDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_information_drug_store, container, false)
        viewOfLayout.toolbar_title.text = getString(R.string.detailOfDrugstore)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        setLatLng()
        getDataDrugStore()

        return viewOfLayout
    }

    private fun getDataBanner(idDrug: Int) {
        RetrofitClient.getApiService().getBannerDrug("$type  $token", idDrug).enqueue(object : Callback<List<Banner>> {
            override fun onResponse(call: Call<List<Banner>>, response: Response<List<Banner>>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        listBanner = response.body() as ArrayList<Banner>
                        val adapter = BannerAdapter(context, listBanner)
                        viewOfLayout.viewpagerDS.adapter = adapter
                        viewOfLayout.circleIndicator.setViewPager(viewpagerDS)
                    }
                }
            }

            override fun onFailure(call: Call<List<Banner>>, t: Throwable) {
            }
        })
    }


    private fun setLatLng() {
        args = InformationDrugStoreFragmentArgs.fromBundle(requireArguments()).latlngStore
        latitudeStore = args.latitude
        longitudeStore = args.longitude
    }


    private fun getDataDrugStore() {
        RetrofitClient
                .getApiService()
                .getDrugstore("$type  $token", latitudeStore, longitudeStore)
                .enqueue(object : Callback<DrugStoreModel> {
                    override fun onResponse(call: Call<DrugStoreModel>, response: Response<DrugStoreModel>) {
                        if (response.isSuccessful) {
                            activity!!.runOnUiThread() {
                                var idDrug = response.body()!!.id
                                viewOfLayout.txtNameDrugStore.text = response.body()!!.name
                                viewOfLayout.txtNameDrugStore.text = response.body()!!.name
                                viewOfLayout.ratingBar.rating = (response.body()!!.vote).toFloat()
                                viewOfLayout.txtAddress.text = response.body()!!.apartmentNumber + " " + response.body()!!.street + "," + response.body()!!.district + "," + response.body()!!.city
                                viewOfLayout.txtPhone.text = response.body()!!.phoneNumber
                                viewOfLayout.txtTimeWorking.text = "Open at AM " + response.body()!!.timeWorking + " PM Close"
                                listCommentDS = response.body()!!.evaluates as ArrayList<Comment1>
                                if (listCommentDS.size > 0) {
                                    getAllComment()
                                }
                                listService = response.body()!!.services as ArrayList<Service>
                                if (listService.size > 0) {
                                    getAllSerVice()
                                }
                                listCategory = response.body()!!.categories as ArrayList<Category>
                                if (listCategory.size > 0) {
                                    getAllProduct()
                                }
                                getDataBanner(idDrug)
                                setDataComment(idDrug)
                            }
                        }
                    }

                    override fun onFailure(call: Call<DrugStoreModel>, t: Throwable) {
                        activity!!.runOnUiThread() {
                        }
                    }
                })

    }

    private fun getAllProduct() {
        val listCategorydAdapter = CategorydAdapter()
        viewOfLayout.rcProductUsed.apply {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = listCategorydAdapter
        }
        listCategorydAdapter.setData(listCategory)
    }

    private fun getAllSerVice() {
        val listServiceAdapter = ServiceAdapter()
        viewOfLayout.rcService.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = listServiceAdapter
        }
        listServiceAdapter.setData(listService)
    }

    private fun getAllComment() {
        val listCommentAdapter = CommentAdapter(listCommentDS)
        viewOfLayout.rcCommentDS.layoutManager = LinearLayoutManager(context)
        viewOfLayout.rcCommentDS.adapter = listCommentAdapter
        listCommentAdapter.notifyDataSetChanged()

    }


    private fun setDataComment(idDrug: Int) {
        viewOfLayout.txtDateCommentDS.text = createDate
        viewOfLayout.reviewCommentDS.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }
        viewOfLayout.btnCommentDS.setOnClickListener {
            when {
                edtComment.text.toString() == "" -> {
                    Toasty.error(requireContext(), "Pls Enter Text", Toast.LENGTH_SHORT, true).show()
                }
                reviewCommentDS.rating.toDouble() == 0.0 -> {
                    Toasty.error(requireContext(), "Pls Enter Rating for you", Toast.LENGTH_SHORT, true).show()
                }
                else -> {
                    val cmt = edtComment.text.toString()
                    val currentDate = txtDateCommentDS.text.toString()
                    val rateValue = reviewCommentDS.rating.toDouble()
                    val user = SharedPrefManager.getInstance(requireContext()).getID()
                    RetrofitClient.getApiService()
                            .addCommentDS("$type $token", Comment(cmt, idDrug, null, user, rateValue))
                            .enqueue(object : Callback<Comment1> {
                                override fun onResponse(call: Call<Comment1>, response: Response<Comment1>) {
                                    if (response.isSuccessful) {
                                        activity!!.runOnUiThread {
                                            val username = response.body()!!.user
                                            listCommentDS.add(0, Comment1(cmt, currentDate, idDrug, username, rateValue))
                                            viewOfLayout.edtComment.text.clear()
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


