package com.example.worldstory.duc.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.duc.ducviewmodel.DucStoryViewModel

class DucStoryViewModelFactory(private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucStoryViewModel::class.java))
        {
            return DucStoryViewModel(context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}