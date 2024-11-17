package com.example.worldstory.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.ducviewmodel.DucCommentViewModel

class DucCommentViewModelFactory (private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucCommentViewModel::class.java))
        {
            return DucCommentViewModel(context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}