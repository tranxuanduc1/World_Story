package com.example.worldstory.data.ducdataclass

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
    var commentReplyId:Int?=null,
    var contentReply:String?=null
) :
    Parcelable
