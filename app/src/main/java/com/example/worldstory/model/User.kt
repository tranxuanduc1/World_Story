package com.example.worldstory.model

data class User(
    val userID: Int?,
    val userName:String,
    val hashedPw:String,
    val nickName:String,
    val roleID: Int,
    val createdDate:String,
)
