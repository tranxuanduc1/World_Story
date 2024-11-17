package com.example.worldstory.admin_view_navs

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
import com.example.myapplication.databinding.FragmentUserBinding
import com.example.worldstory.admin_dialog.AddUserDialogFragment
import com.example.worldstory.admin_adapter.UserAdapter
import com.example.worldstory.model_for_test.Role
import com.example.worldstory.model_for_test.User
import com.example.worldstory.adim_viewmodels.SharedViewModel


class UserFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<User>()
    private  var searchView: androidx.appcompat.widget.SearchView?=null
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater, container, false)
        searchView = binding.searchViewUser  // Đảm bảo khởi tạo ở đây
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddButtonClicked ->
            if (isAddButtonClicked == true) {
                onAddButtonClicked()  // Gọi hàm xử lý khi sự kiện xảy ra
                sharedViewModel.addHandled()
            }
        }
        //searchview
        searchView = view.findViewById(R.id.search_view_user)
        //khôi phục
        savedInstanceState?.let {
            searchQuery = it.getString("search_query")
            isSearchViewOpen = it.getBoolean("is_search_view_open", false)

            // Gán lại giá trị tìm kiếm
            searchView?.setQuery(searchQuery, false)

        }
        if (isSearchViewOpen) {
            searchView?.visibility=View.VISIBLE
        }
        searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                userAdapter.filter.filter(p0)
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                userAdapter.filter.filter(p0)
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
        //tạo list
        recyclerView = view.findViewById(R.id.user_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //thêm item
        var i = 1
        while (i <= 100) {
            userList.add(User("USER1", "Hứa Quang Đạt", Role.USER))
            userList.add(User("USER2", "Trần Xuân Đức", Role.ADMIN))
            i++
        }
        val color1= ContextCompat.getColor(requireContext(),R.color.pastel)
        userAdapter = UserAdapter(userList,color1)
        recyclerView.adapter = userAdapter
    }

    private fun onAddButtonClicked() {
        AddUserDialogFragment().show(parentFragmentManager, "AddUserDialogFragment ")
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
        outState.putString("search_query", searchView?.query.toString()) // Lưu giá trị tìm kiếm
        outState.putBoolean(
            "is_search_view_open",
            isSearchViewOpen
        )    // Lưu trạng thái mở/đóng của SearchView
    }
}