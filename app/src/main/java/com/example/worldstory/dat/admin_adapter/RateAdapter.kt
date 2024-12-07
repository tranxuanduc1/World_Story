package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.RateViewHolder
import com.example.worldstory.model.Rate
import com.example.worldstory.model.User


class RateAdapter(private var userList: List<User>,private val context: Context) :
    RecyclerView.Adapter<RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rate_item_row, parent, false)
        return RateViewHolder(view)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val user = userList[position]
        holder.c2.text = user.userName
        holder.c3.text = user.nickName
        holder.c1.text = user.userID.toString()
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.Light_Beige))
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }


    override fun getItemCount(): Int = userList.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(newRateList: List<User>) {
        userList = newRateList
        notifyDataSetChanged()
    }
}
