package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucImageViewModel(var repository: DucDataRepository,var context: Context): ViewModel() {
    private val _imagesByChapter= MutableLiveData<List<Image>>()
    val imagesByChapter : LiveData<List<Image>> get()=_imagesByChapter

    fun setImagesByChapter(chapterId:Int){
        viewModelScope.launch{
            val result= withContext(Dispatchers.IO){
                repository.getImagesByChapter(chapterId)
            }
            _imagesByChapter.value=result
        }
    }
}