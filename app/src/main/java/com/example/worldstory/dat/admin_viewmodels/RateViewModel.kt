package com.example.worldstory.dat.admin_viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Rate

class RateViewModel(private val db: DatabaseHelper) : ViewModel(db) {

    private val _rateList = MutableLiveData<List<Rate>>()
    val rateList: LiveData<List<Rate>> get() = _rateList

    init {
        fetch()
    }

    fun fetch() {
        _rateList.value = db.getAllRates()
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