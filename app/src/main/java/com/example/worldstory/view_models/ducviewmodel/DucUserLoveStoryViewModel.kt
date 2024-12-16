package com.example.worldstory.view_models.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.data.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.data.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucUserLoveStoryViewModel(var repository: DucDataRepository, var context: Context) :
    ViewModel() {
    private val _userSessionLoveStories = MutableLiveData<List<Story>>()
    val userSessionLoveStories: LiveData<List<Story>> get() = _userSessionLoveStories

    init {
        fetchUserSessionLoveStory()
    }
    private fun fetchUserSessionLoveStory(){
        var userId= context.getUserIdSession()
        viewModelScope.launch{
            val resultUserSessionLoveStory= withContext(Dispatchers.IO){
                repository.getLoveStoriesByUser(userId)
            }
            _userSessionLoveStories.value=resultUserSessionLoveStory
        }
    }
    fun setUserSessionLovedStory(storyId: Int){
        var userId= context.getUserIdSession()

        viewModelScope.launch{
           repository.setUserLovedStory(userId,storyId)
        }
    }
    fun deleteUserSessionLovedStory(storyId: Int){
        var userId= context.getUserIdSession()

        viewModelScope.launch{
            repository.deleteUserLovedStory(userId,storyId)
        }
    }
}