package com.hoanganh.drugstore.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.model.product.EvaluateRequest
import kotlinx.android.synthetic.main.item_comment.view.*

class EvaluateAdapter(private val listComment: ArrayList<EvaluateRequest>) : RecyclerView.Adapter<EvaluateAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evaluate_product, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val evaluateRequest = listComment[position]
        holder.itemView.txtComment.text = evaluateRequest.comment
        holder.itemView.dateComment.text = evaluateRequest.updatedDate
        if (evaluateRequest.user != null) {
            holder.itemView.txtNameUser.text = evaluateRequest.user.userName
        }
    }

    override fun getItemCount(): Int {
        return listComment.size

    }
}