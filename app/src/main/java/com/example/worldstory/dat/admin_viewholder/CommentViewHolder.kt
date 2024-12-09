package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.R.*
import com.example.myapplication.R.id.*


class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val id=itemView.findViewById<TextView>(id_user_cmt)
    val nickname=itemView.findViewById<TextView>(nickname_cmt)
    val username=itemView.findViewById<TextView>(username_cmt)
    val content=itemView.findViewById<TextView>(content_cmt)
    val date=itemView.findViewById<TextView>(date_cmt)

}

