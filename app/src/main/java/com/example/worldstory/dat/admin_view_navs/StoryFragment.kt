package com.example.worldstory.dat.admin_view_navs

import android.app.AlertDialog
import android.content.Intent
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStoryBinding
import com.example.worldstory.dat.admin_adapter.OnItemClickListener
import com.example.worldstory.dat.admin_adapter.StoryAdapter
import com.example.worldstory.dat.admin_dialog.AddStoryDialog
import com.example.worldstory.dat.admin_sheet.MyBottomSheetFragment
import com.example.worldstory.dat.admin_view_navs.chapter_activity.ChapterActivity
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.RecyclerViewState
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Story
import com.example.worldstory.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StoryFragment : Fragment(), OnItemClickListener {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var storyAdapter: StoryAdapter
    private val storyList = mutableListOf<Story>()
    private var isSearchViewOpen = false
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: FragmentStoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
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
        binding.storyList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //thêm item
        val color1 = ContextCompat.getColor(requireContext(), R.color.sweetheart)
        storyAdapter = StoryAdapter(storyViewModel.stories.value ?: emptyList(), color1, this)
        binding.storyList.adapter = storyAdapter
        storyViewModel.stories.observe(viewLifecycleOwner) {
            storyAdapter.updateList(storyViewModel.stories.value ?: emptyList())
        }

        //////////////////////////////////////////

//        CoroutineScope(Dispatchers.Main).launch {
//            val data = withContext(Dispatchers.IO) {
//                val tempList = mutableListOf<Story>()
//                for (i in 1..1000) {
//                    tempList.add(
//                        Story(
//                            "T1",
//                            "Ngao, Sò, Ốc, Hến",
//                            "Unknow",
//                            listOf("Hài", "Kịch Tính")
//                        )
//                    )
//                    tempList.add(
//                        Story(
//                            "T2",
//                            "Conan",
//                            "Unknow",
//                            listOf("Hài", "Trinh Thám")
//                        )
//                    )
//                }
//                tempList
//            }
//            // Cập nhật danh sách sau khi load xong
//            storyList.addAll(data)
//
//            if (sharedViewModel._selectedChips.isNotEmpty()) {
//                sharedViewModel._selectedChips.forEach { chip -> storyAdapter.filter.filter(chip.toString()) }
//            } else
//                storyAdapter.notifyDataSetChanged()
//        }


        //searchview
        binding.searchViewStory.setOnQueryTextListener(object :
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
        binding.searchViewStory.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
            ?.setOnClickListener() {
                hideSearchView()
                sharedViewModel
            }

        sharedViewModel.searchQueryStory.observe(viewLifecycleOwner) { query ->
            if (sharedViewModel.searchQueryStory.value != "") {
                binding.searchViewStory.visibility = View.VISIBLE
                binding.searchViewStory.isIconified = false
                binding.searchViewStory.setQuery(query, false)
            }
        }
        sharedViewModel.recyclerViewStateStory.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
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

            storyAdapter.filterByCates(sharedViewModel._selectedChips, storyViewModel)


        }



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
                val story = storyAdapter.getStory(position)
                if (direction == ItemTouchHelper.LEFT ) {
//                    Toast.makeText(requireContext(), "Swipe left $position", Toast.LENGTH_SHORT).show()
                    showConfirmationDialog(story,position)
                } else if (direction == ItemTouchHelper.RIGHT ) {
                    storyAdapter.notifyItemChanged(position)
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
        itemTouchHelper.attachToRecyclerView(binding.storyList)

    }

    private fun onAddButtonClicked() {
        AddStoryDialog().show(parentFragmentManager, "AddStoryDialogFragment")
    }

    override fun onPause() {
        super.onPause()

        // Lưu trạng thái của RecyclerView
        val layoutManager = binding.storyList.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val offset = binding.storyList.getChildAt(0)?.top ?: 0

        // Cập nhật ViewModel với trạng thái cuộn
        sharedViewModel.recyclerViewStateStory.value =
            RecyclerViewState(firstVisibleItemPosition, offset)
    }

    override fun onItemClick(item: Story) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        intent.putExtra("ID",item.storyID)

        startActivity(intent)
    }

    private fun showSearchView() {
        binding.searchViewStory.visibility = View.VISIBLE
        binding.searchViewStory.isIconified = false    // Mở rộng SearchView
        isSearchViewOpen = true
        binding.searchViewStory.requestFocus()
    }

    private fun hideSearchView() {
        binding.searchViewStory.setQuery("", false)  // Xóa nội dung tìm kiếm
        binding.storyList.clearFocus()
        binding.searchViewStory.visibility = View.GONE
        sharedViewModel.searchQueryStory.value = ""
    }

    private fun showConfirmationDialog(story: Story, p:Int) {
        // Tạo AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Có chắc muốn xóa không?")
            .setCancelable(false)
            .setPositiveButton("Chấp nhận") { dialog, id ->
                storyViewModel.deleteStory(story)
                storyAdapter.delete(p)
                dialog.dismiss()
            }
            .setNegativeButton("Không chấp nhận") { dialog, id ->
                storyAdapter.notifyItemChanged(p)
                dialog.dismiss()
            }


        val dialog = builder.create()
        dialog.show()
    }
}