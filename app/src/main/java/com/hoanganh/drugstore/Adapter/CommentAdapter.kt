package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.Model.Comment
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val listComment: ArrayList<com.hoanganh.drugstore.Model.Comment>) : RecyclerView.Adapter<CommentAdapter.Comment>() {

    class Comment(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Comment {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return Comment(view)
    }

    override fun onBindViewHolder(holder: Comment, position: Int) {
        val comment: com.hoanganh.drugstore.Model.Comment = listComment[position]
        holder.itemView.txtComment.text = listComment[position].comment
        holder.itemView.txtNameUser.text = listComment[position].nameUser
        holder.itemView.dateComment.text = listComment[position].date.toString()
        holder.itemView.reviewComment.numStars = listComment[position].review.toInt()
    }

    override fun getItemCount(): Int {
       return listComment.size
    }
}