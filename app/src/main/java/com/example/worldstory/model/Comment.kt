package com.example.worldstory.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId : Int?,
    val content: String,
    val time: String,
    val userId: Int,
    val storyId: Int,
    val commentReplyId: Int?=null,
  ): Parcelable
