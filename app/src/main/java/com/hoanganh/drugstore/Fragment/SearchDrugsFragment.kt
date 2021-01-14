package com.hoanganh.drugstore.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanganh.drugstore.Adapter.SearchDrugsAdapter
import com.hoanganh.drugstore.Model.SearchDrugsModel
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.activity.MapsActivity
import kotlinx.android.synthetic.main.app_bar_fragments.view.*
import kotlinx.android.synthetic.main.fragment_search_drugs.view.*

class SearchDrugsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var dataList: ArrayList<SearchDrugsModel>
    var navc: NavController? = null

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
        setData()
        setupView()
        setTotalResult()

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

    private fun setData() {
        for (i in 0..10) {
            dataList.add(
                SearchDrugsModel(
                    R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    165, 23
                )
            )
        }
    }

    private fun onCellClickListener(drugModel: SearchDrugsModel) {
        navc?.navigate(R.id.action_fmSearchDugs_to_fmDrugInfo2)
        Toast.makeText(context, "123", Toast.LENGTH_SHORT).show()
    }
}