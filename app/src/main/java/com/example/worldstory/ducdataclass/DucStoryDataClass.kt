package com.example.worldstory.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucStoryDataClass(var idStory:Int, var title: String, var author : String,
                             var description: String,
                             var imgURL : Int, var backgroundImageURL:Int,
                             var dateCreate:String,
                             var score : Float, var isComic: Boolean): Parcelable
