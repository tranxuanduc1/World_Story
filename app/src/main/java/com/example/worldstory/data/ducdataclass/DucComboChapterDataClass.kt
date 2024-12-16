package com.example.worldstory.data.ducdataclass

import android.os.Parcelable
import com.example.worldstory.data.model.Chapter
import com.example.worldstory.data.model.Story
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucComboChapterDataClass(
    var preChapter: Chapter?, var mainChapter: Chapter, var nextChapter: Chapter?,
    var story: Story
) : Parcelable
