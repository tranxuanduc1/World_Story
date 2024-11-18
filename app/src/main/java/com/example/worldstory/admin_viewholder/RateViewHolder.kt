package com.example.worldstory.admin_viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RateViewHolder(item:View):RecyclerView.ViewHolder(item) {
    val c1:TextView=item.findViewById(R.id.cl1_rate)
    val c2:TextView=item.findViewById(R.id.cl2_rate)
    val c3:TextView=item.findViewById(R.id.cl3_rate)
}