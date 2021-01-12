package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Adapter.CommentAdapter
import com.hoanganh.drugstore.Adapter.ProductUsedAdapter
import com.hoanganh.drugstore.Adapter.ServiceAdapter
import com.hoanganh.drugstore.Model.Comment
import com.hoanganh.drugstore.Model.ProductUsed
import com.hoanganh.drugstore.Model.Services
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_information_drug_store.*
import kotlinx.android.synthetic.main.fragment_information_drug_store.view.*
import java.text.SimpleDateFormat
import java.util.*

class InformationDrugStoreFragment : Fragment() {
    lateinit var viewOfLayout: View
    private val listService = ArrayList<Services>()
    private val listProductUsed = ArrayList<ProductUsed>()
    private val listCommentDS = ArrayList<Comment>()
    private val now = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

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

    }


    private fun setDataService() {
        listService.add(Services(R.drawable.ic_pills, "Prescription"))
        listService.add(Services(R.drawable.ic_pharmacy, "Modulation"))
        listService.add(Services(R.drawable.ic_blood, "Blood Tests"))
        listService.add(Services(R.drawable.ic_cardiogram, "AED"))
        listService.add(Services(R.drawable.ic_wine, "Alcohol"))
        listService.add(Services(R.drawable.ic_coffee, "Coffee"))
        listService.add(Services(R.drawable.ic_parking, "Parking"))
    }

    private fun setDataProduct() {
        listProductUsed.add(ProductUsed(R.drawable.ic_medicine, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_apple, "Dietary Supplement"))
        listProductUsed.add(ProductUsed(R.drawable.ic_toothbrush, "Sanitary"))
        listProductUsed.add(ProductUsed(R.drawable.ic_bag, "Medicine"))
        listProductUsed.add(ProductUsed(R.drawable.ic_milk, "For Children"))
        listProductUsed.add(ProductUsed(R.drawable.ic_candy, "Candy"))
    }

    private fun setDataComment() {
        viewOfLayout.txtDateCommentDS.text = now
        viewOfLayout.reviewCommentDS.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }

        viewOfLayout.btnCommentDS.setOnClickListener {
            if (edtComment.text.toString() == "" ) {
                Toast.makeText(context, "Pls Enter Text", Toast.LENGTH_SHORT).show()
            } else if (reviewCommentDS.rating.toDouble() == 0.0){
                Toast.makeText(context, "Pls Enter Rating for you", Toast.LENGTH_SHORT).show()
            } else {
                val cmt = edtComment.text.toString()
                val currentDate = txtDateCommentDS.text.toString()
                val rateValue = reviewCommentDS.rating
                listCommentDS.add(0,Comment(cmt, "", rateValue, currentDate))
                val listCommentAdapter = CommentAdapter(listCommentDS)
                viewOfLayout.rcComment.layoutManager = LinearLayoutManager(context)
                viewOfLayout.rcComment.setItemViewCacheSize(2)
                viewOfLayout.rcComment.adapter = listCommentAdapter
            }
        }

    }


}


