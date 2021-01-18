package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_rv_search_drugs.view.*

class SearchDrugsAdapter(
    private val listDrug: ArrayList<SearchDrugsModel>,
    private val onItemClick: (SearchDrugsModel) -> Unit
) : RecyclerView.Adapter<SearchDrugsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_rv_search_drugs,
            parent,
            false
        )
        return ViewHolder(view, this::onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drugsModel = listDrug[position]
        holder.bindData(drugsModel)

    }

    private fun onItemClick(position: Int) {
        onItemClick.invoke(listDrug[position])
    }

    override fun getItemCount(): Int {
        return listDrug.size
    }

    class ViewHolder(itemView: View, onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
            }
        }

        fun bindData(model: SearchDrugsModel) {
            itemView.apply {
              //  imageDrugs.setImageResource(model.imageProducts.get(0))
                nameDrug.text = model.vietnameseName
                subTitleDrug.text = model.categoryName
                descriptionDrug.text = model.description
                numberOfLikes.text = model.likeTotal.toString()
                numberOfCmt.text = model.commentTotal.toString()
            }
        }
    }
}

