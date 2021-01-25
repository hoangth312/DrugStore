package com.hoanganh.drugstore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.hoanganh.drugstore.model.Banner

class BannerAdapter(context : Context, private val listBanner : List<Banner>) : PagerAdapter()
{
    lateinit var layoutInflater: LayoutInflater
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        layoutInflater = LayoutInflater.from(c)
//        val view = layoutInflater.inflate(R.layout.item_tutorial,container,false)
//        val imgTutorial = view.findViewById<ImageView>(R.id.imgTutorial)
//        imgTutorial.setImageResource(list.get(position))
//        container.addView(view, 0)
//        return view
//    }


    override fun getCount(): Int {
        if (listBanner != null){
            return  listBanner.size
        }else
            return 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view == `object`
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}