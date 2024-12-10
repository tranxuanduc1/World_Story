package com.example.worldstory.duc.ducdataclass

import android.os.Parcelable
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story
import kotlinx.parcelize.Parcelize

@Parcelize
data class DucComboChapterDataClass(
    var preChapter: Chapter?, var mainChapter: Chapter, var nextChapter: Chapter?,
    var story: Story
) : Parcelable
