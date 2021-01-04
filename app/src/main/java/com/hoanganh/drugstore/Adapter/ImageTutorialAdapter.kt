package com.hoanganh.drugstore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.hoanganh.drugstore.R

class ImageTutorialAdapter( var context: Context?,var list: List<Int>): PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_tutorial,container,false)
        val imgTutorial = view.findViewById<ImageView>(R.id.imgTutorial)
        imgTutorial.setImageResource(list.get(position))
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       container.removeView(`object` as View)
    }
}