package com.example.worldstory.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.admin_viewholder.RateViewHolder
import com.example.worldstory.model_for_test.Rate

class RateAdapter(private val rateList: List<Rate>) :
    RecyclerView.Adapter<RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rate_item_row, parent, false)
        return RateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rate = rateList[position]
        holder.c1.text = rate.userRated
        holder.c2.text = rate.rate.toString()
        holder.c3.text = rate.dateRate
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(android.graphics.Color.CYAN)
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }


    override fun getItemCount(): Int = rateList.size
}
