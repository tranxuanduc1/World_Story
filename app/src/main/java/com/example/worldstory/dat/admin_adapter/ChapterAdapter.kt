package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.ChapterViewHolder
import com.example.worldstory.dat.admin_viewholder.CommentViewHolder
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.model.Chapter

class ChapterAdapter(private var chapterList: List<Chapter>?) :
    RecyclerView.Adapter<ChapterViewHolder>() {
    override fun getItemCount(): Int = chapterList?.size?:0


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapterList?.get(position)
        holder.col1.text = "Tập $position"
        holder.col2.text = chapter?.title
        holder.col3.text = chapter?.dateCreated
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chapter_item_about_story, parent, false)
        return ChapterViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newChapterList: List<Chapter>?) {
        chapterList=newChapterList
        Log.w("adapter",chapterList?.size.toString())
        notifyDataSetChanged()
    }

}