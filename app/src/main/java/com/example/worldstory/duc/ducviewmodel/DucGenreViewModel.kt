package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucGenreDataClass
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.model.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucGenreViewModel(var repository: DucDataRepository,var context: Context): ViewModel() {
    private val _genres= MutableLiveData<List<Genre>>()
    val genres:LiveData<List<Genre>> get() =_genres

    init {
       fetchGenres()
    }
    fun fetchGenres() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllGenres()
            }
            _genres.value = result

        }

    }

    fun getGenresByStory(storyId: Int):List<Genre>{
        return _genres.value?:listOf<Genre>()
    }
}