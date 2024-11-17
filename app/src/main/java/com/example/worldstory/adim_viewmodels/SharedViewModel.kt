package com.example.worldstory.adim_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val add = MutableLiveData<Boolean?>()
    private val search = MutableLiveData<Boolean?>()
    private val filterBtn = MutableLiveData<Boolean?>()
    val recyclerViewStateStory = MutableLiveData<RecyclerViewState>()
    val searchQueryStory = MutableLiveData<String>()
    val recyclerViewStateCategory = MutableLiveData<RecyclerViewState>()
    private val selectedChips = mutableSetOf<String>()
    val _add: LiveData<Boolean?> get() = add
    val _search: LiveData<Boolean?> get() = search
    val _filterBtn: LiveData<Boolean?> get() = filterBtn
    val _selectedChips: MutableSet<String> get() = selectedChips

    //////////
    fun addSeclectedChip(text: String) {
        selectedChips.add(text)
    }

    fun delSelectedChip(text: String) {
        selectedChips.remove(text)
    }

    fun isContainChip(text: String): Boolean {
        return selectedChips.contains(text)
    }


    //
    //
    fun onFilterBtnClicked() {
        filterBtn.value = true
    }

    fun filterBtnHandled() {
        filterBtn.value = null
    }

    //
    //
    fun onAddButtonClicked() {
        add.value = true
    }

    fun addHandled() {
        add.value = null
    }

    //
    //
    fun onSearch() {
        search.value = true
    }

    fun searchHandle() {
        search.value = null
    }
    /////////
}

data class RecyclerViewState(val firstVisibleItemPosition: Int, val offset: Int)
