package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.Services
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_service.view.*
import java.util.ArrayList

class ServiceAdapter(private var listService: ArrayList<Services>) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.imgItemService
        private val name = itemView.txtItemNameService

        fun bindData(services: Services) {
            image.setImageResource(services.imgServices)
            name.text = services.nameService
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = listService[position]
        holder.bindData(services)
    }

    override fun getItemCount(): Int {
        return listService.size
    }
}