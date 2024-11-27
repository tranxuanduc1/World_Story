package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChapterBinding
import com.example.worldstory.dat.admin_dialog.EditTitleDialog
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.squareup.picasso.Picasso


class ChapterFragment(private val idStory: Int) : Fragment() {
    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: FragmentChapterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTitleBtn.setOnClickListener {
            EditTitleDialog().show(parentFragmentManager, "Edit")
        }
        binding.storyViewModel = storyViewModel
        binding.lifecycleOwner = this
        storyViewModel.setStoryByID(idStory)
        binding.addChapter.setOnClickListener {
            onClickAddChapter()
        }
        Picasso.get().load(storyViewModel.storyImg.value).into(binding.imgBground)
    }

    fun onClickAddChapter() {
        val intent = Intent(requireContext(), AddChapterActivity::class.java)
        startActivity(intent)
    }

}