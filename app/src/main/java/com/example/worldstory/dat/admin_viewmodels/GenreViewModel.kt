package com.example.worldstory.dat.admin_viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Genre

class GenreViewModel(private val db: DatabaseHelper) : ViewModel(db) {
    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres
    val genreName = MutableLiveData<String>()

    init {
        fetchAllGenre()
    }

    fun onAddNewGern(): Long {
        val genre = Genre(null, genreName = genreName.value.toString(), 1)
        fetchAllGenre()
        val l = insertGenre(genre)
        genreName.value = ""
        return l
    }

    fun fetchAllGenre() {
        _genres.value = db.getAllGenres()
    }

    fun deleteGenre(genre: Genre): Int {
        val i: Int = db.deleteGenre(genre.genreID)
        fetchAllGenre()
        return i
    }

    fun insertGenre(genre: Genre): Long {
        val l: Long = db.insertGenre(genre)
        fetchAllGenre()
        return l
    }

    fun updateGenre(genre: Genre): Int {
        val i: Int = db.updateGenre(genre)
        fetchAllGenre()
        return i
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