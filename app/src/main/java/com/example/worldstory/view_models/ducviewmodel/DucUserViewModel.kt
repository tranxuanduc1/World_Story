package com.example.worldstory.view_models.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.data.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.AUTHOR
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucUserViewModel (var repository: DucDataRepository, var context: Context) :
    ViewModel() {

    private val _authorUser= MutableLiveData<List<User>>()
    val userAuthor:LiveData< List<User>>  get()=_authorUser

    private val _userbyUserId= MutableLiveData<User?>()
    val userbyUserId:LiveData<  User?>  get()=_userbyUserId

    private val _userLiveData= MutableLiveData<User?>()
    val userLiveData:LiveData<  User?>  get()=_userLiveData

    var avtIdLiveData= mutableListOf<String>()
    init {
        //fetchAuthorUser()
        fetchUserSession()
    }

    fun fetchUserSession() {

        viewModelScope.launch{
            var userId= context.getUserIdSession()
            var result= withContext(Dispatchers.IO){
                repository.getUserByUserId(userId)
            }
            _userLiveData.value=result

        }
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
    fun fetchUserByUserId(userId: Int){
        viewModelScope.launch{
            var resultUser= withContext(Dispatchers.IO){
                repository.getUserByUserId(userId)
            }
            _userbyUserId.value=resultUser

        }
    }
    fun updateAvatar(){
        var userId=context.getUserIdSession()
        viewModelScope.launch{
            var user =withContext(Dispatchers.IO){
                repository.getUserByUserId(userId)
            }
                user?.let {
                    it.imgAvatar=transform( avtIdLiveData.firstOrNull().toString())
                    repository.updateUser(it)

                }
        }
    }
    fun transform(id: String): String {
        return "https://drive.usercontent.google.com/download?id=${id}&export=view"
    }
}