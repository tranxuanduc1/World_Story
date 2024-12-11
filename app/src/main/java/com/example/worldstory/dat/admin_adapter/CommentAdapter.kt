package com.example.worldstory.dat.admin_adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewholder.CommentViewHolder
import com.example.worldstory.dat.admin_viewmodels.CommentViewModel
import com.example.worldstory.model.Comment
import com.example.worldstory.model.User


class CommentAdapter(
    private val commentMap: MutableMap<Comment, User>,
    private val commentList: MutableList<Comment>,
    private val context: Context,
    private val commentViewModel: CommentViewModel
) :
    RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item_row, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        val user = commentMap[comment]
        holder.id.text = user?.userID.toString()
        holder.nickname.text = user?.nickName
        holder.username.text = user?.userName
        holder.content.text = comment.content
        holder.date.text = comment.time
        holder.itemView.setOnLongClickListener {
            showPopupMewnu(holder.itemView, comment)
            true
        }
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.Light_Beige))
        } else
            holder.itemView.setBackgroundColor(android.graphics.Color.WHITE)
//        // Xử lý sự kiện cho các button
//        holder.rplBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Reply
//        }
//
//        holder.hideBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Hide
//        }
//
//        holder.deleteBtn.setOnClickListener {
//            // Code xử lý khi nhấn nút Delete
//        }
    }

    override fun getItemCount(): Int = commentList.size

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupMewnu(view: View, comment: Comment) {
        val popupMenu = PopupMenu(view.context, view)

        popupMenu.menuInflater.inflate(R.menu.stats_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.del_item -> {
                    val dialog = AlertDialog.Builder(view.context)
                    dialog.setMessage("Có chắc muốn xóa ?")
                        .setPositiveButton("Đồng ý") { dialog, _ ->
                            commentViewModel.delComment(commentId = comment.commentId ?: -1)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Hủy") { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
            }
            true
        }
        popupMenu.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newCommentMap: Map<Comment, User>, newCommentList: List<Comment>) {
        commentMap.clear()
        commentList.clear()
        newCommentMap.forEach { (k, v) -> commentMap[k] = v }
        commentList.addAll(newCommentList)
        notifyDataSetChanged()
    }

}

