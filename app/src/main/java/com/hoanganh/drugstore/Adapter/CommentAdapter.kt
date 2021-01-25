package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.model.Comment
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val listComment: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment: Comment = listComment[position]

        holder.itemView.txtComment.text = comment.comment
        holder.itemView.txtNameUser.text = comment.nameUser
        holder.itemView.dateComment.text = comment.date
        holder.itemView.reviewComment.rating = comment.review.toFloat()
    }

    override fun getItemCount(): Int {
        return if (listComment.size > 2) {
            2
        } else {
            listComment.size
        }

    }
}