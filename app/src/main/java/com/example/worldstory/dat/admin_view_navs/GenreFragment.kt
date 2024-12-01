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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCategoryBinding
import com.example.worldstory.dat.admin_adapter.GenreAdapter
import com.example.worldstory.dat.admin_dialog.AddCategoryDialog
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Genre
import com.example.worldstory.model.User
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
                val genre = genreAdapter.getGenre(position)
                if (direction == ItemTouchHelper.LEFT ) {
//                    Toast.makeText(requireContext(), "Swipe left $position", Toast.LENGTH_SHORT).show()
                    showConfirmationDialog(genre,position)
                } else if (direction == ItemTouchHelper.RIGHT ) {
                    genreAdapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "Swipe right", Toast.LENGTH_SHORT).show()
//                    val editDialog = EditUserDialog()
//                    val bundle = Bundle()
//                    bundle.putInt("userID", user.userID!!)
//                    editDialog.arguments = bundle
//                    editDialog.show(requireActivity().supportFragmentManager, "EditUserDialog")
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
        itemTouchHelper.attachToRecyclerView(binding.categoryList)

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


    private fun showConfirmationDialog(genre: Genre, p:Int) {
        // Tạo AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Có chắc muốn xóa không?")
            .setCancelable(false)
            .setPositiveButton("Chấp nhận") { dialog, id ->
                genreViewModel.deleteGenre(genre.genreID!!)
                genreAdapter.remove(p)
                dialog.dismiss()
            }
            .setNegativeButton("Không chấp nhận") { dialog, id ->
                genreAdapter.notifyItemChanged(p)
                dialog.dismiss()
            }


        val dialog = builder.create()
        dialog.show()
    }
}