package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.SearchDrugsModel
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_rv_search_drugs.view.*

class SearchDrugsAdapter(private val listDrug: ArrayList<SearchDrugsModel>): RecyclerView.Adapter<SearchDrugsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_search_drugs,
                parent,
                false
        )
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val drugsModel: SearchDrugsModel = listDrug[position]

       holder.itemView.imageDrugs.setImageResource(listDrug[position].image)
        holder.itemView.nameDrug.text = listDrug[position].name
        holder.itemView.subTitleDrug.text = listDrug[position].subTilte
        holder.itemView.descriptionDrug.text = listDrug[position].description
        holder.itemView.numberOfLikes.text = listDrug[position].numLike.toString()
        holder.itemView.numberOfCmt.text = listDrug[position].numCmt.toString()




    }

    override fun getItemCount(): Int {
        return listDrug.size
    }
}