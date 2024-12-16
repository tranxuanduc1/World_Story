package com.example.worldstory.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chapter(
    val chapterID: Int?,
    val title:String,
    val dateCreated: String,
    val storyID:Int
): Parcelable
