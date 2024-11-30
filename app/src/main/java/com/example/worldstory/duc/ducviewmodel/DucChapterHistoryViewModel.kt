package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dbhelper.Contract
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.model.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucChapterHistoryViewModel (var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _chaptersHistoryByStory= MutableLiveData<List<Chapter>>()
    val chaptersHistoryByStory:LiveData<List<Chapter>>  get()=_chaptersHistoryByStory

    fun fetchChaptersHistoryByStory(storyId:Int){
        viewModelScope.launch{
            var userId= context.getUserIdSession()
            val result= withContext(Dispatchers.IO){

                repository.getChaptersHistoryByStoryAndUser(storyId,userId)

            }
            _chaptersHistoryByStory.value=result
        }
    }
    fun setChapterHistoryUserSession(chapterId: Int){
        var userId= context.getUserIdSession()
        repository.setChapterHistory(userId,chapterId)
    }

}