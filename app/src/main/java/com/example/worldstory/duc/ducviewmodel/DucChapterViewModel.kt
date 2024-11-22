package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getLoremIpsum
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucChapterViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    var isFirstLoadChapter: Boolean = true
    var mainChapter: Chapter? = null
    var nextChapter: Chapter? = null
    var previousChapter: Chapter? = null
    private val _chapters = MutableLiveData<List<Chapter>>()
    val chapters: LiveData<List<Chapter>> get() = _chapters
    private val _chaptersByStory = MutableLiveData<List<Chapter>>()
    val chaptersByStory: LiveData<List<Chapter>> get() = _chaptersByStory
    init {
        //fetchChapters()
    }

    private fun fetchChapters() {
        viewModelScope.launch {
            val resultChapters = withContext(Dispatchers.IO) {
                repository.getAllChapter()
            }
            _chapters.value = resultChapters


        }
    }


    fun setPreMainNextChapter(
        mChapter: Chapter?, pChapter: Chapter?,
        nChapter: Chapter?
    ) {
        mainChapter = mChapter
        previousChapter = pChapter
        nextChapter = nChapter

        // set data chaptersByStory
        if(mainChapter!=null){
            viewModelScope.launch{
                val result=withContext(Dispatchers.IO)
                {
                    repository.getChaptersByStory(mainChapter?.storyID?:1)
                }
                _chaptersByStory.value=result

            }
        }
    }
    fun setChaptersByStory(storyId: Int){
        viewModelScope.launch{
            val result=withContext(Dispatchers.IO)
            {
                repository.getChaptersByStory(storyId)
            }
            _chaptersByStory.value=result

        }
    }
    fun getChaptersByStory(story: Story): List<Chapter> {
        var list = _chapters.value?:listOf<Chapter>()
        var filter =list.filter { it.storyID == story.storyID }
        return filter
    //return SampleDataStory.getListOfChapter(context).filter { it.idStory == story.storyID }
    }

    fun getChaptersByStory(storyId: Int): List<Chapter> {
        setChaptersByStory(storyId)
        var list = _chaptersByStory.value?:listOf<Chapter>()
       // var filter =list.filter { it.storyID == storyId }
        return list
       // return _chaptersByStory.value?:listOf<Chapter>()
        //return SampleDataStory.getListOfChapter(context).filter { it.idStory == idStory }
    }

//    fun getOneChapter(story: DucStoryDataClass, idxChapter: Int): Chapter {
//        return SampleDataStory.getListOfChapter(context).get(idxChapter)
//
//    }

    fun getOneExampleChapter(): Chapter {
        return Chapter(1, getLoremIpsum(context), dateTimeNow,1)

    }

    fun getNextChapterByCurrentChapter(currentChapter: Chapter?): Chapter? {
        var tempChapter = getOneExampleChapter()
        var listAllChaptersByStory =
            getChaptersByStory(currentChapter?.storyID ?: tempChapter.storyID)
        var idxCurrentChapter = listAllChaptersByStory.indexOf(currentChapter)
        if (listAllChaptersByStory.size > idxCurrentChapter + 1) {
            return listAllChaptersByStory.get(idxCurrentChapter + 1)
        } else {
            return null
        }
    }

    fun getPreviousChapterByCurrentChapter(currentChapter: Chapter?): Chapter? {
        var tempChapter = getOneExampleChapter()
        var listAllChaptersByStory =
            getChaptersByStory(currentChapter?.storyID ?: tempChapter.storyID)

        var idxCurrentChapter = listAllChaptersByStory.indexOf(currentChapter)
        if (0 <= idxCurrentChapter - 1) {
            return listAllChaptersByStory.get(idxCurrentChapter - 1)
        } else {
            return null
        }
    }
}