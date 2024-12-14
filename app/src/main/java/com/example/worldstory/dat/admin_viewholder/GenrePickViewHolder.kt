package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class GenrePickViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)  {
    val genreTextView=itemView.findViewById<TextView>(R.id.genre_name)
    val checkBox=itemView.findViewById<CheckBox>(R.id.checkbox_genre)
}