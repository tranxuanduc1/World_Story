package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.ducdataclass.DucComboChapterDataClass
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucChapterMarkViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _chaptersMarked= MutableLiveData<List<Chapter>>()
    val chaptersMarked: LiveData<List<Chapter>> get()=_chaptersMarked

    private val _chaptersMarkedByStory= MutableLiveData<List<Chapter>>()
    val chaptersMarkedByStory: LiveData<List<Chapter>> get()=_chaptersMarkedByStory

//    private val _chaptersMarkedAndStory= MutableLiveData<List<Pair<Chapter,Story>>>()
//    val chaptersMarkedAndStory: LiveData<List<Pair<Chapter,Story>>> get()=_chaptersMarkedAndStory

    private val _comboChapter= MutableLiveData<List<DucComboChapterDataClass>>()
    val comboChapter: LiveData<List<DucComboChapterDataClass>> get()=_comboChapter

    fun fetchChaptersMarkedByUserSession(){
        viewModelScope.launch{
            var userId=context.getUserIdSession()
            val resultChapters= withContext(Dispatchers.IO){
                repository.getChaptersMarkedByUser(userId)
            }
            _chaptersMarked.value=resultChapters
        }
    }
    fun fetchComboChapterByUserSession(){
        viewModelScope.launch{
            var userId=context.getUserIdSession()
            var listOfChapterMarkAndStory=mutableListOf<DucComboChapterDataClass>()
            val resultChapters= withContext(Dispatchers.IO){
                repository.getChaptersMarkedByUser(userId)
            }
            // lay story cua tung chapter
            resultChapters.forEach{
                    chapter->
                var resultStory=withContext(Dispatchers.IO){
                    repository.getStoriesByStoryId(chapter.storyID)
                }

                resultStory?.let {
                        story->
                    //lay danh sach chuong trong cung chuyen de lay pre va next chapter
                    var resultListChapter=withContext(Dispatchers.IO){
                        repository.getChaptersByStory(story.storyID?: numDef)

                    }
                    var nextChapter=getNextChapterByCurrentChapter(chapter,resultChapters)
                    var preChapter=getPreviousChapterByCurrentChapter(chapter,resultChapters)

                    listOfChapterMarkAndStory.add(DucComboChapterDataClass(preChapter,chapter,nextChapter,story))


                }

            }

            _comboChapter.value=listOfChapterMarkAndStory
        }
//        viewModelScope.launch{
//            var userId=context.getUserIdSession()
//            var listOfChapterMarkAndStory=mutableListOf<Pair<Chapter,Story>>()
//            val resultChapters= withContext(Dispatchers.IO){
//                repository.getChaptersMarkedByUser(userId)
//            }
//            // lay story cua tung chapter
//            resultChapters.forEach{
//                chapter->
//                var resultStory=withContext(Dispatchers.IO){
//                    repository.getStoriesByStoryId(chapter.storyID)
//                }
//
//                resultStory?.let {
//                    story->
//                    listOfChapterMarkAndStory.add(Pair(chapter,story))
//
//                }
//
//            }
//
//            _chaptersMarkedAndStory.value=listOfChapterMarkAndStory
//        }
    }
    fun fetchChaptersMarkedByUserSessionAndStory(storyId: Int){
        viewModelScope.launch{
            var userId=context.getUserIdSession()
            val resultChapters= withContext(Dispatchers.IO){
                repository.getChaptersMarkedByUserAndStory(userId,storyId)
            }
            _chaptersMarkedByStory.value=resultChapters
        }
    }
    fun addChapterMarkByUserSession(chapterId:Int){
        viewModelScope.launch{
            var userIdSession=context.getUserIdSession()
            repository.setChapterMark(userIdSession,chapterId)
        }
    }
    fun deleteChapterMarkByUserSession(chapterId:Int){
        viewModelScope.launch{
            var userIdSession=context.getUserIdSession()
            repository.deleteChapterMark(userIdSession,chapterId)
        }
    }
    fun getNextChapterByCurrentChapter(currentChapter: Chapter,listOfChapters: List<Chapter>): Chapter? {
        var listAllChaptersByStory =listOfChapters
        var idxCurrentChapter = listAllChaptersByStory.indexOf(currentChapter)
        if (listAllChaptersByStory.size > idxCurrentChapter + 1) {
            return listAllChaptersByStory.get(idxCurrentChapter + 1)
        } else {
            return null
        }
    }

    fun getPreviousChapterByCurrentChapter(currentChapter: Chapter,listOfChapters: List<Chapter>): Chapter? {
        var listAllChaptersByStory =listOfChapters

        var idxCurrentChapter = listAllChaptersByStory.indexOf(currentChapter)
        if (0 <= idxCurrentChapter - 1) {
            return listAllChaptersByStory.get(idxCurrentChapter - 1)
        } else {
            return null
        }
    }
}