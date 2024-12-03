package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.PreviewViewHolder

class PreviewTextFileAdapter(private val items: MutableMap<Int, Uri>?) :
    RecyclerView.Adapter<PreviewTextFileAdapter.PrvTxtViewHolder>() {

    class PrvTxtViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val txt: TextView = item.findViewById(R.id.prv_txt_item)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrvTxtViewHolder{
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.prv_txt_upload, parent, false)
        return PrvTxtViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrvTxtViewHolder, position: Int) {
        holder.txt.text= items?.get(position)?.toString() ?: "Error"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: MutableMap<Int, Uri>?){
        if (items != null) {
            items?.putAll(items)
        }
        notifyDataSetChanged()
    }

}



