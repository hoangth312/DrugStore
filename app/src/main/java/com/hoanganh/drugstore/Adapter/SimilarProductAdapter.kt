package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.product.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*

class SimilarProductAdapter(private val listSP: ArrayList<Product>, private val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<SimilarProductAdapter.ViewHoler>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoler {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
        )
        return ViewHoler(view, this::onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHoler, position: Int) {
        val products = listSP[position]
        holder.bindData(products)
    }

    private fun onItemClick(position: Int) {
        onItemClick.invoke(listSP[position])
    }

    override fun getItemCount(): Int {
        return listSP.size
    }

    class ViewHoler(itemView: View, onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener() {
                onItemClick.invoke(adapterPosition)
            }
        }
        fun bindData(product: Product) {
            itemView.apply {

                var listURL = product.imageProducts
                if (product.imageProducts[0] == "null"){
                    imgProduct.setImageResource(R.drawable.bear_test)
                }else {
                    imgProduct.visibility = View.VISIBLE
                    Picasso.get().load(listURL[0])
                            .error(R.drawable.bear_test)
                            .fit().into(imgProduct)
                }

                txtNameProduct.text = product.vietnameseName
                tax.text = product.price.toString()+" VNĐ("+product.priceTax.toString()+" VNĐ) "
                txtNumber.text = product.likeTotal.toString()
            }
        }
    }
}