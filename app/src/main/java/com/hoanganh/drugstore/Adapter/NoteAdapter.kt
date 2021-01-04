package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.Clinics
import com.hoanganh.drugstore.Model.SearchDrugsModel
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_note_clinic.view.*

class NoteAdapter(private val listNote: ArrayList<Clinics>) : RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder>() {


    class NoteAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_clinic, parent, false)
        return NoteAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapterViewHolder, position: Int) {
       val clinics: Clinics = listNote[position]
        holder.itemView.txtNoteClinic.text = clinics.note
    }

    override fun getItemCount(): Int {
       return listNote.size
    }
}