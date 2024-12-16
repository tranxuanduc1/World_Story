package com.example.worldstory.view_models.ducviewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.data.ducdataclass.DucComboHighScoreStoryDataClass
import com.example.worldstory.data.ducdataclass.DucStoriesAndGenreDataClass
import com.example.worldstory.duc.ducutils.getLoremIpsum
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.data.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.data.model.Genre
import com.example.worldstory.data.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucStoryViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories

    private val _genreAndStoriesByGenre = MutableLiveData<Pair<Genre,List<Story>>>()
    val genreAndStoriesByGenre: LiveData<Pair<Genre,List<Story>>> get() = _genreAndStoriesByGenre

    private val _listOfStoriesAndGenre = MutableLiveData<List<DucStoriesAndGenreDataClass>>()
    val listOfStoriesAndGenre: LiveData<List<DucStoriesAndGenreDataClass>> get() = _listOfStoriesAndGenre

    private val _storiesHistory = MutableLiveData<List<Story>>()
    val storiesHistory: LiveData<List<Story>> get() = _storiesHistory

    private val _storiesUserSessionLoved= MutableLiveData<List<Story>>()
    val storiesUserSessionLoved: LiveData<List<Story>> get() = _storiesUserSessionLoved

    private val _storiesIsText = MutableLiveData<List<Story>>()
    val storiesIsText: LiveData<List<Story>> get() = _storiesIsText

    private val _comboHighScoreStories = MutableLiveData<List<DucComboHighScoreStoryDataClass>>()
    val comboHighScoreStories: LiveData<List<DucComboHighScoreStoryDataClass>> get() = _comboHighScoreStories

    private val _storiesByUser = MutableLiveData<List<Story>>()
    val storiesByUser: LiveData<List<Story>> get() = _storiesByUser

    init {
        fetchStories()
        fetchStoriesHistory()
        fetchStoriesUserSessionLoved()

    }




     fun fetchStories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllStories()
            }
            //lay rating cua tung story
            setRatingByStory(result)
            _stories.value = result

        }

    }
    fun fetchStoriesIsText(isText: Boolean) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getStoriesByIsText(isText =isText )
            }
            //lay rating cua tung story
            setRatingByStory(result)
            _storiesIsText.value = result

        }

    }
     fun fetchStoriesHistory() {

        viewModelScope.launch {
            var userId=context.getUserIdSession()
            val result = withContext(Dispatchers.IO) {
                repository.getStoriesHistoryByUser(userId)
            }

            //lay rating cua tung story
            setRatingByStory(result)
            _storiesHistory.value = result

        }
    }
     fun fetchStoriesUserSessionLoved() {
        viewModelScope.launch {
            var userId=context.getUserIdSession()
            val result = withContext(Dispatchers.IO) {
                repository.getLoveStoriesByUser(userId)
            }

            //lay rating cua tung story
            setRatingByStory(result)
            _storiesUserSessionLoved.value = result

        }
    }
    fun fetchGenreAndStoriesByGenre(genre: Genre, isText: Boolean){
        viewModelScope.launch{
            val resultStoriesByGenre=withContext(Dispatchers.IO){
                repository.getStoriesByGenre(genre.genreID?: numDef,isText)
            }

            //lay rating cua tung story
            setRatingByStory(resultStoriesByGenre)

            _genreAndStoriesByGenre.value= Pair(genre, resultStoriesByGenre)
        }
    }
    fun fetchListOfStoriesAndGenre(listOfGenres: List<Genre>, isText: Boolean){
        viewModelScope.launch{
            var listOfSAndG=mutableListOf<DucStoriesAndGenreDataClass>()
            listOfGenres.forEach{
                genre->
                genre.genreID?.let {
                    var resultStories=withContext(Dispatchers.IO){
                        repository.getStoriesByGenre(genre.genreID,isText)
                    }
                    listOfSAndG.add(DucStoriesAndGenreDataClass(resultStories,genre))
                }

            }
            _listOfStoriesAndGenre.value=listOfSAndG
        }
    }
    fun fetchComboHighScoreStories(stories: List<Story>){
        viewModelScope.launch{
            var resultStoryIdComment=withContext(Dispatchers.IO){
                repository.getAllStoryIdsInComment()
            }
            var resultStoryIdRate=withContext(Dispatchers.IO){
                repository.getAllStoryIdsInRate()
            }

            //lay so commtn moi story
            var mapOfStoryIdComment=mutableMapOf<Int, Int>()
            resultStoryIdComment.forEach{
                storyId->
                mapOfStoryIdComment[storyId]=mapOfStoryIdComment.getOrDefault(storyId,0)+1
            }

            //lay so rate moi story
            var mapOfStoryIdRate=mutableMapOf<Int, Int>()
            resultStoryIdRate.forEach{
                    storyId->
                mapOfStoryIdRate[storyId]=mapOfStoryIdRate.getOrDefault(storyId,0)+1
            }

            //tong hop vao
            var listOfComboHighScoreStories=mutableListOf<DucComboHighScoreStoryDataClass>()
            stories.forEach{
                story->
                var numComment=mapOfStoryIdComment.getOrDefault(story.storyID,0)
                var numRate=mapOfStoryIdRate.getOrDefault(story.storyID,0)
                var combo= DucComboHighScoreStoryDataClass(story,numComment,numRate)
                listOfComboHighScoreStories.add(combo)
            }
            _comboHighScoreStories.value=listOfComboHighScoreStories
        }
    }

    fun fetchStoriesByUser(userId: Int){
        viewModelScope.launch{
            var resultStoriesByUser=withContext(Dispatchers.IO){
                repository.getStoriesByUser(userId)
            }
            _storiesByUser.value=resultStoriesByUser
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
    @SuppressLint("DefaultLocale")
    private fun setRatingByStory(result: List<Story>){
        //lay rating cua tung story
        for(story in result){
            var ratings=repository.getRatingsByStory(story.storyID?:numDef)

            if(ratings.isNullOrEmpty()){
                story.score=5f
            }else{
                var averageScore= String.format("%.1f", ratings.map { it.score }.average()).replace(",", ".").toFloat()
                story.score=averageScore

            }

        }
    }
}