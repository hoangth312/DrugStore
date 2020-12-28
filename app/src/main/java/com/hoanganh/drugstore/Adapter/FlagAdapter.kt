package com.hoanganh.drugstore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.hoanganh.drugstore.Model.Flags
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_category_flags.view.*

class FlagAdapter(context: Context,  listFlags:ArrayList<Flags>): ArrayAdapter<Flags>(context, 0, listFlags) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val country  = getItem(position)

          val  view = convertView?: LayoutInflater.from(context).inflate(R.layout.item_category_flags,parent,false)
        view.imFlags.setImageResource(country !!.flag)

            return view
    }

}
