package com.example.worldstory.dat.admin_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.CategoryViewHolder
import com.example.worldstory.model_for_test.Category

class CategoryAdapter(private val categoryList: List<Category>, private var color:Int) :
    RecyclerView.Adapter<CategoryViewHolder>(), Filterable {
    private var filteredList: List<Category> = categoryList
    override fun getItemCount(): Int = filteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_row, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = filteredList[position]
        holder.column1.text = category.id
        holder.column2.text = category.name
        holder.column3.text = category.amount_Story
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
                    categoryList
                } else {
                    categoryList.filter {
                        it.name.contains(query, ignoreCase = true)
                                || it.id.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
                                || it.amount_Story.contains(query, ignoreCase = true)
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(p0: CharSequence, p1: FilterResults) {
                filteredList = p1.values as List<Category>
                notifyDataSetChanged()
            }

        }
    }
}