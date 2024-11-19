package com.example.worldstory.dat.admin_view_navs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_adapter.CategoryAdapter
import com.example.worldstory.dat.admin_dialog.AddCategoryDialog
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.model_for_test.Category


class CategoryFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()
    private var searchView: androidx.appcompat.widget.SearchView? = null
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
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
        searchView = view.findViewById(R.id.search_view_cate)
        //khôi phục
        savedInstanceState?.let {
            searchQuery = it.getString("search_query_c")
            isSearchViewOpen = it.getBoolean("is_search_view_open_c", false)

            // Gán lại giá trị tìm kiếm
            searchView?.setQuery(searchQuery, false)

        }
        if (isSearchViewOpen) {
            searchView?.visibility = View.VISIBLE
        }
        searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                categoryAdapter.filter.filter(p0)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                categoryAdapter.filter.filter(p0)
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
            }

        recyclerView = view.findViewById(R.id.category_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        var i = 1
        while (i < 50) {
            categoryList.add(Category("G1", "Tình cảm", "23"))
            categoryList.add(Category("G2", "Kinh dị", "9"))
            i++
        }
        val color1= ContextCompat.getColor(requireContext(),R.color.pastel)
        categoryAdapter = CategoryAdapter(categoryList,color1)
        recyclerView.adapter = categoryAdapter
    }


    private fun onAddButtonClicked() {
        AddCategoryDialog().show(parentFragmentManager, "AddCategoryDialogFragment")
    }

    private fun showSearchView() {
        searchView?.visibility = View.VISIBLE
        searchView?.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
    }

    private fun hideSearchView() {
        searchView?.setQuery("", false)  // Xóa nội dung tìm kiếm
        searchView?.visibility = View.GONE
        isSearchViewOpen = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search_query_c", searchView?.query.toString()) // Lưu giá trị tìm kiếm
        outState.putBoolean(
            "is_search_view_open_c",
            isSearchViewOpen
        )    // Lưu trạng thái mở/đóng của SearchView
    }
}