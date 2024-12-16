package com.example.worldstory.data.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucParagraphDataClass(var idParagraph: Int, var imgContent: String?, var textContent:String?,
                                 var position:Int, var idChapter: Int, var isComic: Boolean=true) : Parcelable
