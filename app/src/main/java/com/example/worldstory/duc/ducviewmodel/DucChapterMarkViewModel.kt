package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.model.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucChapterMarkViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _chaptersMarked= MutableLiveData<List<Chapter>>()
    val chaptersMarked: LiveData<List<Chapter>> get()=_chaptersMarked

    private val _chaptersMarkedByStory= MutableLiveData<List<Chapter>>()
    val chaptersMarkedByStory: LiveData<List<Chapter>> get()=_chaptersMarkedByStory

    fun fetchChaptersMarkedByUserSession(){
        viewModelScope.launch{
            var userId=context.getUserIdSession()
            val resultChapters= withContext(Dispatchers.IO){
                repository.getChaptersMarkedByUser(userId)
            }
            _chaptersMarked.value=resultChapters
        }
    }
    fun fetchChaptersMarkedByUserSessionAndStory(storyId: Int){
        viewModelScope.launch{
            var userId=context.getUserIdSession()
            val resultChapters= withContext(Dispatchers.IO){
                repository.getChaptersMarkedByUserAndStory(userId,storyId)
            }
            _chaptersMarkedByStory.value=resultChapters
        }
    }
    fun addChapterMarkByUserSession(chapterId:Int){
        viewModelScope.launch{
            var userIdSession=context.getUserIdSession()
            repository.setChapterMark(userIdSession,chapterId)
        }
    }
    fun deleteChapterMarkByUserSession(chapterId:Int){
        viewModelScope.launch{
            var userIdSession=context.getUserIdSession()
            repository.deleteChapterMark(userIdSession,chapterId)
        }
    }
}