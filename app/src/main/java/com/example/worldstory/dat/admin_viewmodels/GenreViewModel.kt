package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Role
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenreViewModel(private val db: DatabaseHelper) : ViewModel(db) {
    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres
    val genreName = MutableLiveData<String>()

    private val tempGenres = mutableListOf<Genre>()

    init {
        fetch()
    }

    fun onAddNewGern(userId: Int): Long {
        val genre = Genre(null, genreName = genreName.value.toString(), userId)
        val l = insertGenre(genre)
        genreName.value = ""
        return l
    }

    fun fetch() {
        tempGenres.clear()
        tempGenres.addAll(db.getAllGenres())
        _genres.value = tempGenres
    }

    fun fetchAllGenre() {
        tempGenres.clear()

                tempGenres.addAll(db.getAllGenres())


            _genres.value = tempGenres
            tempGenres.clear()



    }

    fun getIDbyName(name: String): Int {
        return db.getGenreIDByName(name)
    }

    fun checkExits(name: String): Int {
        return db.checkExistGenreName(name)
    }

    fun deleteGenre(genre: Genre): Int {
        val i: Int = db.deleteGenre(genre.genreID)
        fetch()
        return i
    }

    fun insertGenre(genre: Genre): Long {
        val l: Long = db.insertGenre(genre)
        fetch()
        return l
    }

    fun updateGenre(genre: Genre): Int {
        val i: Int = db.updateGenre(genre)
        fetch()
        return i
    }


    fun deleteGenre(id: Int) {
        db.deleteGenre(id)
        fetch()
    }

    fun sumStoryByGenre(id: Int): Int {
        val set = db.getStoriesIdbyGenreId(id)
        return set.size
    }
}

class GenreViewModelFactory(private val databaseHelper: DatabaseHelper) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenreViewModel::class.java)) {
            return GenreViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}