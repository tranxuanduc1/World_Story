package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.duc.ducactivity.DucStoriesByGenreActivity
import com.example.worldstory.duc.ducutils.getKeyGenreInfo
import com.example.worldstory.duc.ducutils.getKeyIsText
import com.example.worldstory.duc.ducutils.getKeyStoriesByGenre
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre
import com.example.worldstory.model.Genre

class Duc_Button_Adapter(
    var context: Context,
    private val dataList: ArrayList<Genre>,
    private var isText: Boolean = false
) :
    RecyclerView.Adapter<Duc_Button_Adapter.ViewHolderClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.genre_item_layout, parent, false
        )

        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolderClass,
        position: Int
    ) {
        holder.btnGenre.setText(dataList[position].genreName)
        holder.txtIDGenre.setText(dataList[position].genreID.toString())
        holder.btnGenre.setOnClickListener {

            context.toActivityStoriesByGenre(isText,dataList[position])

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btnGenre = itemView.findViewById<Button>(R.id.btn_genreItemlayout)
        var txtIDGenre = itemView.findViewById<TextView>(R.id.txtIDGenre_genreItemlayout)
    }
}