package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.getLoremIpsum
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucStoryViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories

    private val _storiesByGenre = MutableLiveData<List<Story>>()
    val storiesByGenre: LiveData<List<Story>> get() = _storiesByGenre
    init {
        fetchStories()
    }

     fun fetchStories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllStories()
            }
            _stories.value = result

        }

    }
    fun fetchStoriesByGenre(genreId: Int, isText: Boolean){
        viewModelScope.launch{
            val result=withContext(Dispatchers.IO){
                repository.getStoriesByGenre(genreId,isText)
            }
            _storiesByGenre.value=result
        }
    }

    fun getStoriesByGenre(genreId: Int, isText: Boolean): List<Story> {
        return  repository.getStoriesByGenre(genreId,isText)
    }
    fun getStoriesByGenre(genreId: Int, isText: Boolean?): List<Story> {
        repository.getStoriesByGenre(genreId, isText = isText?:false)
        var textStoryList = _stories.value ?: listOf<Story>()
        // xu ly loc genreId
        var filter = textStoryList

        if(isText==null) return filter

        filter=filter.filter {  it.isTextStory.toBoolean() == isText}
        return filter
    }

    fun getTextStories(): List<Story> {
        var textStoryList = _stories.value ?: listOf<Story>()
        var filter = textStoryList.filter { it.isTextStory == 1 }
        return filter
    }

    fun getComicStories(): List<Story> {
        var comicStoryList = _stories.value ?: listOf<Story>()
        var filter = comicStoryList.filter { it.isTextStory == 0 }
        return filter
    }

    fun getStoriesByQuery(query: String, isText: Boolean?): List<Story> {

        var storyList = _stories.value ?: listOf<Story>()
        var filter = storyList
            .filter { it.title.lowercase().contains(query.lowercase(), true) }

        // if dont use isText
        if (isText == null) return filter

        filter = filter.filter { it.isTextStory.toBoolean() == isText }
        return filter
    }

    fun getStoriesByIsText(isText: Boolean): List<Story> {
        var storyList = _stories.value ?: listOf<Story>()
        var filter = storyList.filter { it.isTextStory.toBoolean() == isText }
        return filter
    }

    fun getOneExampleStory(): Story {

        return Story(
            1,
            getLoremIpsum(context),
            getLoremIpsum(context),
            getLoremIpsumLong(context),
            SampleDataStory.getExampleImgURL(),
            SampleDataStory.getExampleImgURL(),
            0,
            dateTimeNow,
            4f,
            1
        )

    }
}