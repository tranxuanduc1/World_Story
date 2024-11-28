package com.example.worldstory.dat.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.CommentViewHolder
import com.example.worldstory.model_for_test.Comment


class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item_row, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.cmt.text = comment.content
        holder.userName.text = comment.userName
        holder.cmtDate.text = comment.date
        holder.avatarImage.setImageResource(comment.avatarResId)

        // Xử lý sự kiện cho các button
        holder.rplBtn.setOnClickListener {
            // Code xử lý khi nhấn nút Reply
        }

        holder.hideBtn.setOnClickListener {
            // Code xử lý khi nhấn nút Hide
        }

        holder.deleteBtn.setOnClickListener {
            // Code xử lý khi nhấn nút Delete
        }
    }

    override fun getItemCount(): Int = commentList.size
}

