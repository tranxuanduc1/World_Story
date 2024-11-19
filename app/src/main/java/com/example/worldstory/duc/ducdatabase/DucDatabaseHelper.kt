package com.example.worldstory.duc.ducdatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.collection.mutableIntListOf
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.showTestToast

val DATABASE_NAME="ducdb"
val TABLE_NAME="stories"
val COL_ID="id"
val COL_TITLE="title"
val COL_AUTHOR="author"
val COL_DESCRIPTION="description"
val COL_IMAGE_URL ="imageURL"
val COL_BG_IMAGE_URL="backgroundImageURL"
val COL_IS_COMIC="isComic"
val COL_DATECREATED="dateCreated"
val COL_SCORE="score"
val version=2
class DucDatabaseHelper(var context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,version) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val queryCreateTable="""
            create table ${TABLE_NAME} (
            $COL_ID integer primary key autoincrement,
            $COL_TITLE text not null ,
            $COL_AUTHOR text not null,
            $COL_DESCRIPTION text ,
            $COL_IMAGE_URL text not null,
            $COL_BG_IMAGE_URL text not null,
            $COL_DATECREATED date ,
            $COL_IS_COMIC integer not null,
            $COL_SCORE float not null
            )
        """.trimIndent()
        p0?.execSQL(queryCreateTable)
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
    fun insertStory(story: DucStoryDataClass){
        val db = writableDatabase
        val cv= ContentValues()
        cv.put(COL_TITLE,story.title)
        cv.put(COL_AUTHOR,story.author)
        cv.put(COL_DESCRIPTION,story.description)
        cv.put(COL_IMAGE_URL,story.imgURL)
        cv.put(COL_BG_IMAGE_URL,story.backgroundImageURL)
        cv.put(COL_SCORE,story.score)
        cv.put(COL_DATECREATED,story.dateCreate)
        cv.put(COL_IS_COMIC,story.isComic)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result==-1.toLong()){
            showTestToast(context,"error add table")
        }else{
            showTestToast(context,"added table suceed")
        }

    }
    fun getAllStory():List<DucStoryDataClass>{
        val db= readableDatabase
        val cursor: Cursor=db.rawQuery("select * from $TABLE_NAME",null)
        val listStories = mutableListOf<DucStoryDataClass>()
        if(cursor.moveToFirst() ) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("$COL_ID"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("$COL_TITLE"))
                val author = cursor.getString(cursor.getColumnIndexOrThrow("$COL_AUTHOR"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("$COL_DESCRIPTION"))
                val imgURL = cursor.getString(cursor.getColumnIndexOrThrow("$COL_IMAGE_URL"))
                val bgImgURL = cursor.getString(cursor.getColumnIndexOrThrow("$COL_BG_IMAGE_URL"))
                val score = cursor.getFloat(cursor.getColumnIndexOrThrow("$COL_SCORE"))
                val isComic = cursor.getInt(cursor.getColumnIndexOrThrow("$COL_IS_COMIC"))==1
                val dateCreated = cursor.getString(cursor.getColumnIndexOrThrow("$COL_DATECREATED"))
                listStories.add(DucStoryDataClass(id,title,author,description,imgURL,bgImgURL,dateCreated,score,isComic))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listStories




    }
}