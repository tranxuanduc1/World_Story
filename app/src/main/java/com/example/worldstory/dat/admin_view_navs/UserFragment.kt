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
    private val currentUserID = arguments?.getInt("currentID")
    private val currentRole = arguments?.getString("currentRole")
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }

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

        binding.userViewModel = userViewModel

        //spinner
        items = roleViewModel.roles.map { it.roleName }
        val adapter = ArrayAdapter(requireContext(), R.layout.user_spinner_item, items)
        binding.autoCompleteTextF.setText(
            savedInstanceState?.getString("selected") ?: items[0],
            true
        )
        binding.autoCompleteTextF.setAdapter(adapter)

        //thiết lập onselected spinner
        binding.autoCompleteTextF.setOnItemClickListener { _, _, position, _ ->
            userAdapter.filterByRole(position)
        }
        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddButtonClicked ->
            if (isAddButtonClicked == true) {
                onAddButtonClicked()
                sharedViewModel.addHandled()
            }
        }

        //searchview
        //khôi phục
        savedInstanceState?.let {
            searchQuery = it.getString("search_query")
            isSearchViewOpen = it.getBoolean("is_search_view_open", false)

            // Gán lại giá trị tìm kiếm
            binding.searchViewUser.setQuery(searchQuery, false)

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
        userViewModel.__users.observe(viewLifecycleOwner) { new ->
            userAdapter.update(new ?: emptyList())

        }
        //recycleview
        binding.userList.layoutManager = LinearLayoutManager(requireContext())
        val color1 = ContextCompat.getColor(requireContext(), R.color.sweetheart)
        userAdapter = UserAdapter(userViewModel.__users.value ?: emptyList(), color1,parentFragmentManager)
        userAdapter.filterByRole(0)
        binding.userList.adapter = userAdapter

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
                }
                else{
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
    }

    override fun onResume() {
        super.onResume()
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        binding.autoCompleteTextF.setAdapter(adapter)
        userAdapter.filterByRole(0)

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
            outState.putString("selected", binding.autoCompleteTextF.text.toString())
            outState.putStringArrayList("items", ArrayList(items))
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
