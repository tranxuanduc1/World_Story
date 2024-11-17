package com.example.worldstory.admin_viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R.*
import com.example.myapplication.R.id.*


class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cmt: TextView = itemView.findViewById(id.cmt)
    val userName: TextView = itemView.findViewById(comment_user_name)
    val cmtDate: TextView = itemView.findViewById(cmt_date)
    val avatarImage: ImageView = itemView.findViewById(avatar_image)
    val rplBtn: Button = itemView.findViewById(rpl_btn)
    val hideBtn: Button = itemView.findViewById(hide_btn)
    val deleteBtn: ImageButton = itemView.findViewById(id.del_cmt)
}

