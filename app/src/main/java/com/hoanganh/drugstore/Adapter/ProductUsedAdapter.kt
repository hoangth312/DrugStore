package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.ProductUsed
import com.hoanganh.drugstore.Model.Services
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_product_used.view.*
import java.util.ArrayList

class ProductUsedAdapter(private val listProductUsed: ArrayList<ProductUsed>) : RecyclerView.Adapter<ProductUsedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_used, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productUsed: ProductUsed = listProductUsed[position]
        holder.itemView.imgProductUsed.setImageResource(productUsed.imgProductUsed)
        holder.itemView.txtNameProductUsed.text = productUsed.nameProductUsed
    }

    override fun getItemCount(): Int {
        return listProductUsed.size
    }

}