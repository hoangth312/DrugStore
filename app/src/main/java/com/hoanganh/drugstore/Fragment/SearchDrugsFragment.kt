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
import com.hoanganh.drugstore.model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.activity.MapsActivity
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CompanionObject.Companion.VIETNAMESE
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.hoanganh.drugstore.utils.InternetConnection
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_fragments.view.*
import kotlinx.android.synthetic.main.fragment_search_drugs.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchDrugsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private var dataList: ArrayList<SearchDrugsModel> = ArrayList()
    var navc: NavController? = null
    private var nameDrugSearch = ""
    var token: String = ""
    var type: String = ""
    var adapter: SearchDrugsAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_search_drugs, container, false)

        searchDrugByEditText()
        viewOfLayout.btnToMap.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
            activity?.onBackPressed()
        }
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
        linearLayoutManager = LinearLayoutManager(context)
        viewOfLayout.rvSearchDrugs.layoutManager = linearLayoutManager
        adapter = SearchDrugsAdapter(dataList, onItemClick = this::onCellClickListener)
        viewOfLayout.rvSearchDrugs.adapter = adapter
        adapter?.notifyDataSetChanged()
        setTotalResult()

    }


    private fun searchDrugByEditText() {

        viewOfLayout.edtSearchDrug.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                getListData()
            }
            false
        }
    }


    private fun getListData() {


        nameDrugSearch = viewOfLayout.edtSearchDrug.text.toString()
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        if (InternetConnection.checkConnection(requireContext())) {
            RetrofitClient.getApiService().getDrugSearchbyName("$type  $token",
                    VIETNAMESE, nameDrugSearch).enqueue(object : Callback<List<SearchDrugsModel>> {
                override fun onResponse(call: Call<List<SearchDrugsModel>>, response: Response<List<SearchDrugsModel>>) {
                    if (response.isSuccessful) {
                        dataList = response.body() as ArrayList<SearchDrugsModel>
                        activity!!.runOnUiThread {
                            if (dataList.size == 0) {
                                Toasty.error(context!!, "No matching results were found", Toast.LENGTH_SHORT, true).show()
                            }
                            setupView()
                        }
                    } else {
                        activity!!.runOnUiThread {
                            Toasty.error(context!!, "No matching results were found", Toast.LENGTH_SHORT, true).show()

                            //Toast.makeText(context, "lỗi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<SearchDrugsModel>>, t: Throwable) {
                    activity!!.runOnUiThread {
                        Toasty.error(context!!, "onFailure", Toast.LENGTH_SHORT, true).show()

                        //Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            requireActivity().runOnUiThread {
                Toasty.error(requireContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT, true).show()

               // Toast.makeText(requireContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCellClickListener(drugsModel: SearchDrugsModel) {
        navc?.navigate(R.id.action_fmSearchDugs_to_fmDrugInfo)
        val action = SearchDrugsFragmentDirections.actionFmSearchDugsToFmDrugInfo(drugsModel.id)
        navc?.navigateUp()
        navc?.navigate(action)

    }


}




