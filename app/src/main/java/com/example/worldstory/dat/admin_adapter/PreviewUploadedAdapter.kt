package com.example.worldstory.dat.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.PreviewViewHolder

class PreviewUploadedAdapter(private val items: List<String>) :
    RecyclerView.Adapter<PreviewViewHolder>() {
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.col.text = items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_uploaded_item, parent, false)
        return PreviewViewHolder(view)
    }
}