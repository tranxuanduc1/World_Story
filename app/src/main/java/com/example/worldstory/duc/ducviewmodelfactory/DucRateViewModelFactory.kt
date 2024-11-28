package com.example.worldstory.duc.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducviewmodel.DucRateViewModel

class DucRateViewModelFactory (private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucRateViewModel::class.java))
        {
            var dbHelper= DatabaseHelper(context)
            var repository: DucDataRepository= DucDataRepository(dbHelper)
            return DucRateViewModel(repository,context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}