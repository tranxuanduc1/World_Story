package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val img: ImageView = itemView.findViewById(R.id.img_story)
    val column2: TextView = itemView.findViewById(R.id.cl2)
    val column3: TextView = itemView.findViewById(R.id.cl3)
    val column4:TextView=itemView.findViewById(R.id.cl4)
}