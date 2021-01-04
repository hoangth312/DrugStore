package com.hoanganh.drugstore.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Adapter.ProductUsedAdapter
import com.hoanganh.drugstore.Adapter.ServiceAdapter
import com.hoanganh.drugstore.Model.ProductUsed
import com.hoanganh.drugstore.Model.Services
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_information_drug_store.view.*
import java.util.ArrayList

class InformationDrugStoreFragment : Fragment() {
    lateinit var viewOfLayout: View
    val listService = ArrayList<Services>()
    val listProductUsed = ArrayList<ProductUsed>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_information_drug_store, container, false)
        setupView()
        setDataService()
        setDataProduct()
        return viewOfLayout


    }


    private fun setupView() {
        viewOfLayout.rcService.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val serviceAdapter = ServiceAdapter(listService)
        viewOfLayout.rcService.adapter = serviceAdapter
        viewOfLayout.rcProductUsed.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val productUsedAdapter = ProductUsedAdapter(listProductUsed)
        viewOfLayout.rcProductUsed.adapter = productUsedAdapter
    }

    private fun setDataService() {
        listService.add(Services(R.drawable.ic_pills, "Prescription"))
        listService.add(Services(R.drawable.ic_pharmacy, "Modulation"))
        listService.add(Services(R.drawable.ic_blood, "BBlood Tests"))
        listService.add(Services(R.drawable.ic_cardiogram, "AED"))
        listService.add(Services(R.drawable.ic_wine, "Alcohol"))
        listService.add(Services(R.drawable.ic_coffee, "Coffee"))
        listService.add(Services(R.drawable.ic_parking, "Parking"))
    }

    private fun setDataProduct() {
        listProductUsed.add(ProductUsed(R.drawable.ic_medicine, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_apple, "Dietary Supplement"))
        listProductUsed.add(ProductUsed(R.drawable.ic_tooth, "Sanitary"))
        listProductUsed.add(ProductUsed(R.drawable.ic_bag, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_milk, "For Children"))
        listProductUsed.add(ProductUsed(R.drawable.ic_candy, "Candy"))


    }

}