package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.StoryViewHolder
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.model.Story
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

interface OnItemClickListener {
    fun onItemClick(item: Story)
}

class StoryAdapter(
    private var storyList: MutableList<Story>,
    private var color: Int,
    private val listener: OnItemClickListener

) :
    RecyclerView.Adapter<StoryViewHolder>(), Filterable {
    private var filteredList: List<Story> = storyList
    private var searchQuery: String = ""
    private var sizeOfChipList: Int = 0

    override fun getItemCount(): Int = filteredList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.story_item_row, parent, false)
        return StoryViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = filteredList[position]
        Picasso.get().load(SampleDataStory.getExampleImgURL()).into(holder.img)
        holder.column2.text = story.title
        holder.column3.text = story.author
        holder.column4.text = getFormatedDate(story.createdDate)
        holder.itemView.setOnClickListener {
            listener.onItemClick(story)

        }
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(color)
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)


    }

    fun setSizeChipList(size: Int) {
        sizeOfChipList = size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterByCates(catesMap: Map<Int, String>, viewModel: StoryViewModel) {
        if (catesMap.size == 1) filteredList =
            storyList.filter {
                viewModel.storyGenreMap[it.storyID]?.contains(catesMap.entries.first().key) == true
            }
        else if (catesMap.size == 0) filteredList = storyList
        else
            catesMap.forEach { c ->
                filteredList =
                    filteredList.filter {
                        viewModel.storyGenreMap[it.storyID]?.contains(c.key) == true
                    }
            }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence): FilterResults {
                var query: String = p0.toString()
                filteredList = if (query.isEmpty()) {
                    storyList
                } else {
                    storyList.filter {
                        it.title.contains(query, ignoreCase = true)
                                || it.author.contains(
                            query,
                            ignoreCase = true
                        ) || it.createdDate.contains(
                            query,
                            ignoreCase = true
                        )
                        true
                    }
                }

                val result = FilterResults()
                result.values = filteredList
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<Story>
                notifyDataSetChanged()
            }

        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchQuery(query: String?) {
        searchQuery = query as String
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(stories: List<Story>) {
        Log.w("update", stories.size.toString())
        storyList.clear()
        storyList.addAll(stories)
        notifyDataSetChanged()
    }

    fun getStory(p: Int): Story {
        return filteredList[p]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(p: Int) {
        if (p >= 0 && p < storyList.size) {
            (storyList as MutableList).removeAt(p)
            notifyItemRemoved(p)
            notifyItemRangeChanged(p, storyList.size)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun sortId() {
        storyList = storyList.sortedBy { it.storyID }.toMutableList()
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun desortId() {
        storyList = storyList.sortedByDescending { it.storyID }.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortDate() {
        storyList = storyList.sortedBy { formatDate(it.createdDate) }.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deSortDate() {
        storyList = storyList.sortedByDescending { formatDate(it.createdDate) }.toMutableList()
        notifyDataSetChanged()
    }


    fun formatDate(dateTime: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.parse(dateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormatedDate(dateTime: String?): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH/mm/ss")
        val date = LocalDateTime.parse(dateTime, inputFormatter)
        val formattedDate = date.format(outputFormatter)
        return formattedDate

    }
}