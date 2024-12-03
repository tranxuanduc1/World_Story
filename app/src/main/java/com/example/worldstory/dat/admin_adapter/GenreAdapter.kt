package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.GenreViewHolder
import com.example.worldstory.model.Genre

class GenreAdapter(private var genreList: List<Genre>, private var color: Int) :
    RecyclerView.Adapter<GenreViewHolder>(), Filterable {
    private var filteredList: List<Genre> = genreList
    override fun getItemCount(): Int = filteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_item_row, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = filteredList.get(position)
        holder.column1.text = genre.genreID.toString()
        holder.column2.text = genre.genreName
        holder.column3.text = "32"
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(color)
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence): FilterResults {
                var query: String = p0.toString()
                filteredList = if (query.isEmpty()) {
                    genreList
                } else {
                    genreList.filter {
                        it.genreName.contains(query, ignoreCase = true)
                                || it.genreID.toString().contains(query, ignoreCase = true)
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<Genre>
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(genre: List<Genre>) {
        genreList = genre
        notifyDataSetChanged()
    }

    fun getGenre(p: Int): Genre {
        return filteredList[p]
    }

    fun remove(p: Int) {
        if (p >= 0 && p < genreList.size) {
            (genreList as MutableList).removeAt(p)
            notifyItemRemoved(p)
            notifyItemRangeChanged(p,genreList.size)
        }
    }
}