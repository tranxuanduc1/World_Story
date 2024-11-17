package com.example.worldstory.admin_view_navs

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
import com.example.worldstory.admin_adapter.OnItemClickListener
import com.example.worldstory.admin_adapter.StoryAdapter
import com.example.worldstory.admin_dialog.AddStoryDialog
import com.example.worldstory.model_for_test.Story
import com.example.worldstory.admin_sheet.MyBottomSheetFragment
import com.example.worldstory.admin_view_navs.chapter_activity.ChapterActivity
import com.example.worldstory.adim_viewmodels.RecyclerViewState
import com.example.worldstory.adim_viewmodels.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StoryFragment : Fragment() ,OnItemClickListener{
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private val storyList = mutableListOf<Story>()
    private var searchView: androidx.appcompat.widget.SearchView? = null
    private var isSearchViewOpen = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStoryBinding.inflate(inflater, container, false)
        searchView = binding.searchViewStory  // Đảm bảo khởi tạo ở đây
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
        recyclerView = view.findViewById(R.id.story_list)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //thêm item
        val color1 = ContextCompat.getColor(requireContext(), R.color.pastel)
        storyAdapter = StoryAdapter(storyList, color1,this)
        recyclerView.adapter = storyAdapter
        CoroutineScope(Dispatchers.Main).launch {
            val data = withContext(Dispatchers.IO) {
                val tempList = mutableListOf<Story>()
                for (i in 1..1000) {
                    tempList.add(
                        Story(
                            "T1",
                            "Ngao, Sò, Ốc, Hến",
                            "Unknow",
                            listOf("Hài", "Kịch Tính")
                        )
                    )
                    tempList.add(
                        Story(
                            "T2",
                            "Conan",
                            "Unknow",
                            listOf("Hài", "Kịch Tính", "Trinh Thám")
                        )
                    )
                }
                tempList
            }
            // Cập nhật danh sách sau khi load xong
            storyList.addAll(data)

            if (sharedViewModel._selectedChips.isNotEmpty()) {
                sharedViewModel._selectedChips.forEach { chip -> storyAdapter.filter.filter(chip) }
            } else
                storyAdapter.notifyDataSetChanged()
        }


        //searchview
        searchView = view.findViewById(R.id.search_view_story)
        searchView?.setOnQueryTextListener(object :
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
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
                sharedViewModel
            }

        sharedViewModel.searchQueryStory.observe(viewLifecycleOwner) { query ->
            if (sharedViewModel.searchQueryStory.value != "") {
                searchView?.visibility = View.VISIBLE
                searchView?.isIconified = false
                searchView?.setQuery(query, false)
            }
        }
        sharedViewModel.recyclerViewStateStory.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
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
            if (isClicked == true && sharedViewModel._selectedChips.isNotEmpty()) {
                sharedViewModel._selectedChips.forEach { chip -> storyAdapter.filter.filter(chip) }
                sharedViewModel.filterBtnHandled()
            } else if (isClicked == true) {
                storyAdapter.filter.filter("")
                sharedViewModel.filterBtnHandled()
            } else if (sharedViewModel._selectedChips.isNotEmpty()) {
                sharedViewModel._selectedChips.forEach { chip -> storyAdapter.filter.filter(chip) }
            }
        }

    }

    private fun onAddButtonClicked() {
        AddStoryDialog().show(parentFragmentManager, "AddStoryDialogFragment")
    }

    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val offset = recyclerView.getChildAt(0)?.top ?: 0

        // Cập nhật ViewModel với trạng thái cuộn
        sharedViewModel.recyclerViewStateStory.value =
            RecyclerViewState(firstVisibleItemPosition, offset)
    }

    override fun onItemClick(item: Story) {
        val intent=Intent(requireContext(),ChapterActivity::class.java)
        startActivity(intent)
    }
    private fun showSearchView() {
        searchView?.visibility = View.VISIBLE
        searchView?.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
        searchView?.requestFocus()
    }

    private fun hideSearchView() {
        searchView?.setQuery("", false)  // Xóa nội dung tìm kiếm
        searchView?.clearFocus()
        searchView?.visibility = View.GONE
        sharedViewModel.searchQueryStory.value = ""
    }


}