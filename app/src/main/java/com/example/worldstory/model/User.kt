package com.example.worldstory.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userID: Int?,
    val userName:String,
    val hashedPw:String,
    val email: String,
    val imgAvatar: String,
    val nickName:String,
    val roleID: Int,
    val createdDate:String,
): Parcelable
