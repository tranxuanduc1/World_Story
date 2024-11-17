package com.example.worldstory.ducviewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.ducviewmodel.DucParagraphViewModel

class DucParagraphViewModelFactory (private var context :Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DucParagraphViewModel::class.java))
        {
            return DucParagraphViewModel(context) as T
        }
        throw IllegalArgumentException("unknown view model class")
    }
}