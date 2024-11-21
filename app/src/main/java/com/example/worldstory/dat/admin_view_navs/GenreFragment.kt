package com.example.worldstory.dat.admin_view_navs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCategoryBinding
import com.example.worldstory.dat.admin_adapter.GenreAdapter
import com.example.worldstory.dat.admin_dialog.AddCategoryDialog
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model_for_test.Category


class GenreFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var genreAdapter: GenreAdapter
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    private lateinit var binding: FragmentCategoryBinding
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
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
        //searchview
        //khôi phục
        savedInstanceState?.let {
            searchQuery = it.getString("search_query_c")
            isSearchViewOpen = it.getBoolean("is_search_view_open_c", false)

            // Gán lại giá trị tìm kiếm
            binding.searchViewCate.setQuery(searchQuery, false)

        }
        if (isSearchViewOpen) {
            binding.searchViewCate.visibility = View.VISIBLE
        }
        binding.searchViewCate.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                genreAdapter.filter.filter(p0)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                genreAdapter.filter.filter(p0)
                return false
            }
        })
        sharedViewModel._search.observe(viewLifecycleOwner) { isSearchClicked ->
            if (isSearchClicked == true) {
                showSearchView()
                sharedViewModel.searchHandle()
            }
        }
        binding.searchViewCate.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
            }
//
//        /recycleview///
//

        binding.categoryList.layoutManager = LinearLayoutManager(requireContext())
        val color1 = ContextCompat.getColor(requireContext(), R.color.sweetheart)
        genreAdapter = GenreAdapter(genreViewModel.genres.value ?: emptyList(), color1)
        binding.categoryList.adapter = genreAdapter
        genreViewModel.genres.observe(viewLifecycleOwner) {
            genreAdapter.updateList(genreViewModel.genres.value ?: emptyList())
        }
        Log.i("size","${genreViewModel.genres.value?.size}")
    }


    private fun onAddButtonClicked() {
        AddCategoryDialog().show(parentFragmentManager, "AddCategoryDialogFragment")
    }

    private fun showSearchView() {
        binding.searchViewCate.visibility = View.VISIBLE
        binding.searchViewCate.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
    }

    private fun hideSearchView() {
        binding.searchViewCate.setQuery("", false)  // Xóa nội dung tìm kiếm
        binding.searchViewCate.visibility = View.GONE
        isSearchViewOpen = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            "search_query_c",
            binding.searchViewCate.query.toString()
        ) // Lưu giá trị tìm kiếm
        outState.putBoolean(
            "is_search_view_open_c",
            isSearchViewOpen
        )    // Lưu trạng thái mở/đóng của SearchView
    }
}