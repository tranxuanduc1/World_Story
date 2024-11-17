package com.example.worldstory.ducviewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.worldstory.SampleDataStory
import com.example.worldstory.ducdataclass.DucGenreDataClass
import com.example.worldstory.ducdataclass.DucStoryDataClass

class DucGenreViewModel(var context: Context): ViewModel() {
    private val _genres= MutableLiveData<List<DucGenreDataClass>>()
//    val genres:LiveData<List<GenreDataClass>> get() =_genres
//    init {
//        _genres.value= SampleDataStory.getListOfGenre()
//    }
    fun getAllGenres(): List<DucGenreDataClass>{
        return SampleDataStory.getListOfGenre(context)
    }
    fun getGenresByStory(story: DucStoryDataClass):List<DucGenreDataClass>{
        return SampleDataStory.getListOfGenre(context).filter { it.idGenre<=4 }
    }
}