package com.example.worldstory.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucChapterDataClass(var idChapter: Int, var idStory:Int, var title: String,
                               var dateCreated: String): Parcelable
