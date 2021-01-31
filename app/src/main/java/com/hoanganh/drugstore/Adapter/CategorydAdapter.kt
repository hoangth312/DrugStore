package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.drugstore.Category
import kotlinx.android.synthetic.main.item_product_used.view.*

import kotlinx.android.synthetic.main.item_service.view.*
import java.util.ArrayList

class CategorydAdapter : RecyclerView.Adapter<CategorydAdapter.CategoryViewHolder>() {
    private var listProductUsed: ArrayList<Category> = arrayListOf()

    fun setData(list: ArrayList<Category>){
        listProductUsed.clear()
        listProductUsed.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_used, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val productUsed = listProductUsed[position]
       holder.bindData(productUsed)
    }

    override fun getItemCount(): Int {
        return listProductUsed.size
    }
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.imgCategory
        private val name = itemView.txtNameCategory

        fun bindData(category: Category) {
            
            when (category.categoryCode) {
                "DMP" -> {
                    image.setImageResource(R.drawable.ic_medicine)
                    name.text = category.categoryCode
                }
                "CSCT" -> {
                    image.setImageResource(R.drawable.ic_apple)
                    name.text = category.categoryCode
                }
                "SPVS" -> {
                    image.setImageResource(R.drawable.ic_toothbrushes)
                    name.text = category.categoryCode
                }
                "MPTD" -> {
                    image.setImageResource(R.drawable.ic_bag)
                    name.text = category.categoryCode
                }
                "SPTE" -> {
                    image.setImageResource(R.drawable.ic_milk)
                    name.text = category.categoryCode
                }
                "KEO" -> {
                    image.setImageResource(R.drawable.ic_candy)
                    name.text = category.categoryCode
                }
                else -> {
                    image.setImageResource(R.drawable.ic_candy)
                    name.text = category.categoryCode
                }
            }
        }
    }

}