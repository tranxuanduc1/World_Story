package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.toInt
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story

class StoryViewModel(private val db: DatabaseHelper) : ViewModel(db) {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> get() = _stories
    val title = MutableLiveData<String>()
    val author = MutableLiveData<String>()
    val decription = MutableLiveData<String>()
    val isText = MutableLiveData(false)
    val storyImg = MutableLiveData<String>()
    val genreIDList = MutableLiveData<List<Int>>()
    val storyGenreMap = mutableMapOf<Int, Set<Int>>()


    val currentStoryID = MutableLiveData(-1)
    val chapterListByStory = mutableListOf<Chapter>()

    init {
        fetchAllStories()
        fetchAllChapters()
    }


    fun fetchAllChapters() {
        if (currentStoryID.value!! >= 0)
            chapterListByStory.addAll(db.getChaptersByStory(currentStoryID.value!!))
    }

    fun setIDStory(id: Int?) {
        currentStoryID.value = id
        fetchAllChapters()
    }

    fun setStoryByID(id: Int?) {
        val story = stories.value?.filter { it -> it.storyID == id }!!.first()
        author.value = story.author
        decription.value = story.description
        isText.value = if (story.isTextStory == 1) true else false
        title.value = story.title
        genreIDList.value = storyGenreMap[story.storyID]?.toList()
        storyImg.value = story.bgImgUrl
    }

    fun onAddNewStory(): Long {
        val story = Story(
            null,
            title.value.toString(),
            author.value.toString(),
            decription.value.toString(),
            SampleDataStory.getExampleImgURL(),
            SampleDataStory.getExampleImgURLParagraph(),
            isTextStory = isText.value?.toInt() ?: 1,
            dateTimeNow,
            Float.MAX_VALUE / Float.MAX_VALUE,
            1
        )

        val l: Long = insertStory(story)
        genreIDList.value?.forEach { gID -> insertStoryGenre(l.toInt(), gID) }
        resetValue()
        return l
    }

    fun resetValue() {
        genreIDList.value = emptyList()
        decription.value = ""
        isText.value = false
        author.value = ""
        title.value = ""
        storyImg.value = ""
    }

    fun fetchAllStories() {
        _stories.value = db.getAllStories()
        stories.value?.forEach { a ->
            storyGenreMap[a.storyID ?: 0] = db.getGenreIDbyStoryID(a.storyID)
        }
    }

    fun insertStory(story: Story): Long {
        val l = db.insertStory(story)
        fetchAllStories()
        return l
    }

    fun insertStoryGenre(sID: Int, gID: Int): Long {
        return db.insertStoryGenre(sID, gID)
    }

    fun getStoryByGenre() {

    }

    fun deleteStory(story: Story): Int {
        val i: Int = db.deleteStory(story.storyID)
        fetchAllStories()
        return i
    }

    fun updateStory(story: Story): Int {
        val i: Int = db.updateStory(story)
        fetchAllStories()
        return i
    }
}

class StoryViewModelFactory(private val databaseHelper: DatabaseHelper) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}