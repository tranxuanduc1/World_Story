package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class GenreItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val checkBox = item.findViewById<CheckBox>(R.id.checkbox)
    val itemText=item.findViewById<TextView>(R.id.cate_item )



}