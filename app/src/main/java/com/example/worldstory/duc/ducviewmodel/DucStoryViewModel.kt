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
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucStoryViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories

    private val _genreAndStoriesByGenre = MutableLiveData<Pair< Genre,List<Story>>>()
    val genreAndStoriesByGenre: LiveData<Pair< Genre,List<Story>>> get() = _genreAndStoriesByGenre

    private val _storiesHistory = MutableLiveData<List<Story>>()
    val storiesHistory: LiveData<List<Story>> get() = _storiesHistory

    private val _storiesUserSessionLoved= MutableLiveData<List<Story>>()
    val storiesUserSessionLoved: LiveData<List<Story>> get() = _storiesUserSessionLoved

    init {
        fetchStories()
        fetchStoriesHistory()
        fetchStoriesUserSessionLoved()

    }




    private fun fetchStories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllStories()
            }
            _stories.value = result

        }

    }
     fun fetchStoriesHistory() {

        viewModelScope.launch {
            var userId=context.getUserIdSession()
            val result = withContext(Dispatchers.IO) {
                repository.getStoriesHistoryByUser(userId)
            }
            _storiesHistory.value = result

        }
    }
     fun fetchStoriesUserSessionLoved() {
        viewModelScope.launch {
            var userId=context.getUserIdSession()
            val result = withContext(Dispatchers.IO) {
                repository.getLoveStoriesByUser(userId)
            }
            _storiesUserSessionLoved.value = result

        }
    }
    fun fetchGenreAndStoriesByGenre(genre: Genre, isText: Boolean){
        viewModelScope.launch{
            val resultStoriesByGenre=withContext(Dispatchers.IO){
                repository.getStoriesByGenre(genre.genreID?: numDef,isText)
            }
            //lay rating cua tung story
            for(story in resultStoriesByGenre){
                var ratings=repository.getRatingsByStory(story.storyID?:numDef)

                if(ratings.isNullOrEmpty()){
                    story.score=5f
                }else{
                    var averageScore= String.format("%.1f", ratings.map { it.score }.average()).toFloat()

                    story.score=averageScore

                }

            }

            _genreAndStoriesByGenre.value= Pair(genre, resultStoriesByGenre)
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
            dateTimeNow(),
            4f,
            1
        )

    }
}