package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Image

class ChapterViewModel(private val db: DatabaseHelper) : ViewModel() {
    val imgMap: MutableLiveData<Map<Int, String>> = MutableLiveData()
    val name = MutableLiveData<String>()
    private val _isChangedImgMap=MutableLiveData<Boolean?>()
    val isChangedImgMap:LiveData<Boolean?>get()=_isChangedImgMap

    fun onAddChapter(storyID: Int) {
        val chapter = Chapter(
            null, name.value.toString(), dateTimeNow.toString(), storyID
        )
        val l: Long = db.insertChapter(chapter)
        if (imgMap.value != null)
            for (i in imgMap.value!!) {
                val img=Image(null,i.value,i.key,l.toInt())
                db.insertImage(img)
            }
    }
    fun onChangedImgMap(){
        _isChangedImgMap.value=true
    }
    fun onChangedImgMapHandled(){
        _isChangedImgMap.value=false
    }
    fun removeAllSelectedImgs(){
        imgMap.value= emptyMap()
    }
}

class ChapterViewModelFactory(private val databaseHelper: DatabaseHelper) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChapterViewModel::class.java)) {
            return ChapterViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}