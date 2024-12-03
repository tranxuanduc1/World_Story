package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.PreviewViewHolder
import com.example.worldstory.duc.SampleDataStory
import com.squareup.picasso.Picasso

class PreviewUploadedAdapter(private val items: MutableMap<Int, Uri>?) :
    RecyclerView.Adapter<PreviewViewHolder>() {
    override fun getItemCount(): Int = items?.size?:0
    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        Picasso.get().load(items?.get(position)).into(holder.col)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_uploaded_item, parent, false)
        return PreviewViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMap(imgMap: Map<Int, Uri>?) {
        if (imgMap != null) {
            items?.putAll(imgMap)
        }
        notifyDataSetChanged()
    }
}