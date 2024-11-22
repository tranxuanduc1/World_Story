package com.example.worldstory.duc.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducviewmodel.DucImageViewModel

class DucImageViewModelFactory(var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var dbHelper= DatabaseHelper(context)
        var repository= DucDataRepository(dbHelper)

        return DucImageViewModel(repository,context) as T
    }

}