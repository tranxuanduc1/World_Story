package com.example.worldstory.admin_adapter

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
import com.example.worldstory.model_for_test.Story
import com.example.worldstory.admin_viewholder.StoryViewHolder

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

    override fun getItemCount(): Int = filteredList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.story_item_row, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = filteredList[position]
        holder.column1.text = highlightQuery(text = story.id, query = searchQuery)
        holder.column2.text = highlightQuery(text = story.name, query = searchQuery)
        holder.column3.text = highlightQuery(text = story.tacGia, query = searchQuery)
        holder.itemView.setOnClickListener {
            listener.onItemClick(story)
        }
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
                    storyList
                } else if (filteredList.isEmpty()) {
                    storyList.filter {
                        it.name.contains(query, ignoreCase = true)
                                || it.id.contains(query, ignoreCase = true) || it.tacGia.contains(
                            query,
                            ignoreCase = true
                        )
                                || it.theLoai.joinToString(separator = ",")
                            .contains(query, ignoreCase = true)
                    }
                } else {
                    filteredList.filter {
                        it.name.contains(query, ignoreCase = true)
                                || it.id.contains(query, ignoreCase = true) || it.tacGia.contains(
                            query,
                            ignoreCase = true
                        )
                                || it.theLoai.joinToString(separator = ",")
                            .contains(query, ignoreCase = true)
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

    public fun updateSearchQuery(query: String?) {
        searchQuery = query as String
        notifyDataSetChanged()
    }
}