package com.example.worldstory.dat.admin_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.CommentViewHolder
import com.example.worldstory.model.Comment
import com.example.worldstory.model.User


class CommentAdapter(private val commentMap: Map<Comment,User>, private val commentList:List<Comment>,private val context: Context) :
    RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item_row, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment=commentList[position]
        val user=commentMap[comment]
        holder.id.text = user?.userID.toString()
        holder.nickname.text = user?.nickName
        holder.username.text = user?.userName
        holder.content.text=comment.content
        holder.date.text=comment.time

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.Light_Beige))
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
//        // Xử lý sự kiện cho các button
//        holder.rplBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Reply
//        }
//
//        holder.hideBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Hide
//        }
//
//        holder.deleteBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Delete
//        }
    }

    override fun getItemCount(): Int = commentList.size
}

