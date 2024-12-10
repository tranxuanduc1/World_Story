package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.AUTHOR
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucUserViewModel (var repository: DucDataRepository, var context: Context) :
    ViewModel() {

    private val _authorUser= MutableLiveData<List< User>>()
    val userAuthor:LiveData< List< User>>  get()=_authorUser

    init {
        fetchAuthorUser()
    }

    fun fetchAuthorUser() {
        viewModelScope.launch{
            var authorRoleId= AUTHOR
            var resultAuthorUsers= withContext(Dispatchers.IO){
                repository.getUsersByRole(authorRoleId)
            }
            _authorUser.value=resultAuthorUsers

        }
    }
}