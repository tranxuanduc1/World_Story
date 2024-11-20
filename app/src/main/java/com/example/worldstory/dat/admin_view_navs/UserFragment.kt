package com.example.worldstory.dat.admin_view_navs

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import java.util.ArrayList


class UserFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var userAdapter: UserAdapter
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    private lateinit var items: List<String>
    private lateinit var binding: FragmentUserBinding
    private val roleViewModel: RoleViewModel by viewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val userViewModel: UserViewModel by viewModels {
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
        //recycleview
        binding.userList.layoutManager = LinearLayoutManager(requireContext())

        ////////
        val color1 = ContextCompat.getColor(requireContext(), R.color.pastel)
        userAdapter = userViewModel._users.value?.let { UserAdapter(it,color1) }?:
        UserAdapter(emptyList(), color1)
        userAdapter.filterByRole(0)
        binding.userList.adapter = userAdapter
        userViewModel._users.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),"${userViewModel._users.value!!.size}",Toast.LENGTH_LONG).show()
            userAdapter.update(userViewModel._users.value?: emptyList())

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
                if (direction == ItemTouchHelper.LEFT) {
                    userAdapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "Swipe left", Toast.LENGTH_SHORT).show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    userAdapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "Swipe right", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    val p = Paint()
                    if (dX < 0) {
                        p.color = Color.RED
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        // Vẽ biểu tượng xóa
                        val icon = ContextCompat.getDrawable(
                            recyclerView.context,
                            R.drawable.white_outline_delete_24
                        )!!
                        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + icon.intrinsicHeight
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icon.draw(c)

                    }
                    if (dX > 0) {
                        // Vẽ biểu tượng xóa
                        val icon = ContextCompat.getDrawable(
                            recyclerView.context,
                            R.drawable.outline_edit_24
                        )!!
                        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + icon.intrinsicHeight

                        val iconLeft = itemView.left + iconMargin
                        val iconRight = iconLeft + icon.intrinsicWidth
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        // Vẽ nền màu xanh khi kéo sang phải
                        val background = ColorDrawable(Color.GREEN)
                        background.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt(),
                            itemView.bottom
                        )
                        background.draw(c)

                        // Vẽ icon
                        icon.draw(c)
                    }
                } else {
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / 5,
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

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
