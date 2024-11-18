package com.example.worldstory.duc.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel

class DucChapterViewModelFactory(private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucChapterViewModel::class.java))
        {
            return DucChapterViewModel(context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}