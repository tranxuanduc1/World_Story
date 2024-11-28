package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.TextView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ChapterViewHolder(item:View):RecyclerView.ViewHolder(item) {
    val col1= item.findViewById<TextView>(R.id.STT )
    val col2= item.findViewById<TextView>(R.id.title_chapter )
    val col3= item.findViewById<TextView>(R.id.created_date_chapter )
}