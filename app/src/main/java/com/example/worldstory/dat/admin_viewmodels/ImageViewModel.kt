package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Image

class ImageViewModel(private val db: DatabaseHelper) : ViewModel() {
    private val _imageListByChapter = MutableLiveData<List<Image>>()
    val imageListByChapter: LiveData<List<Image>> get() = _imageListByChapter


    fun insert(img: Image): Long {
        val l = db.insertImage(img)
        return l
    }

}
class ImageViewModelFactory(private val databaseHelper: DatabaseHelper) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}