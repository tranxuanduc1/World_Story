package com.example.worldstory.dbhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


object Contract {
    //Story table
    object StoryEntry : BaseColumns {
        const val TABLE_NAME = "story_table"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_BACKGROUND_IMAGE_URL = "background_image_url"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CREATED_DATE = "created_date"
        const val COLUMN_IS_TEXT_STORY = "is_text_story"
        const val COLUMN_SCORE="score"
        // Foreign key
        //const val COLUMN_GENRE_ID_FK = "genre_id"
        const val COLUMN_USER_CREATED_ID_FK = "user_created_id"

    }

    //Genre table
    object GenreEntry : BaseColumns {
        const val TABLE_NAME = "genre_table"
        const val COLUMN_NAME = "name"

        //Foreign key
        const val COLUMN_USER_CREATED_ID_FK = "user_created_id"
    }

    // story have many genres , a genre have many stories
    object StoryGenreEntry: BaseColumns{
        const val TABLE_NAME ="story_genre_table"
        //Foreign key , 2 key nay khong duoc trung voi 2 key khac
        const val COLUMN_STORY_ID_FK = "story_id"
        const val COLUMN_GENRE_ID_FK = "genre_id"
    }
    //User table
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "user_table"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NICKNAME = "nickname"
        const val COLUMN_CREATED_DATE = "created_date"

        //Foreign key
        const val COLUMN_ROLE_ID_FK = "role_id"
    }

    //Role table
    object RoleEntry : BaseColumns {
        const val TABLE_NAME = "role_table"
        const val COLUMN_NAME = "name"
    }

    //user love story table
    object UserLoveStory: BaseColumns{
        const val TABLE_NAME="user_love_story_table"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }
    //Rate table
    object RateEntry : BaseColumns {
        const val TABLE_NAME = "rate_table"
        const val COLUMN_RATE = "rate"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Comment table
    object CommentEntry : BaseColumns {
        const val TABLE_NAME = "comment_table"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TIME = "time"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Chapter table
    object ChapterEntry : BaseColumns {
        const val TABLE_NAME = "chapter_table"
        const val COLUMN_TITLE = "title"

        //Foreign key
        //const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    // user marked chapter table
    object ChapterMarkEntry : BaseColumns{
        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    // user saw chapter
    object ChapterHistoryEntry : BaseColumns{
        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    //Paragraph table
    object ParagraphEntry : BaseColumns {
        const val TABLE_NAME = "paragraph_table"
        const val COLUMN_CONTENT_FILE = "text_url"
        const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    //Imange table
    object ImageEntry : BaseColumns {
        const val TABLE_NAME = "img_table"
        const val COLUMN_CONTENT_FILE = "img_url"
        const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //ReadHistory table
    object ReadHistory : BaseColumns {
        const val TABLE_NAME = "history_read_table"
        const val COLUMN_DATE_TIME = "date_time"

        //Foreign
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }
}

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_doc_truyen_db"
        private const val DATABASE_VERSION = 1
        private const val _ID= BaseColumns._ID

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        var createStoryTable ="""
            create table ${Contract.StoryEntry.TABLE_NAME} (
            ${_ID} integer primary key autoincrement,
            ${Contract.StoryEntry.COLUMN_TITLE} text not null ,            
            ${Contract.StoryEntry.COLUMN_AUTHOR} text not null ,            
            ${Contract.StoryEntry.COLUMN_DESCRIPTION} text not null ,            
            ${Contract.StoryEntry.COLUMN_IMAGE_URL} text not null ,            
            ${Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL} text not null ,            
            ${Contract.StoryEntry.COLUMN_CREATED_DATE} text not null ,            
            ${Contract.StoryEntry.COLUMN_SCORE} text not null ,            
            ${Contract.StoryEntry.COLUMN_IS_TEXT_STORY} integer not null ,            
            foreign key (${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID})  
            )
        """.trimIndent()
        p0?.execSQL(createStoryTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}
//package com.example.worldstory.dbhelper
//
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import android.provider.BaseColumns
//
//
//object Contract {
//    //Story table
//    object StoryEntry : BaseColumns {
//        const val TABLE_NAME = "story_table"
//        const val COLUMN_TITLE = "title"
//        const val COLUMN_DESCRIPTION = "description"
//        const val COLUMN_IMAGE_URL = "image_url"
//        const val COLUMN_AUTHOR = "author"
//        const val COLUMN_CREATED_DATE = "created_date"
//        const val COLUMN_IS_TEXT_STORY = "is_text_story"
//
//        // Foreign key
//        const val COLUMN_GENRE_ID_FK = "genre_id"
//    }
//
//    //Genre table
//    object GenreEntry : BaseColumns {
//        const val TABLE_NAME = "genre_table"
//        const val COLUMN_NAME = "name"
//
//        //Foreign key
//        const val COLUMN_USER_ID_FK = "user_id"
//    }
//
//    //User table
//    object UserEntry : BaseColumns {
//        const val TABLE_NAME = "user_table"
//        const val COLUMN_USERNAME = "username"
//        const val COLUMN_PASSWORD = "password"
//        const val COLUMN_NICKNAME = "nickname"
//        const val COLUMN_CREATED_DATE = "created_date"
//
//        //Foreign key
//        const val COLUMN_ROLE_ID_FK = "role_id"
//    }
//
//    //Role table
//    object RoleEntry : BaseColumns {
//        const val TABLE_NAME = "role_table"
//        const val COLUMN_NAME = "name"
//    }
//
//    //Rate table
//    object RateEntry : BaseColumns {
//        const val TABLE_RATE = "rate_table"
//        const val COLUMN_RATE = "rate"
//
//        //Foreign key
//        const val COLUMN_USER_ID_FK = "user_id"
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//
//    //Comment table
//    object CommentEntry : BaseColumns {
//        const val TABLE_NAME = "comment_table"
//        const val COLUMN_CONTENT = "content"
//        const val COLUMN_TIME = "time"
//
//        //Foreign key
//        const val COLUMN_USER_ID_FK = "user_id"
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//
//    //Chapter table
//    object ChapterEntry : BaseColumns {
//        const val TABLE_NAME = "chapter_table"
//        const val COLUMN_TITLE = "title"
//
//        //Foreign key
//        const val COLUMN_USER_ID_FK = "user_id"
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//
//    //Paragraph table
//    object ParagraphEntry : BaseColumns {
//        const val TABLE_NAME = "paragraph_table"
//        const val COLUMN_CONTENT_FILE = "text_url"
//        const val COLUMN_NUMBER_ORDER = "num_order"
//
//        //Foreign key
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//
//    //Imange table
//    object ImageEntry:BaseColumns{
//        const val TABLE_NAME = "img_table"
//        const val COLUMN_CONTENT_FILE = "img_url"
//        const val COLUMN_NUMBER_ORDER = "num_order"
//
//        //Foreign key
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//
//    //ReadHistory table
//    object ReadHistory:BaseColumns{
//        const val TABLE_NAME="history_read_table"
//        const val COLUMN_DATE_TIME="date_time"
//        //Foreign
//        const val COLUMN_USER_ID_FK = "user_id"
//        const val COLUMN_STORY_ID_FK = "story_id"
//    }
//}
//
//class DatabaseHelper(context: Context) :
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        private const val DATABASE_NAME = "app_doc_truyen_db"
//        private const val DATABASE_VERSION = 1
//
//    }
//
//    override fun onCreate(p0: SQLiteDatabase?) {
//        var a ="${Contract.ReadHistory.TABLE_NAME}"
//
//    }
//
//    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        TODO("Not yet implemented")
//    }
//}