package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.rememberUpdatedState
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.ChapterViewHolder
import com.example.worldstory.dat.admin_viewholder.CommentViewHolder
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.model.Chapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChapterAdapter(private var chapterList: List<Chapter>?) :
    RecyclerView.Adapter<ChapterViewHolder>() {
    override fun getItemCount(): Int = chapterList?.size ?: 0


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapterList?.get(position)
        holder.col1.text = "Chapter $position"
        holder.col2.text = chapter?.title
        holder.col3.text = getFormatedDate(chapter?.dateCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chapter_item_about_story, parent, false)
        return ChapterViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newChapterList: List<Chapter>?) {
        chapterList = newChapterList
        Log.w("adapter", chapterList?.size.toString())
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormatedDate(dateTime: String?): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDateTime.parse(dateTime, inputFormatter)
        val formattedDate = date.format(outputFormatter)
        return formattedDate

    }
}