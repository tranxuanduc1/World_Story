package com.example.worldstory.view.admin_view_navs

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.worldstory.view.admin_dialog.AddCategoryDialog
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModel
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.view_models.admin_viewmodels.SharedViewModel
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.isUserCurrentAdmin
import com.example.worldstory.data.model.Genre


class GenreFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var genreAdapter: GenreAdapter
    private var searchQuery: String? = null
    private var isSearchViewOpen = false
    private lateinit var binding: FragmentCategoryBinding
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }

    private val DEBOUNCE_DELAY = 500L
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

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
                runnable?.let { handler.removeCallbacks(it) }

                runnable = Runnable {
                    p0?.let {
                        genreAdapter.filter.filter(it)
                    }
                }

                handler.postDelayed(runnable!!, DEBOUNCE_DELAY)
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
        genreAdapter = GenreAdapter(
            genreViewModel,
            genreViewModel.genres.value ?: emptyList(),
            color1,
            requireContext()
        )
        binding.categoryList.adapter = genreAdapter
        genreViewModel.genres.observe(viewLifecycleOwner) {
            genreAdapter.updateList(genreViewModel.genres.value ?: emptyList())
        }
        Log.i("size", "${genreViewModel.genres.value?.size}")


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
                val genre = genreAdapter.getGenre(position)
                if (direction == ItemTouchHelper.LEFT) {
                    if (requireContext().isUserCurrentAdmin())
                        showConfirmationDialog(genre, position)
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Bạn không có quyền xóa đối tượng này",
                            Toast.LENGTH_SHORT

                        ).show()
                        genreAdapter.notifyItemChanged(position)
                    }
                } else if (direction == ItemTouchHelper.RIGHT) {

                    if (requireContext().isUserCurrentAdmin())
                        showConfirmationDialog(genre, position)
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Bạn không có quyền xóa đối tượng này",
                            Toast.LENGTH_SHORT

                        ).show()
                        genreAdapter.notifyItemChanged(position)
                    }

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
        itemTouchHelper.attachToRecyclerView(binding.categoryList)


        //sort
//
//        binding.sortIdGenre.setOnClickListener {
//            if (it.visibility == View.VISIBLE) {
//                binding.desSortIdGenre.visibility = View.VISIBLE
//                it.visibility = View.GONE
//                genreViewModel.des_sort()
//            }
//        }
//
//        binding.desSortIdGenre.setOnClickListener {
//            if (it.visibility == View.VISIBLE) {
//                binding.sortIdGenre.visibility = View.VISIBLE
//                it.visibility = View.GONE
//                genreViewModel.sort()
//            }
//        }
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


    private fun showConfirmationDialog(genre: Genre, p: Int) {
        // Tạo AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Có chắc muốn xóa không?")
            .setCancelable(false)
            .setPositiveButton("Chấp nhận") { dialog, id ->
                genreViewModel.deleteGenre(genre.genreID!!)
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