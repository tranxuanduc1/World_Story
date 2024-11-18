package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val column1: TextView = itemView.findViewById(R.id.ca_column1)
    val column2: TextView = itemView.findViewById(R.id.ca_column2)
    val column3: TextView = itemView.findViewById(R.id.ca_column3)
}