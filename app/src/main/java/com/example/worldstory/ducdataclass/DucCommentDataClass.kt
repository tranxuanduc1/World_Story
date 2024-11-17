package com.example.worldstory.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucCommentDataClass(var idComment:Int, var idStory:Int, var idUser:Int, var content:String, var date:String):
    Parcelable
