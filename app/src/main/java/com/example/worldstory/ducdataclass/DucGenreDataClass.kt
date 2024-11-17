package com.example.worldstory.ducdataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucGenreDataClass(var idGenre: Int, var title: String): Parcelable
