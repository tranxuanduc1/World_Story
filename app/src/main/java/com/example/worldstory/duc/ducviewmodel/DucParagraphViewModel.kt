package com.example.worldstory.duc.ducviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass
import com.example.worldstory.duc.ducdataclass.DucParagraphDataClass
import com.example.worldstory.duc.ducrepository.DucDataRepository

class DucParagraphViewModel(var repository: DucDataRepository,var context: Context): ViewModel() {

    fun getOneParagraphByChapter(chapter: DucChapterDataClass, pos:Int): DucParagraphDataClass?{
        var paragraph= SampleDataStory.getListOfParagraph(context).filter{ it.idChapter==chapter.idChapter  }
            .filter { it.position==pos  }.get(0)?:null
        return paragraph
    }
    fun getAllParagraphsByChapter(chapter: DucChapterDataClass?): List<DucParagraphDataClass>{
        var clone=chapter?: SampleDataStory.getOneChapter()
        var paragraphs= SampleDataStory.getListOfParagraph(context).filter { it.idChapter==clone.idChapter }
        return paragraphs
    }
    fun getNextParagraphsByChapter(currentParagraph: DucParagraphDataClass): DucParagraphDataClass?{
        var paragraph= SampleDataStory.getListOfParagraph(context).firstOrNull{
            it.position==currentParagraph.position+1
        }
        return paragraph
    }
}