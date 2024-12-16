package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.myapplication.R
import com.example.worldstory.view.ducactivity.DucStoryOverviewActivity
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.data.model.Story

class Duc_ListSearch_ArrayAdapter(
    var appContext: Context,private val resource: Int,private val dataList: List<Story>) :
    ArrayAdapter<Story>(appContext,resource,dataList) {
    public var filteredDataList: List<Story> = dataList.toList()

    override fun getCount(): Int {
        return filteredDataList.size
    }

    override fun getItem(position: Int): Story? {
        return filteredDataList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(appContext)
        val view = convertView ?: inflater.inflate(resource, parent, false)

        val item = filteredDataList[position]

        // Ánh xạ các view trong custom_item_layout
        val title = view.findViewById<TextView>(R.id.txtTile_listItemLayout)
        val idStory = view.findViewById<TextView>(R.id.txtIdStory_listItemLayout)

        // Thiết lập dữ liệu
        title.text=item.title
        idStory.text = item.storyID.toString()
        view.setOnClickListener{
            appContext.toActivity(DucStoryOverviewActivity::class.java, getKeyStoryInfo(appContext),item)
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""

                val filtered = if (query.isEmpty()) {
                    dataList
                } else {
                    dataList.filter {
                        it.title.lowercase().contains(query)
                    }
                }

                val results = FilterResults()
                results.values = filtered
                results.count = filtered.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDataList = results?.values as List<Story>? ?: listOf()
                notifyDataSetChanged()
            }
        }
    }

}