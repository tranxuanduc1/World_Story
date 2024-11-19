package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucGenreDataClass
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.getLoremIpsum
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.myapplication.R
class DucStoryViewModel(var context: Context): ViewModel() {
    private val _stories= MutableLiveData<List<DucStoryDataClass>>()

//    val stories : LiveData<List<StoryDataClass>> get() =_stories
//    init {
//        _stories.value= SampleDataStory.getDataList(this)
//    }
    fun getComicStoriesByGenre( genre: DucGenreDataClass?): List<DucStoryDataClass>{
        var comicStoryList= SampleDataStory.getDataList(context).filter { it.isComic==true }
        return comicStoryList
    }
    fun getTextStoriesByGenre(genre: DucGenreDataClass?): List<DucStoryDataClass>{
        var comicStoryList= SampleDataStory.getDataList(context).filter { it.isComic==false }
        return comicStoryList
    }
    fun getTextStories( ): List<DucStoryDataClass>{
        var comicStoryList= SampleDataStory.getDataList(context).filter { it.isComic==false }
        return comicStoryList
    }
    fun getComicStories( ): List<DucStoryDataClass>{
        var comicStoryList= SampleDataStory.getDataList(context ).filter { it.isComic==true }
        return comicStoryList
    }
    fun getStoriesByQuery(query: String,isComic: Boolean): List<DucStoryDataClass>{
        var comicStoryList= SampleDataStory.getDataList(context).filter { it.isComic==isComic }
            .filter { it.title.lowercase().contains(query.lowercase(),true) }
        return comicStoryList
    }
    fun getOneExampleStory(): DucStoryDataClass{
        return DucStoryDataClass(1, getLoremIpsum(context),getLoremIpsum(context),
            getLoremIpsumLong(context),SampleDataStory.getExampleImgURL(),SampleDataStory.getExampleImgURL(), SampleDataStory.date,4f,true)

    }
}