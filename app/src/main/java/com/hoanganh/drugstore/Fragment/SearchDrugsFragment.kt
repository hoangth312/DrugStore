package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanganh.drugstore.Adapter.SearchDrugsAdapter
import com.hoanganh.drugstore.Model.SearchDrugsModel
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_search_drugs.view.*


class SearchDrugsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    val dataList = ArrayList<SearchDrugsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_search_drugs, container, false)

        setupView()
        setData()

        setTotalResult()


        return viewOfLayout

    }

    private fun setTotalResult(){
        var number = ""
        number = if (dataList.size < 10) {
            "0" + dataList.size
        }else dataList.size.toString()

        viewOfLayout.totalResult.text = number

    }

    private fun setupView() {
        viewOfLayout.rvSearchDrugs.layoutManager = LinearLayoutManager(context)
        val adpter = SearchDrugsAdapter(dataList)
        viewOfLayout.rvSearchDrugs.adapter = adpter
    }



        private fun setData(){

        dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                165, 23))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPASssssss ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    15, 25))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    665, 13))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    755, 83))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    55, 63))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    785, 33))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    75, 12))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    73, 53))
            dataList.add(SearchDrugsModel(R.drawable.salonpas, "CAO DÁN SALONPAS (HỘP 20 MIẾNG) ",
                    "Công ty TNHH Dược phẩm Hisamitsu Việt Nam",
                    "Cao dán Salonpas dùng để giảm đau nhanh chóng, kháng viêm trong các cơn đau liên quan đến các triệu chứng đau khớp, đau cơ, đau lưng, căng cơ, bong gân, đau răng. Sản phẩm là giải pháp tiện lợi chăm sóc cho sức khỏe gia đình bạn.",
                    15, 5))
        }
}