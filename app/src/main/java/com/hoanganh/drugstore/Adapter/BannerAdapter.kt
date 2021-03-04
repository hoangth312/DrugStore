package com.hoanganh.drugstore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.hoanganh.drugstore.R
import com.squareup.picasso.Picasso
import com.hoanganh.drugstore.model.Banner

class BannerAdapter(val context: Context?, private val listBanner: ArrayList<Banner>) : PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_banner, container, false)
        val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
        Picasso.get().load(listBanner.get(position).imageUrl).into(imgBanner)
        container.addView(view, 0)
        return view
    }


    override fun getCount(): Int {
        if (listBanner != null) {
            return listBanner.size
        } else
            return 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}