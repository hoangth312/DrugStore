package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.drugstore.Service
import kotlinx.android.synthetic.main.item_service.view.*
import java.util.ArrayList

class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    private var listService: ArrayList<Service> = arrayListOf()

    fun setData(list: ArrayList<Service>){
        listService.clear()
        listService.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val services = listService[position]
        holder.bindData(services)
    }

    override fun getItemCount(): Int {
        return listService.size
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.imgItemService
        private val name = itemView.txtItemNameService

        fun bindData(service: Service) {
            when (service.serviceCode) {
                "KSK" -> {
                    image.setImageResource(R.drawable.ic_pills)
                    name.text = service.serviceCode
                }
                "KR" -> {
                    image.setImageResource(R.drawable.ic_pharmacy)
                    name.text = service.serviceCode
                }
                "KNK" -> {
                    image.setImageResource(R.drawable.ic_blood)
                    name.text = service.serviceCode
                }
                "KT" -> {
                    image.setImageResource(R.drawable.ic_cardiogram)
                    name.text = service.serviceCode
                }
                "NN" -> {
                    image.setImageResource(R.drawable.ic_wine)
                    name.text = service.serviceCode
                }
                else -> {
                    image.setImageResource(R.drawable.ic_pills)
                    name.text = service.serviceCode
                }
//                6 -> {
//                    image.setImageResource(R.drawable.ic_coffee)
//                    name.text = service.serviceCode
//                }
//                7 -> {
//                    image.setImageResource(R.drawable.ic_parking)
//                    name.text = service.serviceCode
//                }
            }
        }
    }
}