package com.example.worldstory.model

data class Story(
    val storyID: Int?,
    val title: String,
    val author: String,
    val imgUrl:String,
    val bgImgUrl:String,
    val isTextStory: Int,
    val description:String,
    val createdDate:String,
    val score:String,
    val genreID:Int,
    val userID:Int
)