package com.example.worldstory.dat.admin_view_navs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_adapter.CommentAdapter
import com.example.worldstory.dat.admin_view_navs.chapter_activity.ChapterFragment
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.model_for_test.Comment


class CommentFragment : Fragment() {

    companion object {
        fun newInstance(idStory: Int): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putInt("idStory", idStory)
            fragment.arguments = args
            return fragment
        }
    }


    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //thiết lập quan sát sự kiện nút add
        sharedViewModel._add.observe(viewLifecycleOwner) {isAddEvent->
            if(isAddEvent==true) {
                onAddButtonClicked()  // Gọi hàm xử lý khi sự kiện xảy ra
                sharedViewModel.addHandled()
            }
        }
        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo dữ liệu mẫu
        var i=1
        while(i<25){
            commentList.add(Comment("Bình luận 1", "Hứa Quang Đạt", "12/09/2024", R.drawable.baseline_search_24))
            commentList.add(Comment("Bình luận 2", "Người dùng khác", "15/09/2024", R.drawable.outline_mode_comment_24))
            i++
        }

        // Gán adapter cho RecyclerView
        commentAdapter = CommentAdapter(commentList)
        recyclerView.adapter = commentAdapter
    }

    private fun onAddButtonClicked() {
        Toast.makeText(requireContext(),"comment", Toast.LENGTH_LONG).show()    }
}