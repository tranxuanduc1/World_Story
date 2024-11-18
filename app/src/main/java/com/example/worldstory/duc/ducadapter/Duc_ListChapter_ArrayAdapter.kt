package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass

class Duc_ListChapter_ArrayAdapter(
    var appContext: Context,var rescource: Int,var dataList: List<DucChapterDataClass>):
    ArrayAdapter<DucChapterDataClass>(appContext,rescource,dataList) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val inflater = LayoutInflater.from(appContext)
        val view = convertView ?: inflater.inflate(rescource, parent, false)
        val title = view.findViewById<TextView>(R.id.txtTitleChapter_listItemStoryOverview_layout)
        val idChapter = view.findViewById<TextView>(R.id.txtIDChapter_listItemStoryOverview_layout)
        val dateCreated = view.findViewById<TextView>(R.id.txtDateCreatedChapter_listItemStoryOverview_layout)


        title.text =dataList[position].title
        idChapter.text = dataList[position].idChapter.toString()
        dateCreated.text = dataList[position].dateCreated.toString()

        return view
    }
}