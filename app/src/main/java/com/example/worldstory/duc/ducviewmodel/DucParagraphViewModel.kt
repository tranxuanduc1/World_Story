package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucParagraphDataClass
import com.example.worldstory.duc.ducrepository.DucDataRepository
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Paragraph

class DucParagraphViewModel(var repository: DucDataRepository,var context: Context): ViewModel() {

    fun getExampleParagraph(): Paragraph{
        return Paragraph(1, SampleDataStory.getExampleImgURLParagraph(),1,1)
    }
    fun getParagraphsByChapter(chapter: Chapter): List<Paragraph>{
        var paragraphs= repository.getAllParagraph().filter { it.chapterID==chapter.chapterID }
        return paragraphs
    }
    fun getNextParagraphsByChapter(currentParagraph: DucParagraphDataClass): DucParagraphDataClass?{
        var paragraph= SampleDataStory.getListOfParagraph(context).firstOrNull{
            it.position==currentParagraph.position+1
        }
        return paragraph
    }
}