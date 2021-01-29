package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.drugstore.Product
import kotlinx.android.synthetic.main.item_product_used.view.*
import kotlinx.android.synthetic.main.item_service.view.*
import java.util.ArrayList

class ProductUsedAdapter(private val listProductUsed: ArrayList<Product>) : RecyclerView.Adapter<ProductUsedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.imgItemService
        private val name = itemView.txtItemNameService
        fun bindData(product: Product) {
            when (product.id) {
                1 -> {
                    image.setImageResource(R.drawable.ic_medicine)
                    name.text = product.englishName
                }
                2 -> {
                    image.setImageResource(R.drawable.ic_apple)
                    name.text = product.englishName
                }
                3 -> {
                    image.setImageResource(R.drawable.ic_toothbrush)
                    name.text = product.englishName
                }
                4 -> {
                    image.setImageResource(R.drawable.ic_bag)
                    name.text = product.englishName
                }
                5 -> {
                    image.setImageResource(R.drawable.ic_milk)
                    name.text = product.englishName
                }
                6 -> {
                    image.setImageResource(R.drawable.ic_candy)
                    name.text = product.englishName
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_used, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productUsed = listProductUsed[position]
       holder.bindData(productUsed)
    }

    override fun getItemCount(): Int {
        return listProductUsed.size
    }

}