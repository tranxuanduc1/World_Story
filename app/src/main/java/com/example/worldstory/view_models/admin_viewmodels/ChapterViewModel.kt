package com.example.worldstory.view_models.admin_viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.data.model.Chapter
import com.example.worldstory.data.model.Image
import com.example.worldstory.data.model.Paragraph

class ChapterViewModel(private val db: DatabaseHelper) : ViewModel() {

    val arrID = mutableMapOf<Int, String>()
    val imgMap = mutableMapOf<Int, String>()
    val name = MutableLiveData<String>()


    fun transform(id: String): String {
        return "https://drive.usercontent.google.com/download?id=${id}&export=view"
    }

    fun setImgs() {
        val sortedMap = arrID.toSortedMap()
        sortedMap.forEach { (k, v) ->
            imgMap[k] = transform(v)
        }
        arrID.clear()
    }

    fun getAllImage(): List<Image> {
        return db.getAllImage()
    }

    fun onAddChapter(storyID: Int): Boolean {
        if (storyID != -1) {
            val chapter = Chapter(
                null, name.value.toString(), dateTimeNow(), storyID
            )
            val l: Long = db.insertChapter(chapter)


            if (imgMap.isNotEmpty())
                for (i in imgMap) {
                    val img = Image(null, i.value, i.key, l.toInt())
                    val ll = db.insertImage(img)


                }
            imgMap.clear()
            return true
        }
        imgMap.clear()
        return false
    }

    fun updateContent(chapterID: Int, storyID: Int) {


        val chapter = Chapter(
            chapterID, name.value.toString(), dateTimeNow(), storyID
        )
        db.updateChapter(chapter)
        db.deleteImageByChapterId(chapterID)
        for (i in imgMap) {
            val img = Image(null, i.value, i.key, chapterID)
            db.insertImage(img)
        }

        imgMap.clear()
    }

    fun updateTextContent(chapterID: Int, storyID: Int, content: Map<Int, String>) {

        val chapter = Chapter(
            chapterID, name.value.toString(), dateTimeNow(), storyID
        )
        db.updateChapter(chapter)

        if (content.isNotEmpty()) {
            db.deleteParagraphByChapterId(chapterID)
            for (c in content) {
                val p = Paragraph(null, c.value, c.key, chapterID)
                db.insertParagraph(p)
            }
        }
        imgMap.clear()
    }

    fun onAddTextChapter(storyID: Int, content: Map<Int, String>): Boolean {
        if (storyID != -1) {
            val chapter = Chapter(null, name.value.toString(), dateTimeNow(), storyID)
            val l: Long = db.insertChapter(chapter)

            if (content.isNotEmpty()) {
                for (c in content) {
                    val p = Paragraph(null, c.value, c.key, l.toInt())
                    db.insertParagraph(p)
                }
            }
            return true
        }
        return false
    }


}

class ChapterViewModelFactory(private val databaseHelper: DatabaseHelper) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChapterViewModel::class.java)) {
            return ChapterViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}