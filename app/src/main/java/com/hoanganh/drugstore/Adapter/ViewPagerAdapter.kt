package com.hoanganh.drugstore.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_image.view.*

class ViewPagerAdapter( var listImage: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {

    return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
     holder.itemView.imgBanner.setImageResource(listImage[position])
    }

    override fun getItemCount(): Int {
        return listImage.size
    }


}