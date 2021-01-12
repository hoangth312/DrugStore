package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Adapter.CommentAdapter
import com.hoanganh.drugstore.Adapter.ProductUsedAdapter
import com.hoanganh.drugstore.Adapter.ServiceAdapter
import com.hoanganh.drugstore.Model.Comment
import com.hoanganh.drugstore.Model.ProductUsed
import com.hoanganh.drugstore.Model.Service
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_information_drug_store.view.*
import java.util.ArrayList

class InformationDrugStoreFragment : Fragment() {
    lateinit var viewOfLayout: View
    val listService = ArrayList<Service>()
    val listProductUsed = ArrayList<ProductUsed>()
    val listComment = ArrayList<Comment>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        viewOfLayout = inflater.inflate(R.layout.fragment_information_drug_store, container, false)
        setupView()
        setDataService()
        setDataProduct()
        setDataComment()
        return viewOfLayout


    }



    private fun setupView() {
        viewOfLayout.rcService.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val serviceAdapter = ServiceAdapter(listService)
        viewOfLayout.rcService.adapter = serviceAdapter
        viewOfLayout.rcProductUsed.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val productUsedAdapter = ProductUsedAdapter(listProductUsed)
        viewOfLayout.rcProductUsed.adapter = productUsedAdapter
        viewOfLayout.rcProductUsed.layoutManager = LinearLayoutManager(context)
        val listCommentAdapter = CommentAdapter(listComment)
        viewOfLayout.rcProductUsed.adapter = listCommentAdapter
    }

    private fun setDataService() {
        listService.add(Service(R.drawable.ic_pills, "Prescription"))
        listService.add(Service(R.drawable.ic_pharmacy, "Modulation"))
        listService.add(Service(R.drawable.ic_blood, "BBlood Tests"))
        listService.add(Service(R.drawable.ic_cardiogram, "AED"))
        listService.add(Service(R.drawable.ic_wine, "Alcohol"))
        listService.add(Service(R.drawable.ic_coffee, "Coffee"))
        listService.add(Service(R.drawable.ic_parking, "Parking"))
    }

    private fun setDataProduct() {
        listProductUsed.add(ProductUsed(R.drawable.ic_medicine, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_apple, "Dietary Supplement"))
        listProductUsed.add(ProductUsed(R.drawable.ic_tooth, "Sanitary"))
        listProductUsed.add(ProductUsed(R.drawable.ic_bag, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_milk, "For Children"))
        listProductUsed.add(ProductUsed(R.drawable.ic_candy, "Candy"))
    }
    private fun setDataComment() {
        listComment.add(Comment("Drugstore not enthusiastic service and the service was not clean", "HoangHH", 3.0, 20/10/2020))
        listComment.add(Comment("Drugstore is good", "HoangHH", 4.5, 24/12/2020))
    }


}