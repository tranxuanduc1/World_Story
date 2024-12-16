package com.example.worldstory.view_models.ducviewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.data.ducdataclass.DucCommentDataClass
import com.example.worldstory.data.ducrepository.DucDataRepository
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.data.model.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucCommentViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {
    private val _commentsByStory = MutableLiveData<List<DucCommentDataClass>>()
    val commentsByStory: LiveData<List<DucCommentDataClass>> get() = _commentsByStory

    fun fetchCommentsByStory(storyId: Int) {
        viewModelScope.launch {
            val resultCommentsByStory = withContext(Dispatchers.IO) {
                repository.getCommentsByStory(storyId)
            }
            _commentsByStory.value = resultCommentsByStory
        }
    }

    fun checkCommentFromUser(comment: DucCommentDataClass): Boolean {
        var userID = context.getUserIdSession()
        return (comment.userId == userID)
    }

//    fun getAllComment(): List<DucCommentDataClass> {
//        return SampleDataStory.getListOfComment(context)
//    }
//    fun getCommentsByUser(idUser:Int): List<DucCommentDataClass> {
//        return SampleDataStory.getListOfComment(context).filter { it.userId== idUser }
//    }

    fun createUserCommnet(storyId: Int, content: String) {
        var userIdSession = context.getUserIdSession()
        var comment = Comment(null, content, dateTimeNow(), userIdSession, storyId)
        viewModelScope.launch {
            repository.createComment(comment)

        }

    }
    fun createUserCommnetWithReply(storyId: Int, content: String,commentReplyId: Int) {
        var userIdSession = context.getUserIdSession()
        var comment = Comment(null, content, dateTimeNow(), userIdSession, storyId,commentReplyId=commentReplyId)
        viewModelScope.launch {
            repository.createComment(comment)

        }

    }

}