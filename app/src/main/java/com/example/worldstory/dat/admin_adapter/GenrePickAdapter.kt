package com.example.worldstory.dat.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.GenrePickViewHolder
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModel
import com.example.worldstory.view_models.admin_viewmodels.StoryViewModel

class GenrePickAdapter(
    private val storyViewModel: StoryViewModel,
    private val genreViewModel: GenreViewModel
) : RecyclerView.Adapter<GenrePickViewHolder>() {

    override fun getItemCount(): Int = genreViewModel.genres.value?.size ?: 0

    override fun onBindViewHolder(holder: GenrePickViewHolder, position: Int) {
        val genre = genreViewModel.genres.value?.get(position)
        holder.genreTextView.text = genre?.genreName
        if (storyViewModel.name_Genres.contains(genre?.genreName)) {
            holder.checkBox.isChecked = true
            val i =
                genreViewModel.genres.value?.find {
                    it.genreName == holder.genreTextView.text.toString()
                }
            storyViewModel.genreEditList.add(i?.genreID ?: 0)
        } else holder.checkBox.isChecked = false

        holder.checkBox.setOnClickListener {
            val i =
                genreViewModel.genres.value?.find { it.genreName == holder.genreTextView.text.toString() }
            if (holder.checkBox.isChecked == true) {

                storyViewModel.genreEditList.add(i?.genreID ?: 0)
            } else {
                storyViewModel.genreEditList.remove(i?.genreID)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenrePickViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_pick_item, parent, false)
        return GenrePickViewHolder(view)
    }

}