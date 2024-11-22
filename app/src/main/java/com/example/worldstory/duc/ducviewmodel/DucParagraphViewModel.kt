package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.model.Paragraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DucParagraphViewModel(var repository: DucDataRepository, var context: Context) : ViewModel() {

    private val _paragraphsByChapter = MutableLiveData<List<Paragraph>>()
    val paragraphsByChapter: LiveData<List<Paragraph>> get() = _paragraphsByChapter

    fun setParagraphsByChapter(chapterId: Int) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getParagraphsByChapter(chapterId)
            }

            _paragraphsByChapter.value = result

        }
    }

    fun getExampleParagraph(): Paragraph {
        return Paragraph(1, SampleDataStory.getExampleImgURLParagraph(), 1, 1)
    }

    fun getParagraphsByChapter(chapterId: Int): List<Paragraph> {
        // setParagraphsByChapter(chapterId)
        var paragraphs = _paragraphsByChapter.value ?: listOf()
        return paragraphs
    }
//    fun getNextParagraphsByChapter(currentParagraph: DucParagraphDataClass): DucParagraphDataClass?{
//        var paragraph= SampleDataStory.getListOfParagraph(context).firstOrNull{
//            it.position==currentParagraph.position+1
//        }
//        return paragraph
//    }
}