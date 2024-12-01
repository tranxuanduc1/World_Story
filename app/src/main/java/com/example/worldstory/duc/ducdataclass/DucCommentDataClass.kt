package com.example.worldstory.duc.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucCommentDataClass(
    var commentId: Int,
    var content: String,
    var imgAvatarUrl: String,
    var nameUser:String,
    var date: String,
    var storyId: Int,
    var userId: Int,
) :
    Parcelable
