package com.hoanganh.drugstore.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.R
import com.squareup.picasso.Picasso
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

                var listURL = model.imageProducts
                if (model.imageProducts[0] == "null"){
                    imageDrugs.setImageResource(R.drawable.bear_test)
                }else {
                    imageDrugs.visibility = View.VISIBLE


                    Picasso.get().load(listURL[0])
                            .error(R.drawable.bear_test)
                            .fit().into(imageDrugs);
                }

                nameDrug.text = model.vietnameseName
                subTitleDrug.text = model.categoryName
                descriptionDrug.text = model.description
                numberOfLikes.text = model.likeTotal.toString()
                numberOfCmt.text = model.commentTotal.toString()
            }
        }
    }
}



