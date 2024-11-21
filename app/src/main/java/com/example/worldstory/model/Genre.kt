package com.example.worldstory.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val genreID: Int?,
    val genreName:String,
    val userID:Int
): Parcelable
