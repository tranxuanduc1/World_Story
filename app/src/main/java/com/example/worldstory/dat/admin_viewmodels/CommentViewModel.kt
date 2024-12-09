package com.example.worldstory.dat.admin_viewmodels

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dat.admin_view_navs.CommentFragment
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Comment
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter

class CommentViewModel(private val db: DatabaseHelper, private val id: Int?) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments
    private val tempComments = mutableListOf<Comment>()

    val sumComments = MutableLiveData<String>()

    private val _commentUserMap = MutableLiveData<Map<Comment, User>>()
    private val tempCommentUserMap = mutableMapOf<Comment, User>()
    val commentUserMap: LiveData<Map<Comment, User>> get() = _commentUserMap

    private val _userList = MutableLiveData<List<User>>()
    private val tempUserList = mutableListOf<User>()
    val userList: LiveData<List<User>> get() = _userList


    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        fetch()
    }

    fun fetch() {
        tempComments.clear()
        tempCommentUserMap.clear()
        tempUserList.clear()

        try {
            if (id != null) {
                tempComments.addAll(db.getCommentsByStory(id))
                getUsers(tempComments).forEach { (k, v) -> tempCommentUserMap[k] = v }
            }

            tempCommentUserMap.forEach { (k, v) -> tempUserList.add(v) }

            _userList.value = tempUserList
            _commentUserMap.value = tempCommentUserMap
            _comments.value = tempComments
            sumComments.value = comments.value?.size.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getUsers(comments: List<Comment>): Map<Comment, User> {
        val userMap = mutableMapOf<Comment, User>()
        try {
            comments.forEach { c ->
                userMap[c] = db.getUserByUsersId(c.userId)!!
            }
            return userMap
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyMap()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonth(date: String): Month {
        val dateTime = LocalDateTime.parse(date, formatter)
        return dateTime.month
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getYear(date: String): Int {
        val dateTime = LocalDateTime.parse(date, formatter)
        return dateTime.year
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun groupCommentsByQuarter(comments: List<Comment>): Map<String, Int> {
        return comments.groupBy { comment ->
            val month = getMonth(comment.time)
            val quarter = when {
                month in Month.JANUARY..Month.MARCH -> "Q1"
                month in Month.APRIL..Month.JUNE -> "Q2"
                month in Month.JULY..Month.SEPTEMBER -> "Q3"
                month in Month.OCTOBER..Month.DECEMBER -> "Q4"
                else -> "Unknown"
            }
            val year = getYear(comment.time)
            "$quarter,$year" // Định danh quý trong năm
        }.mapValues { entry ->
            entry.value.size // Đếm số lượng bình luận trong quý
        }
    }

    fun delComment(commentId: Int) {
        if (commentId > 0)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    db.deleteComment(commentId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                withContext(Dispatchers.Main) {
                    fetch()
                }
            }

    }

}


class CommentViewModelFactory(
    private val databaseHelper: DatabaseHelper,
    private val id: Int?
) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(databaseHelper, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}