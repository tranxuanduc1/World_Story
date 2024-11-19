package com.example.worldstory.model

data class Story(
    val title: String,
    val tacGia: String,
    val img:String,
    val imgbg:String,
    val isTextStory: Int,
    val description:String,
    val createdDate:String,
    val score:String,
    val genreID:Int,
    val userID:Int
)