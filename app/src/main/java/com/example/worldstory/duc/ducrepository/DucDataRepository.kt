package com.example.worldstory.duc.ducrepository

import android.util.Log
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Story

class DucDataRepository(private var dbHelper: DatabaseHelper) {
    //story
    fun getAllStories(): List<Story> {
        var list= dbHelper.getAllStories()
        return list
    }
   // fun getStoryById(id: Int): Story? = dbHelper.getStoryById(id)
    fun addStory(story: Story): Long = dbHelper.insertStory(story)



    //genre
    fun getAllGenres(): List<Genre>{
        var list =dbHelper.getAllGenres()
        return list
    }

    //chapter
    fun getAllChapter(): List<Chapter>{
        var list=dbHelper.getAllChapters()
        return list
    }
    fun getChaptersByStory(storyId: Int): List<Chapter>{
        var list=dbHelper.getChaptersByStory(storyId)
        return list
    }

    //paragraph
    fun getAllParagraph(): List<Paragraph>{
        var list=dbHelper.getAllParagraphs()
        return list
    }
    fun getParagraphsByChapter(chapterId:Int): List<Paragraph>{
        return dbHelper.getParagraphsByChapter(chapterId)
    }

}