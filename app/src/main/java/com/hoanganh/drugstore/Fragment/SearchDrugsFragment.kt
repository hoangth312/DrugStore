package com.hoanganh.drugstore.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanganh.drugstore.Adapter.SearchDrugsAdapter
import com.hoanganh.drugstore.Model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.activity.MapsActivity
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.hoanganh.drugstore.utils.InternetConnection
import kotlinx.android.synthetic.main.app_bar_fragments.view.*
import kotlinx.android.synthetic.main.fragment_search_drugs.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDrugsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var dataList: ArrayList<SearchDrugsModel>
    var navc: NavController? = null
    private var nameDrugSearch = ""
    var token: String = ""
    var type: String = ""
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_search_drugs, container, false)
        dataList = ArrayList()
        viewOfLayout.btnToMap.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
            activity?.onBackPressed()
        }
        setTotalResult()
        searchDrugByEditText()
        setupView()
        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
    }

    private fun setTotalResult() {
        val number = if (dataList.size < 10) {
            "0" + dataList.size
        } else dataList.size.toString()
        viewOfLayout.totalResult.text = number
    }

    private fun setupView() {
        viewOfLayout.rvSearchDrugs.layoutManager = LinearLayoutManager(context)
        val adapter = SearchDrugsAdapter(dataList, this::onCellClickListener)
        viewOfLayout.rvSearchDrugs.adapter = adapter
    }


    private fun searchDrugByEditText() {

        viewOfLayout.edtSearchDrug.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                setData()
            }
            false
        }
    }


    private fun setData() {
        nameDrugSearch = viewOfLayout.edtSearchDrug.text.toString()
        token = SharedPrefManager.getInstance(context).getToken().toString()
        type = SharedPrefManager.getInstance(context).getType().toString()

        if (InternetConnection.checkConnection(requireContext())) {


            RetrofitClient.getApiService().getDrugSearchbyName("$type $token",
                    "VIETNAMESE", nameDrugSearch).enqueue(object : Callback<List<SearchDrugsModel>> {
                override fun onResponse(call: Call<List<SearchDrugsModel>>, response: Response<List<SearchDrugsModel>>) {
                    if (response.isSuccessful) {
                        dataList = response.body() as ArrayList<SearchDrugsModel>



                    } else {
                        Toast.makeText(context, "lỗi", Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onFailure(call: Call<List<SearchDrugsModel>>, t: Throwable) {
                    Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show()
                }
            })


        } else {
            Toast.makeText(requireContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show()
        }
//        for (i in 0..10) {
//            dataList.add(
//                    SearchDrugsModel(
//                            R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
//                            "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
//                            "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
//                            165, 23
//                    )
//            )
//        }
    }

    private fun onCellClickListener(drugModel: SearchDrugsModel) {
        navc?.navigate(R.id.action_fmSearchDugs_to_fmDrugInfo2)
        Toast.makeText(context, "123", Toast.LENGTH_SHORT).show()
    }
}




