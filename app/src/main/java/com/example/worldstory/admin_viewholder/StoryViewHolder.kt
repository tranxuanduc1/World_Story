package com.example.worldstory.admin_viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val column1: TextView = itemView.findViewById(R.id.cl1)
    val column2: TextView = itemView.findViewById(R.id.cl2)
    val column3: TextView = itemView.findViewById(R.id.cl3)
}