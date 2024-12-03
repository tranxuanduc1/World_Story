package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucAccountManagerViewModel (var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _checkAccount= MutableLiveData<Pair< Boolean, String>>()
    val checkAccount:LiveData<Pair<Boolean, String>>  get()=_checkAccount

    fun fetchCheckAccount(userName: String,password: String){
        viewModelScope.launch{
            val resultCheck= withContext(Dispatchers.IO){
                repository.checkAccountByUserName(userName,password)

            }
            _checkAccount.value=resultCheck
        }
    }
    fun SignUpNewAccount(){

    }

}