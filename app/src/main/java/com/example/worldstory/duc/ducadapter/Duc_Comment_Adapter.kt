package com.example.worldstory.duc.ducadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CommentContainerLayoutBinding
import com.example.myapplication.databinding.CommentOppositeLayoutBinding
import com.example.myapplication.databinding.CommentSelfLayoutBinding
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducutils.loadImgURL
import android.content.Context
import android.graphics.Canvas
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.myapplication.databinding.ActivityChapterBinding
import com.example.myapplication.databinding.ActivityDucChapterBinding
import com.example.worldstory.duc.ducutils.callLog


class Duc_Comment_Adapter(
    var context: Context,
    var dataList: List<DucCommentDataClass>,
    var bindingCommentReplyInInputKeyboard: ActivityDucChapterBinding,
    var userSessionId: Int,
    var checkReply: MutableList<Any>
) : RecyclerView.Adapter<Duc_Comment_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var binnding = CommentContainerLayoutBinding.inflate(inflater)

        return ViewHolder(binnding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        var comment = dataList[position]
        // gan du lieu comment vao view

        //comment cua nguoi dung hien tai
        if (comment.userId == userSessionId) {
            callLog("Adapteraa",comment.toString())

           setViewCommentSelf(holder,comment)
        } else {
            //comment cua nguoi dung khac

            setViewCommentOposite(holder,comment)
        }

    }




    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(var binding: CommentContainerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
    private fun setViewCommentOposite(holder: ViewHolder,comment: DucCommentDataClass) {

        holder.binding.linearCommentOppositeConmmentContainer.visibility = View.VISIBLE
        holder.binding.linearCommentSelfConmmentContainer.visibility = View.GONE
        holder.binding.linearReplyOppositeConmmentContainer.visibility = View.GONE

        //du lieu comment phu
        if(comment.commentReplyId!=null){
            holder.binding.linearReplyOppositeConmmentContainer.visibility = View.VISIBLE
            holder.binding.txtReplyOppositeCommentContainer.text=comment.contentReply
        }else{
            holder.binding.linearReplySelfConmmentContainer.visibility = View.GONE
            var param = holder.binding.linearContentCommentOppositeCommentContainer.layoutParams
            if (param is ViewGroup.MarginLayoutParams) {
                param.topMargin = 0
            }
            holder.binding.linearContentCommentOppositeCommentContainer.layoutParams=param

        }

        //du lieu cho comment chinh

        holder.binding.txtDisplayNameCommentOppositeLayoutConmmentContainer.text =
            comment.nameUser
        holder.binding.txtContentCommentOppositeLayoutConmmentContainer.text = comment.content
        holder.binding.imgAvatarUserCommentOppositeLayoutConmmentContainer.loadImgURL(
            context,
            comment.imgAvatarUrl
        )
        holder.binding.txtDateCreatedCommentOppositeLayoutConmmentContainer.text = comment.date
    }

    private fun setViewCommentSelf(
        holder: ViewHolder,
        comment: DucCommentDataClass
    ) {
        callLog("Adapteraa",comment.commentReplyId.toString())

        holder.binding.linearCommentOppositeConmmentContainer.visibility = View.GONE
        holder.binding.linearCommentSelfConmmentContainer.visibility = View.VISIBLE


        //du lieu comment phu
        if(comment.commentReplyId!=null){
            callLog("Adapteraa",comment.contentReply.toString())

            holder.binding.linearReplySelfConmmentContainer.visibility = View.VISIBLE
            holder.binding.txtReplySelfCommentContainer.text=comment.contentReply
        }else{
            holder.binding.linearReplySelfConmmentContainer.visibility = View.GONE
            var param = holder.binding.linearContentCommentSelfCommentContainer.layoutParams
            if (param is ViewGroup.MarginLayoutParams) {
                param.topMargin = 0
            }
            holder.binding.linearContentCommentSelfCommentContainer.layoutParams=param
            
        }
        //du lieu cho comment chinh
        holder.binding.txtDisplayNameCommentSelfLayoutConmmentContainer.text =
            comment.nameUser
        holder.binding.txtContentCommentSelfLayoutConmmentContainer.text = comment.content
        holder.binding.imgAvatarUserCommentSelfLayoutConmmentContainer.loadImgURL(
            context,
            comment.imgAvatarUrl
        )
        holder.binding.txtDateCreatedCommentSelfLayoutConmmentContainer.text = comment.date
    }

    fun getCommentSimpleCallBack(): ItemTouchHelper.SimpleCallback{
        return CommentSimpleCallBack()
    }
    inner class CommentSimpleCallBack(): ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.END or ItemTouchHelper.START
    ){
        private val maxSwipeDistance = 200f // Giới hạn khoảng cách kéo
        private val activationThreshold = 150f // Ngưỡng kích hoạt sự kiện
        private var oldDX=0
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            callLog("commentadapter","move")
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            triggerAction(viewHolder.adapterPosition)

        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.1f
        }



        private fun triggerAction(position: Int) {

            // Kích hoạt sự kiện khi kéo đến ngưỡng
            bindingCommentReplyInInputKeyboard.frameContainerCommentReplyInInputKeyboardChapter.visibility = View.VISIBLE
            //hien thi comment reply
            bindingCommentReplyInInputKeyboard.txtCommentReplyInInputKeyboardChapter.text = dataList[position].content
            //flag co reply
            checkReply[0]=true
            checkReply[1]=dataList[position].commentId
            notifyItemChanged(position) // Reset lại vị trí sau khi kích hoạt
        }
    }
}