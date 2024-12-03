package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.StoryViewHolder
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.model.Story
import com.squareup.picasso.Picasso

interface OnItemClickListener {
    fun onItemClick(item: Story)
}

class StoryAdapter(
    private var storyList: List<Story>,
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

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = filteredList[position]
        Picasso.get().load(SampleDataStory.getExampleImgURL()).into(holder.img)
        holder.column2.text = highlightQuery(text = story.title, query = searchQuery)
        holder.column3.text = highlightQuery(text = story.author, query = searchQuery)
        holder.column4.text = highlightQuery(text = story.createdDate, query = searchQuery)
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
                viewModel.storyGenreMap[it.storyID]?.contains(catesMap.entries.first().key)
                true
            }
        else if (catesMap.size == 0) filteredList = storyList
        else
            catesMap.forEach { c ->
                filteredList =
                    filteredList.filter {
                        viewModel.storyGenreMap[it.storyID]?.contains(c.key)
                        true
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
//                        it.name.contains(query, ignoreCase = true)
//                                || it.id.contains(query, ignoreCase = true) || it.tacGia.contains(
//                            query,
//                            ignoreCase = true
//                        )
//                                || it.theLoai.joinToString(separator = ",")
//                            .contains(query, ignoreCase = true)
                        true
                    }
                }

                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<Story>
                notifyDataSetChanged()
            }

        }

    }

    private fun highlightQuery(text: String, query: String): SpannableString {
        val spannableString = SpannableString(text)
        if (query.isNotEmpty()) {
            val startIndex = text.lowercase().indexOf(query.lowercase())
            if (startIndex >= 0) {
                val endIndex = startIndex + query.length
                spannableString.setSpan(
                    ForegroundColorSpan(Color.Yellow.hashCode()),  // Màu tô vàng
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchQuery(query: String?) {
        searchQuery = query as String
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(stories: List<Story>) {
        storyList = stories
        notifyDataSetChanged()
    }

    fun getStory(p:Int):Story{
        return filteredList[p]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(p:Int){
        if(p>=0 && p < storyList.size){
            (storyList as MutableList).removeAt(p)
            notifyItemRemoved(p)
            notifyItemRangeChanged(p,storyList.size)
        }
    }
}