package com.example.worldstory.dat.admin_viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.worldstory.dbhelper.DatabaseHelper

class UserViewModel(application: Application):AndroidViewModel(application) {
    var userName=MutableLiveData<String>()
    var hashedpw=MutableLiveData<String>()

    init {
        val dbHelper = DatabaseHelper(application)
    }
    fun getAllUser(){
        return
    }
}