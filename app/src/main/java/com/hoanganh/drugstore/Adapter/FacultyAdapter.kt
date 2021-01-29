package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.clinic.Faculty
import kotlinx.android.synthetic.main.item_faculty_clinic.view.*


class FacultyAdapter(private val listFaculty: ArrayList<Faculty>) : RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder>() {
    class FacultyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faculty_clinic, parent, false)
        return FacultyViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
       val faculty: Faculty = listFaculty[position]
        holder.itemView.txtFacultyClinic.text = faculty.facultyName

    }

    override fun getItemCount(): Int {
       return listFaculty.size
    }
}