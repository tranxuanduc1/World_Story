package com.example.worldstory.model

data class Comment(
    val commentId : Int?,
    val content: String,
    val time: String,
    val userId: Int,
    val storyId: Int
  )
