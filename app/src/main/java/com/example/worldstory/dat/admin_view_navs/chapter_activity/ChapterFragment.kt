package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChapterBinding
import com.example.worldstory.dat.admin_adapter.ChapterAdapter
import com.example.worldstory.dat.admin_dialog.EditTitleDialog
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class ChapterFragment() : Fragment() {
    private var idStory: Int? = 0

    companion object {
        fun newInstance(idStory: Int): ChapterFragment {
            val fragment = ChapterFragment()
            val args = Bundle()
            args.putInt("idStory", idStory)
            fragment.arguments = args
            return fragment
        }
    }

    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }

    private val chapterViewModel: ChapterViewModel by viewModels {
        StoryViewModelFactory(DatabaseHelper(requireContext()))
    }
    private lateinit var chapterAdapter: ChapterAdapter

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
        idStory=arguments?.getInt("idStory")

        binding.editTitleBtn.setOnClickListener {
            EditTitleDialog().show(parentFragmentManager, "Edit")
        }
        binding.storyViewModel = storyViewModel
        binding.lifecycleOwner = this
        storyViewModel.setStoryByID(idStory)
        binding.addChapter.setOnClickListener {
            onClickAddChapter()
        }
        Picasso.get().load(storyViewModel.storyBgImg.value).into(binding.imgBground)
        Picasso.get().load(storyViewModel.storyImg.value).into(binding.avtStory)


        chapterAdapter = ChapterAdapter(storyViewModel.chapterListByStory)

        binding.chapterList.layoutManager = LinearLayoutManager(requireContext())
        binding.chapterList.adapter = chapterAdapter

        storyViewModel.currentStoryID.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "Đã nghe",
                Toast.LENGTH_LONG
            ).show()
            chapterAdapter.updateList(storyViewModel.chapterListByStory)
        }

        storyViewModel.setIDStory(idStory)

    }


    fun onClickAddChapter() {
        storyViewModel.setIDStory(idStory)
        val intent = Intent(requireContext(), AddChapterActivity::class.java)
        intent.putExtra("storyID", idStory)
        Log.w("Số tập", storyViewModel.chapterListByStory.toString())
        startActivity(intent)
    }

}