package com.example.worldstory.dat.admin_view_navs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStoryBinding
import com.example.worldstory.dat.admin_adapter.OnItemClickListener
import com.example.worldstory.dat.admin_adapter.StoryAdapter
import com.example.worldstory.dat.admin_dialog.AddStoryDialog
import com.example.worldstory.dat.admin_sheet.MyBottomSheetFragment
import com.example.worldstory.dat.admin_view_navs.chapter_activity.ChapterActivity
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.RecyclerViewState
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Story
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StoryFragment : Fragment(), OnItemClickListener {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var storyAdapter: StoryAdapter
    private val storyList = mutableListOf<Story>()
    private var isSearchViewOpen = false
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val storyViewModel:StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: FragmentStoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddEvent ->
            if (isAddEvent == true) {
                onAddButtonClicked()  // Gọi hàm xử lý khi sự kiện xảy ra
                sharedViewModel.addHandled()
            }
        }

        //tạo list
        binding.storyList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //thêm item
        val color1 = ContextCompat.getColor(requireContext(), R.color.pastel)
        storyAdapter = StoryAdapter(storyViewModel.stories.value?: emptyList(), color1, this)
        binding.storyList.adapter = storyAdapter
        storyViewModel.stories.observe(viewLifecycleOwner){
            storyAdapter.updateList(storyViewModel.stories.value?: emptyList())
        }

        //////////////////////////////////////////

//        CoroutineScope(Dispatchers.Main).launch {
//            val data = withContext(Dispatchers.IO) {
//                val tempList = mutableListOf<Story>()
//                for (i in 1..1000) {
//                    tempList.add(
//                        Story(
//                            "T1",
//                            "Ngao, Sò, Ốc, Hến",
//                            "Unknow",
//                            listOf("Hài", "Kịch Tính")
//                        )
//                    )
//                    tempList.add(
//                        Story(
//                            "T2",
//                            "Conan",
//                            "Unknow",
//                            listOf("Hài", "Trinh Thám")
//                        )
//                    )
//                }
//                tempList
//            }
//            // Cập nhật danh sách sau khi load xong
//            storyList.addAll(data)
//
//            if (sharedViewModel._selectedChips.isNotEmpty()) {
//                sharedViewModel._selectedChips.forEach { chip -> storyAdapter.filter.filter(chip.toString()) }
//            } else
//                storyAdapter.notifyDataSetChanged()
//        }


        //searchview
        binding.searchViewStory.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                storyAdapter.updateSearchQuery(p0)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                storyAdapter.updateSearchQuery(p0)
                sharedViewModel.searchQueryStory.value = p0
                return false
            }
        })
        sharedViewModel._search.observe(viewLifecycleOwner) { isSearchClicked ->
            if (isSearchClicked == true) {
                showSearchView()
                sharedViewModel.searchHandle()
            }
        }
        binding.searchViewStory.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
                sharedViewModel
            }

        sharedViewModel.searchQueryStory.observe(viewLifecycleOwner) { query ->
            if (sharedViewModel.searchQueryStory.value != "") {
                binding.searchViewStory.visibility = View.VISIBLE
                binding.searchViewStory.isIconified = false
                binding.searchViewStory.setQuery(query, false)
            }
        }
        sharedViewModel.recyclerViewStateStory.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(it.firstVisibleItemPosition, it.offset)
            }
        })

        //Bottom sheet
        val showBottomSheetButton =
            view.findViewById<FloatingActionButton>(R.id.floating_action_button)

        showBottomSheetButton.setOnClickListener {
            val bottomSheet = MyBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "MyBottomSheet")
        }

        ////Filter Buttun

        sharedViewModel._filterBtn.observe(viewLifecycleOwner) { isClicked ->

            storyAdapter.filterByCates(sharedViewModel._selectedChips.toList())


        }

    }

    private fun onAddButtonClicked() {
        AddStoryDialog().show(parentFragmentManager, "AddStoryDialogFragment")
    }

    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
        val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val offset = binding.storyList.getChildAt(0)?.top ?: 0

        // Cập nhật ViewModel với trạng thái cuộn
        sharedViewModel.recyclerViewStateStory.value =
            RecyclerViewState(firstVisibleItemPosition, offset)
    }

    override fun onItemClick(item: Story) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        startActivity(intent)
    }

    private fun showSearchView() {
        binding.searchViewStory.visibility = View.VISIBLE
        binding.searchViewStory.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
        binding.searchViewStory.requestFocus()
    }

    private fun hideSearchView() {
        binding.searchViewStory.setQuery("", false)  // Xóa nội dung tìm kiếm
        binding.storyList.clearFocus()
        binding.searchViewStory.visibility = View.GONE
        sharedViewModel.searchQueryStory.value = ""
    }


}