package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.PreviewViewHolder
import com.example.worldstory.duc.SampleDataStory
import com.squareup.picasso.Picasso

class PreviewUploadedAdapter(private var items: Map<Int, String>?) :
    RecyclerView.Adapter<PreviewViewHolder>() {
    override fun getItemCount(): Int = items?.size!!
    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        Picasso.get().load(items?.get(position)).into(holder.col)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_uploaded_item, parent, false)
        return PreviewViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMap(imgMap: Map<Int, String>?) {
        if (imgMap != null) {
            items = imgMap
        }
        notifyDataSetChanged()
    }
}