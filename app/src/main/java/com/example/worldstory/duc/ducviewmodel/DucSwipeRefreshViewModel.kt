package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucSwipeRefreshViewModel(var repository: DucDataRepository, var context: Context) :
    ViewModel() {
    private val _refreshView = MutableLiveData<Boolean>()
    val refreshView: LiveData<Boolean> get() = _refreshView

//    init {
//        fetchRefreshView()
//    }

    fun fetchRefreshView() {
        viewModelScope.launch {
            var currentValue= _refreshView.value
            if(currentValue!=null){
                _refreshView.value=!currentValue
            }else{
                _refreshView.value = true

            }
        }
    }
}