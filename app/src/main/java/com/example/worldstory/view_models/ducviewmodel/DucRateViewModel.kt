package com.example.worldstory.view_models.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.data.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.isUserCurrentGuest
import com.example.worldstory.data.model.Rate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucRateViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _ratingsByStory = MutableLiveData<List<Rate>>()
    val ratingsByStory: LiveData<List<Rate>> get() = _ratingsByStory

    fun setRateByStory(storyId: Int) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getRatingsByStory(storyId)
            }

            _ratingsByStory.value = result

        }
    }
    fun getScoreRateByUserSession(): Float{
        var userSessionFilter=(_ratingsByStory.value?.filter { it.userID == context.getUserIdSession() })
        return if(userSessionFilter.isNullOrEmpty()){
                 -1f
        }else{
             userSessionFilter.first().score.toFloat()

        }
    }
    fun ratingStoryByCurrentUser(storyId: Int,score: Int) {
        var userId= context.getUserIdSession()

        viewModelScope.launch {

            if(userId!=-1 && !context.isUserCurrentGuest()){
                var newRate= Rate(null,score,userId,storyId)
                repository.ratingStoryByCurrentUser(newRate)
            }


        }
    }


}