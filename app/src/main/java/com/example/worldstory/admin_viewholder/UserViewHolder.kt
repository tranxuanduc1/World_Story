package com.example.worldstory.admin_viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R;

public class UserViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
    val column1: TextView = itemView.findViewById(R.id.column1)
    val column2: TextView = itemView.findViewById(R.id.column2)
    val column3: TextView = itemView.findViewById(R.id.column3)
}
