package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Rate
import com.example.worldstory.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RateViewModel(private val db: DatabaseHelper, private val id: Int?) : ViewModel(db) {

    private val _rateList = MutableLiveData<List<Rate>>()
    val rateList: LiveData<List<Rate>> get() = _rateList
    private val _rateListByScore = MutableLiveData<List<Rate>>()
    val rateListByScore: LiveData<List<Rate>> get() = _rateListByScore

    private val _userList = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _userList

    private val tempRateList = mutableListOf<Rate>()
    private val tempRateListByScore = mutableListOf<Rate>()
    private val tempUserListByScore = mutableListOf<User>()
    private var currentScore=1

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    var count: Int = -1
    val s = MutableLiveData<String>()

    init {
        fetch()
        setRateListBtScore(currentScore)
    }

    fun fetch() {
        tempUserListByScore.clear()
        tempRateList.clear()
        try {
            if (id != null) {
                tempRateList.addAll(db.getRatesByStory(id))
            }
            _rateList.value = tempRateList
            s.value = count.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    fun setCount(score: Int): Int {
        return rateListByScore.value?.size ?: 0
    }

    fun setRateListBtScore(score: Int) {
        currentScore=score
        _isLoading.value = true
        tempRateListByScore.clear()
        tempUserListByScore.clear()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                rateList.value?.forEach { rate ->
                    if (rate.score == score) tempRateListByScore.add(rate)
                }
                tempUserListByScore.addAll(getUserList(tempRateListByScore))
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("Rate", "Lỗi set")
            }
            withContext(Dispatchers.Main) {
                _rateListByScore.value = tempRateListByScore
                _userList.value = tempUserListByScore
                count = setCount(score)
                s.value = count.toString()
                _isLoading.value = false
            }
        }

    }

    private fun getUserList(rateList: List<Rate>): List<User> {
        val userList = mutableListOf<User>()
        try {
            rateList.forEach { r ->
                userList.add(db.getUserByUsersId(r.userID)!!)
            }
            return userList
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }


    fun delete(rate: Rate?): Int {
        try {

            val i = db.deleteRate(rate)
            fetch()
            Log.w("del",rateList.value?.size.toString())
            setRateListBtScore(currentScore )
            return i
        } catch (e: Exception) {
            e.printStackTrace()
            fetch()
            setRateListBtScore(currentScore )
            return -1
        }
    }

    fun getRatio(i: Int): Float {
        try {
            if (i < 1 || i > 5) {
                return -1f
            } else {
                val sumAll: Float? = rateList.value?.count()?.toFloat()
                val sumOneType: Float? = rateList.value?.count { it.score == i }?.toFloat()
                if (sumOneType != null && sumAll != null) {
                    return sumOneType / sumAll
                } else return -1f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1f
        }

    }

    fun getRateListByScore(score: Int): List<Rate> {
        try {
            if (rateList.value.isNullOrEmpty()) {
                return emptyList()
            } else return rateList.value!!.filter { it.score == score }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

    }

}


class RateViewModelFactory(private val databaseHelper: DatabaseHelper, private val id: Int?) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RateViewModel::class.java)) {
            return RateViewModel(databaseHelper, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}