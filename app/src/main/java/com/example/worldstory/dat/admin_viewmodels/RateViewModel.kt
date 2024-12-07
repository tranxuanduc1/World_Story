package com.example.worldstory.dat.admin_viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Rate
import com.example.worldstory.model.User

class RateViewModel(private val db: DatabaseHelper) : ViewModel(db) {

    private val _rateList = MutableLiveData<List<Rate>>()
    val rateList: LiveData<List<Rate>> get() = _rateList
    private val _rateListByScore = MutableLiveData<List<Rate>>()
    val rateListByScore: LiveData<List<Rate>> get() = _rateListByScore

    private val _userList=MutableLiveData<List<User>>()
    val users:LiveData<List<User>>get() = _userList
    init {
        fetch()
        setRateListBtScore(1)
    }

    fun fetch() {
        _rateList.value = db.getAllRates()
    }

    fun setRateListBtScore(score: Int) {
        try {
            _rateListByScore.value = rateList.value?.filter { it.score == score }?.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("Rate", "Lỗi set")
        }
        setUserList()
    }

    private fun setUserList(){
        try {
            _userList.value=rateListByScore.value?.map { db.getUserByUsersId(it.userID)!! }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }



    fun delete(rate: Rate): Int {
        try {
            val i = db.deleteRate(rate)
            fetch()
            return i
        } catch (e: Exception) {
            e.printStackTrace()
            fetch()
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


class RateViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RateViewModel::class.java)) {
            return RateViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}