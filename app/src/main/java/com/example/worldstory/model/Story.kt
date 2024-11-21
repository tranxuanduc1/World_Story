package com.example.worldstory.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
): Parcelable