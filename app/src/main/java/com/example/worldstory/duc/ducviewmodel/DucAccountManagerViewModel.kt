package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.User
import com.example.worldstory.model.Role
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucAccountManagerViewModel (var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _checkAccountLogin= MutableLiveData<UserLoginStateEnum>()
    val checkAccount:LiveData<UserLoginStateEnum>  get()=_checkAccountLogin

    private val _checkAccountExist= MutableLiveData<UserLoginStateEnum>()
    val checkAccountExist:LiveData<UserLoginStateEnum>  get()=_checkAccountExist

    private val _userSessionAndRole= MutableLiveData<Pair<User, Role>>()
    val userSessionAndRole:LiveData<Pair<User, Role>>  get()=_userSessionAndRole

    private val _user= MutableLiveData<User>()
    val user:LiveData<User>  get()=_user
    fun fetchCheckAccountLogin(userName: String, password: String){
        viewModelScope.launch{
            val resultCheck= withContext(Dispatchers.IO){
                repository.checkAccountByUserNameWhenLogin(userName,password)

            }
            _checkAccountLogin.value=resultCheck
        }
    }
    fun fetchCheckAccountExist(userName: String){
        viewModelScope.launch{
            val resultCheck= withContext(Dispatchers.IO){
                repository.checkAccountExist(userName)

            }
            _checkAccountExist.value=resultCheck
        }
    }
    fun fetchUserSessionAndRoleByUsername(userName: String){
        viewModelScope.launch{
            val resultUser= withContext(Dispatchers.IO){
                repository.getUserByUsername(userName)
            }

            resultUser?.let {
                val resultRole= withContext(Dispatchers.IO){
                    repository.getRoleByRoleId(resultUser.roleID)
                }
                resultRole?.let {
                    _userSessionAndRole.value= Pair(resultUser,resultRole)

                }
            }
        }
    }
    fun SignUpNewAccount(username: String,password: String,email: String,nickname: String){

        repository.addNewUserMember(username,password,email,nickname,dateTimeNow())
    }
    fun fetchNewGuestAccount(){
        viewModelScope.launch{
            var guestUser=repository.createGuestUser()
            val resultUser= withContext(Dispatchers.IO){
               repository.getUserByUsername(guestUser.userName)
            }

            resultUser?.let {

                    _user.value= resultUser


            }
        }
    }
}