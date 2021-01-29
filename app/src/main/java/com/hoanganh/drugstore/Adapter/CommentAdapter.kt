package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.Comment1
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val listComment: ArrayList<Comment1>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment1 = listComment[position]
        if (comment1.comment != null) {
            holder.itemView.txtComment.text = comment1.comment
        }
        holder.itemView.dateComment.text = comment1.updatedDate
        if (comment1.user != null) {
            holder.itemView.txtNameUser.text = comment1.user.userName
        }
        holder.itemView.reviewComment.rating = comment1.vote.toFloat()
    }

    override fun getItemCount(): Int {
        return listComment.size

    }
}