package com.example.worldstory.admin_view_navs.chapter_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.myapplication.R
import com.example.worldstory.admin_dialog.EditTitleDialog


class ChapterFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapter, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editInfor=view.findViewById<ImageButton>(R.id.edit_title_btn)
        editInfor.setOnClickListener{
            EditTitleDialog().show(parentFragmentManager,"Edit" )
        }
    }

}