package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.User

class UserViewModel(db: DatabaseHelper):ViewModel() {
    val users=MutableLiveData<List<User>>()
//
//    fun insert
}