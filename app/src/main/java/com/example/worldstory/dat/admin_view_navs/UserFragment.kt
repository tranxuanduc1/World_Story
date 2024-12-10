package com.example.worldstory.dat.admin_view_navs

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentUserBinding
import com.example.worldstory.dat.admin_adapter.UserAdapter
import com.example.worldstory.dat.admin_dialog.AddUserDialogFragment
import com.example.worldstory.dat.admin_viewmodels.RecyclerViewState
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.User
import java.util.ArrayList


class UserFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var userAdapter: UserAdapter
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    private lateinit var items: List<String>

    private lateinit var binding: FragmentUserBinding
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    var itemPosition = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //khôi phục
        savedInstanceState?.let {
            searchQuery = it.getString("search_query")
            isSearchViewOpen = it.getBoolean("is_search_view_open", false)
            itemPosition = it.getInt("item_position", 1)
            // Gán lại giá trị tìm kiếm
//            if (searchQuery!=null && searchQuery!="")
//                binding.searchViewUser.setQuery(searchQuery, false)

            userAdapter.update(userViewModel.userList.value?.toList() ?: emptyList())
        }


//        binding.userViewModel = userViewModel

        //spinner
        items = roleViewModel.roles.map { it.roleName }
        ArrayAdapter(requireContext(), R.layout.user_spinner_item, items).also { adapter ->
            adapter.setDropDownViewResource(R.layout.user_spinner_item)
            binding.menu.adapter = adapter
        }


        binding.menu.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (itemPosition != position + 1) {
                        userViewModel.setUsersByRoleId(position + 1)
                        Log.w("sizeu", userViewModel.userList.value.toString())
                        itemPosition = position + 1
                        userViewModel.roleSeclected=itemPosition
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddButtonClicked ->
            if (isAddButtonClicked == true) {
                onAddButtonClicked()
                sharedViewModel.addHandled()
            }
        }




        if (isSearchViewOpen) {
            binding.searchViewUser.visibility = View.VISIBLE
        }


        binding.searchViewUser.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
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


        binding.searchViewUser.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
            }

        //recycleview
        binding.userList.layoutManager = LinearLayoutManager(requireContext())

        val color1 = ContextCompat.getColor(requireContext(), R.color.sweetheart)

        userAdapter =
            UserAdapter(
                userViewModel.userList.value?.toMutableList() ?: mutableListOf(),
                color1,
                parentFragmentManager
            )

        binding.userList.adapter = userAdapter


//        userAdapter.filterByRole(itekmPosition)


        userViewModel.userList.observe(viewLifecycleOwner) {
            Log.w("sizeup", userViewModel.userList.value.toString())
            userAdapter.update(userViewModel.userList.value?.toList() ?: emptyList())
//            userAdapter.filterByRole(itemPosition)
        }
        //swipe
        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = userAdapter.getUser(position)
                if (direction == ItemTouchHelper.LEFT) {

                    showConfirmationDialog(user, position)

                } else if (direction == ItemTouchHelper.RIGHT) {

                    showConfirmationDialog(user, position)

                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.7f
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val context = recyclerView.context


                // Lấy icon và kích thước
                val icon = ContextCompat.getDrawable(context, R.drawable.white_outline_delete_24)!!
                val iconIntrinsicHeight = icon.intrinsicHeight

                // Tính toán vị trí icon
                val iconMargin = (itemView.height - iconIntrinsicHeight) / 2

                // Thêm nền khi vuốt
                val paint = Paint()

                if (dX < 0) { // Vuốt
                    paint.color = Color.RED
                    canvas.drawRect(
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        paint
                    )
                    val iconTop = itemView.top + iconMargin
                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = iconTop + icon.intrinsicHeight
                    
                    // Vẽ icon lên Canvas
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon.draw(canvas)
                } else {
                    paint.color = Color.RED
                    canvas.drawRect(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.left + dX,
                        itemView.bottom.toFloat(),
                        paint
                    )
                    val iconTop = itemView.top + iconMargin
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = iconLeft + icon.intrinsicWidth
                    val iconBottom = iconTop + icon.intrinsicHeight

                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon.draw(canvas)

                }
                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.userList)
    }

    private fun onAddButtonClicked() {
        AddUserDialogFragment()
            .show(parentFragmentManager, "AddUserDialogFragment ")
    }

    private fun showSearchView() {
        binding.searchViewUser.visibility = View.VISIBLE
        binding.searchViewUser.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
    }

    private fun hideSearchView() {
        binding.searchViewUser.setQuery("", false)  // Xóa nội dung tìm kiếm
        binding.searchViewUser.visibility = View.GONE
        isSearchViewOpen = false
    }

    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
        val layoutManager = binding.userList.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val offset = binding.userList.getChildAt(0)?.top ?: 0

        // Cập nhật ViewModel với trạng thái cuộn
        sharedViewModel.recycleViewStateUser.value =
            RecyclerViewState(firstVisibleItemPosition, offset)

//        userAdapter.update(userViewModel.userList.value?.toList()?: emptyList())
    }


    override fun onResume() {
        super.onResume()

        sharedViewModel.recycleViewStateUser.value?.let {
            val layoutManager = binding.userList.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(it.firstVisibleItemPosition, it.offset)
        }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
//        binding.autoCompleteTextF.setAdapter(adapter)k
//        userAdapter.filterByRole(userViewModel.itemPosition)
//        userAdapter.update(userViewModel.userList.value?.toList()?: emptyList())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::binding.isInitialized) {
            outState.putString(
                "search_query",
                binding.searchViewUser.query.toString()
            ) // Lưu giá trị tìm kiếm
            outState.putBoolean(
                "is_search_view_open",
                isSearchViewOpen
            )
            outState.putString("default", items[0])
//            outState.putString("selected", binding.autoCompleteTextF.text.toString())
            outState.putStringArrayList("items", ArrayList(items))
            outState.putInt("item_position", itemPosition)
        }
    }

    private fun showConfirmationDialog(user: User, p: Int) {
        // Tạo AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Có chắc muốn xóa không?")
            .setCancelable(false)
            .setPositiveButton("Chấp nhận") { dialog, id ->
                userViewModel.delUser(user)
                dialog.dismiss()
            }
            .setNegativeButton("Không chấp nhận") { dialog, id ->
                userAdapter.notifyItemChanged(p)
                dialog.dismiss()
            }


        val dialog = builder.create()
        dialog.show()
    }

}
