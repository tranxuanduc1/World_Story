package com.example.worldstory.duc.ducrepository

import android.util.Log
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Story

class DucDataRepository(private var dbHelper: DatabaseHelper) {
    fun getAllStories(): List<Story> {
        var list= dbHelper.getAllStories()

        return list
    }
   // fun getStoryById(id: Int): Story? = dbHelper.getStoryById(id)
    fun addStory(story: Story): Long = dbHelper.insertStory(story)
}