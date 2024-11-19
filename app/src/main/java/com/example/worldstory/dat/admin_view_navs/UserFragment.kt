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
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentUserBinding
import com.example.worldstory.dat.admin_adapter.UserAdapter
import com.example.worldstory.dat.admin_dialog.AddUserDialogFragment
import com.example.worldstory.dat.admin_viewmodels.RecyclerViewState
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.model_for_test.Role
import com.example.worldstory.model_for_test.User
import java.util.ArrayList


class UserFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private val userList = mutableListOf<User>()
    private var searchView: SearchView? = null
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    private val items = listOf("Admin", "User", "Manager", "Guest")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater, container, false)
        searchView = binding.searchViewUser
        recyclerView = binding.userList
        autoCompleteTextView = binding.autoCompleteTextF
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //spinner
        val items =  savedInstanceState?.getStringArrayList("items") ?: listOf("ADMIN", "USER", "POOR", "BOSS")
        val adapter = ArrayAdapter(requireContext(), R.layout.user_spinner_item, items)
        autoCompleteTextView.setText(savedInstanceState?.getString("selected")?:items[0],true)
        autoCompleteTextView.setAdapter(adapter)

        //thiết lập onselected spinner
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            userAdapter.filterByRole(selectedItem)
        }
        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) { isAddButtonClicked ->
            if (isAddButtonClicked == true) {
                onAddButtonClicked()
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
            searchView?.visibility = View.VISIBLE
        }
        searchView?.setOnQueryTextListener(object :
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
        searchView?.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
            }
        //tạo list
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //thêm item
        var i = 1
        while (i <= 100) {
            userList.add(
                User(
                    "USER1",
                    "Hứa Quang Đạt",
                    Role.USER
                )
            )
            userList.add(
                User(
                    "USER2",
                    "Trần Xuân Đức",
                    Role.ADMIN
                )
            )
            i++
        }
        val color1 = ContextCompat.getColor(requireContext(), R.color.pastel)
        userAdapter = UserAdapter(userList, color1)
        userAdapter.filterByRole(autoCompleteTextView.text.toString())
        recyclerView.adapter = userAdapter
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
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun onAddButtonClicked() {
        AddUserDialogFragment()
            .show(parentFragmentManager, "AddUserDialogFragment ")
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
    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val offset = recyclerView.getChildAt(0)?.top ?: 0

                // Cập nhật ViewModel với trạng thái cuộn
                sharedViewModel.recycleViewStateUser.value =
                    RecyclerViewState(firstVisibleItemPosition, offset)
    }
    override fun onResume() {
        super.onResume()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextView.setAdapter(adapter)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("search_query", searchView?.query.toString()) // Lưu giá trị tìm kiếm
        outState.putBoolean(
            "is_search_view_open",
            isSearchViewOpen
        )
        outState.putString("default",items[0])
        outState.putString("selected",autoCompleteTextView.text.toString())
        outState.putStringArrayList("items", ArrayList(items))
    }
}
