package com.example.worldstory.model

data class Story(
    val storyID: Int?,
    val title: String,
    val author: String,
    val description:String,
    val imgUrl:String,
    val bgImgUrl:String,
    val isTextStory: Int,
    val createdDate:String,
    val score: Float,
    val userID:Int
)