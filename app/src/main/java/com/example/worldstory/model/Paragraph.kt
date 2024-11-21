package com.example.worldstory.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Paragraph(
    val paragraphID: Int?,
    val content:String,
    val order:Int,
    val chapterID:Int
): Parcelable
