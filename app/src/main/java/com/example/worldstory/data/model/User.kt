package com.example.worldstory.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userID: Int?,
    val userName:String,
    var hashedPw:String,
    val email: String,
    var imgAvatar: String,
    val nickName:String,
    val roleID: Int,
    val createdDate:String,
): Parcelable
