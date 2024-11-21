package com.example.worldstory.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

import com.example.worldstory.dbhelper.Contract.CommentEntry
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Rate
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User


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
        const val COLUMN_SCORE = "score"

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
    object StoryGenreEntry : BaseColumns {
        const val TABLE_NAME = "story_genre_table"

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
        const val COLUMN_IMAGE_AVATAR = "imgAvatar"
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
    object UserLoveStory : BaseColumns {
        const val TABLE_NAME = "user_love_story_table"

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
        const val COLUMN_DATE_CREATED="date_created"
        //Foreign key
        //const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    // user marked chapter table
    object ChapterMarkEntry : BaseColumns {
        const val TABLE_NAME = "chaptermark_table"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
    }

    // user saw chapter
    object ChapterHistoryEntry : BaseColumns {
        const val TABLE_NAME = "chapter_history_table"

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
        const val COLUMN_CHAPTER_ID_FK = "chapter_id"
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
        private const val _ID = BaseColumns._ID

    }

    override fun onCreate(p0: SQLiteDatabase?) {

        var createStoryTable = """
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
            ${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK} integer not null ,    
            foreign key (${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID})  
            )
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createUserTable = """
            create table ${Contract.UserEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.UserEntry.COLUMN_USERNAME} text not null,
            ${Contract.UserEntry.COLUMN_PASSWORD} text not null,
            ${Contract.UserEntry.COLUMN_NICKNAME} text not null,
            ${Contract.UserEntry.COLUMN_IMAGE_AVATAR} text not null,
            ${Contract.UserEntry.COLUMN_CREATED_DATE} text not null,
            ${Contract.UserEntry.COLUMN_ROLE_ID_FK} integer not null,
            foreign key (${Contract.UserEntry.COLUMN_ROLE_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////

        var createGenreTable = """
            create table ${Contract.GenreEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.GenreEntry.COLUMN_NAME} text not null,
            ${Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK} integer not null,
            foreign key (${Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK}) references ${Contract.UserEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createStoryGernTable = """
            create table ${Contract.StoryGenreEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
             ${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK} integer not null,
             ${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK} integer not null,
            foreign key (${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK}) references ${Contract.StoryEntry.TABLE_NAME}(${_ID}),
            foreign key (${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK}) references ${Contract.GenreEntry.TABLE_NAME}(${_ID}))
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        var createRoleTable = """
            create table ${Contract.RoleEntry.TABLE_NAME}(
            ${_ID} integer primary key autoincrement,
            ${Contract.RoleEntry.COLUMN_NAME} text not null)
        """.trimIndent()
        //////////////////////////
        //////////////////////////
        val createUserLoveStoryTable = """
    CREATE TABLE ${Contract.UserLoveStory.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.UserLoveStory.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.UserLoveStory.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.UserLoveStory.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.UserLoveStory.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createRateTable = """
    CREATE TABLE ${Contract.RateEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.RateEntry.COLUMN_RATE} INTEGER NOT NULL,
        ${Contract.RateEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.RateEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.RateEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.RateEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createCommentTable = """
    CREATE TABLE ${CommentEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${CommentEntry.COLUMN_CONTENT} TEXT NOT NULL,
        ${CommentEntry.COLUMN_TIME} TEXT NOT NULL,
        ${CommentEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${CommentEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${CommentEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${CommentEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterTable = """
    CREATE TABLE ${Contract.ChapterEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterEntry.COLUMN_TITLE} TEXT NOT NULL,
        ${Contract.ChapterEntry.COLUMN_DATE_CREATED} TEXT NOT NULL,
        ${Contract.ChapterEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterMarkTable = """
    CREATE TABLE ${Contract.ChapterMarkEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterMarkEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterMarkEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createChapterHistoryTable = """
    CREATE TABLE ${Contract.ChapterHistoryEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createParagraphTable = """
    CREATE TABLE ${Contract.ParagraphEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ParagraphEntry.COLUMN_CONTENT_FILE} TEXT NOT NULL,
        ${Contract.ParagraphEntry.COLUMN_NUMBER_ORDER} INTEGER NOT NULL,
        ${Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createImageTable = """
    CREATE TABLE ${Contract.ImageEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ImageEntry.COLUMN_CONTENT_FILE} TEXT NOT NULL,
        ${Contract.ImageEntry.COLUMN_NUMBER_ORDER} INTEGER NOT NULL,
        ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK}) REFERENCES ${Contract.ChapterEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        val createReadHistoryTable = """
    CREATE TABLE ${Contract.ReadHistory.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Contract.ReadHistory.COLUMN_DATE_TIME} TEXT NOT NULL,
        ${Contract.ReadHistory.COLUMN_USER_ID_FK} INTEGER NOT NULL,
        ${Contract.ReadHistory.COLUMN_STORY_ID_FK} INTEGER NOT NULL,
        FOREIGN KEY (${Contract.ReadHistory.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}),
        FOREIGN KEY (${Contract.ReadHistory.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID})
    )
""".trimIndent()
        //////////////////////////
        //////////////////////////
        p0?.execSQL(createRoleTable)
        p0?.execSQL(createUserTable)
        p0?.execSQL(createStoryTable)
        p0?.execSQL(createGenreTable)
        p0?.execSQL(createChapterTable)
        p0?.execSQL(createParagraphTable)
        p0?.execSQL(createReadHistoryTable)
        p0?.execSQL(createImageTable)
        p0?.execSQL(createChapterHistoryTable)
        p0?.execSQL(createChapterMarkTable)
        p0?.execSQL(createCommentTable)
        p0?.execSQL(createRateTable)
        p0?.execSQL(createUserLoveStoryTable)
        p0?.execSQL(createStoryGernTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    //////////////////////////
    ///----   STORY  -----////
    //////////////////////////

    fun insertStory(
        story: Story
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.StoryEntry.COLUMN_TITLE, story.title)
            put(Contract.StoryEntry.COLUMN_AUTHOR, story.author)
            put(Contract.StoryEntry.COLUMN_DESCRIPTION, story.description)
            put(Contract.StoryEntry.COLUMN_IMAGE_URL, story.imgUrl)
            put(Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL, story.bgImgUrl)
            put(Contract.StoryEntry.COLUMN_CREATED_DATE, story.createdDate)
            put(Contract.StoryEntry.COLUMN_SCORE, story.score)
            put(Contract.StoryEntry.COLUMN_IS_TEXT_STORY, story.isTextStory)
            put(Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK, story.userID)

        }

        return db.insert(Contract.StoryEntry.TABLE_NAME, null, values)
    }

    fun getAllStories(): List<Story> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("select * from ${Contract.StoryEntry.TABLE_NAME}", null)

        val stories = mutableListOf<Story>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_TITLE))
                val author =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_AUTHOR))
                val description =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_DESCRIPTION))
                val imgURL =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_IMAGE_URL))
                val bgImgURL =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL))
                val score =
                    cursor.getFloat(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_SCORE))
                val isTextStory =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_IS_TEXT_STORY))
                val dateCreated =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_CREATED_DATE))
                val userID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK))

                stories.add(
                    Story(
                        id,
                        title,
                        author,
                        description,
                        imgURL,
                        bgImgURL,
                        isTextStory,
                        dateCreated,
                        score,
                        userID
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return stories
    }

    fun updateStory(story: Story): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.StoryEntry.COLUMN_TITLE, story.title)
            put(Contract.StoryEntry.COLUMN_AUTHOR, story.author)
            put(Contract.StoryEntry.COLUMN_DESCRIPTION, story.description)
            put(Contract.StoryEntry.COLUMN_IMAGE_URL, story.imgUrl)
            put(Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL, story.bgImgUrl)
            put(Contract.StoryEntry.COLUMN_IS_TEXT_STORY, story.isTextStory)
        }
        return db.update(
            Contract.StoryEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(story.storyID.toString())
        )
    }

    fun deleteStory(storyId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.StoryEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(storyId.toString())
        )
    }

    //////////////////////////
    ///----  CHAPTER -----////
    //////////////////////////

    fun insertChapter(chapter: Chapter): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ChapterEntry.COLUMN_TITLE, chapter.title)
            put(Contract.ChapterEntry.COLUMN_STORY_ID_FK, chapter.storyID)
            put(Contract.ChapterEntry.COLUMN_DATE_CREATED,chapter.dateCreated)
        }
        return db.insert(Contract.ChapterEntry.TABLE_NAME, null, values)
    }


    fun deleteChapter(chapterID: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.ChapterEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(chapterID.toString())
        )
    }

    fun updateChapter(chapter: Chapter): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ChapterEntry.COLUMN_TITLE, chapter.title)
        }
        return db.update(
            Contract.ChapterEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(chapter.chapterID.toString())
        )
    }

    fun getAllChapters(): List<Chapter> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ChapterEntry.TABLE_NAME}", null)
        val chapters = mutableListOf<Chapter>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_TITLE))
                val storyID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_STORY_ID_FK))
                val dateCreate =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_DATE_CREATED))

                chapters.add(Chapter(id, title, dateCreate,storyID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return chapters
    }

    fun getChaptersByStory(storyId: Int): List<Chapter> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT * FROM ${Contract.ChapterEntry.TABLE_NAME}
            WHERE ${Contract.ChapterEntry.COLUMN_STORY_ID_FK} = ?
            """.trimIndent(),
            arrayOf(storyId.toString())
        )
        val chapters = mutableListOf<Chapter>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_TITLE))
                val storyID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_STORY_ID_FK))
                val dateCreate =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChapterEntry.COLUMN_DATE_CREATED))

                chapters.add(Chapter(id, title, dateCreate,storyID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return chapters
    }

    //////////////////////////
    ///---- PARAGRAPH-----////
    //////////////////////////

    fun insertParagraph(paragraph: Paragraph): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ParagraphEntry.COLUMN_CONTENT_FILE, paragraph.content)
            put(Contract.ParagraphEntry.COLUMN_NUMBER_ORDER, paragraph.order)
            put(Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK, paragraph.chapterID)
        }
        return db.insert(Contract.ParagraphEntry.TABLE_NAME, null, values)
    }

    fun deleteParagraph(paragraphID: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.ParagraphEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(paragraphID.toString())
        )
    }

    fun updateParagraph(paragraph: Paragraph): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ParagraphEntry.COLUMN_CONTENT_FILE, paragraph.content)
        }
        return db.update(
            Contract.ParagraphEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(paragraph.paragraphID.toString())
        )
    }

    fun getAllParagraphs(): List<Paragraph> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ParagraphEntry.TABLE_NAME}", null)
        val paragraphs = mutableListOf<Paragraph>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val content =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ParagraphEntry.COLUMN_CONTENT_FILE))
                val order =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ParagraphEntry.COLUMN_NUMBER_ORDER))
                val chapterID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK))
                paragraphs.add(Paragraph(id, content, order, chapterID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return paragraphs
    }


    //////////////////////////
    ///----   USER   -----////
    //////////////////////////

    fun insertUser(user: User): Long {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.UserEntry.COLUMN_USERNAME, user.userName)
            put(Contract.UserEntry.COLUMN_PASSWORD, user.hashedPw)
            put(Contract.UserEntry.COLUMN_NICKNAME, user.nickName)
            put(Contract.UserEntry.COLUMN_IMAGE_AVATAR, user.imgAvatar)
            put(Contract.UserEntry.COLUMN_ROLE_ID_FK, user.roleID)
            put(Contract.UserEntry.COLUMN_CREATED_DATE, user.createdDate)
        }
        return db.insert(Contract.UserEntry.TABLE_NAME, null, values)
    }

    fun deleteUser(userID: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.UserEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(userID.toString())
        )
    }

    fun updateUser(user: User): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.UserEntry.COLUMN_PASSWORD, user.hashedPw)
            put(Contract.UserEntry.COLUMN_NICKNAME, user.nickName)
            put(Contract.UserEntry.COLUMN_IMAGE_AVATAR, user.imgAvatar)
            put(Contract.UserEntry.COLUMN_ROLE_ID_FK, user.roleID)
        }
        return db.update(
            Contract.UserEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(user.userID.toString())
        )
    }

    fun deleteAllUser() {
        val db = writableDatabase
        db.execSQL("DELETE FROM ${Contract.UserEntry.TABLE_NAME}")
    }

    fun getAllUsers(): List<User> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.UserEntry.TABLE_NAME}", null)
        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val userName =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_USERNAME))
                val hashedPw =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_PASSWORD))
                val nickName =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_NICKNAME))
                val imgAvatar =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_IMAGE_AVATAR))
                val roleID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_ROLE_ID_FK))
                val createdDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_CREATED_DATE))
                users.add(User(id, userName, hashedPw, imgAvatar, nickName, roleID, createdDate))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }


    //////////////////////////
    ///----   GENRE   -----////
    //////////////////////////
    fun insertGenre(genre: Genre): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.GenreEntry.COLUMN_NAME, genre.genreName)
            put(Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK, genre.userID)
        }
        return db.insert(Contract.GenreEntry.TABLE_NAME, null, values)
    }

    fun deleteGenre(genreId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.GenreEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(genreId.toString())
        )
    }

    fun updateGenre(genre: Genre): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.GenreEntry.COLUMN_NAME, genre.genreName)
        }
        return db.update(
            Contract.GenreEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(genre.genreID.toString())
        )
    }

    fun getAllGenres(): List<Genre> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.GenreEntry.TABLE_NAME}", null)
        val genres = mutableListOf<Genre>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.GenreEntry.COLUMN_NAME))
                val userID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK))
                genres.add(Genre(id, name, userID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return genres
    }

    //////////////////////////
    ///----   ROLE   -----////
    //////////////////////////
    fun insertRole(role: Role): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.RoleEntry.COLUMN_NAME, role.roleName)
        }
        return db.insert(Contract.RoleEntry.TABLE_NAME, null, values)
    }

    fun deleteRole(roleID: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.RoleEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(roleID.toString())
        )
    }

    fun deleteAllRole() {
        val db = writableDatabase
        db.execSQL("DELETE FROM ${Contract.RoleEntry.TABLE_NAME}")
    }

    fun updateRoleName(role: Role): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.RoleEntry.COLUMN_NAME, role.roleName)
        }
        return db.update(
            Contract.RoleEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(role.roleID.toString())
        )
    }

    fun getAllRoles(): List<Role> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.RoleEntry.TABLE_NAME}", null)
        val roles = mutableListOf<Role>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.RoleEntry.COLUMN_NAME))
                roles.add(Role(id, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return roles
    }


    //////////////////////////
    ///---UserLoveStory---////
    //////////////////////////
    fun insertUserLoveStory(userId: Int, storyId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.UserLoveStory.COLUMN_USER_ID_FK, userId)
            put(Contract.UserLoveStory.COLUMN_STORY_ID_FK, storyId)
        }
        return db.insert(Contract.UserLoveStory.TABLE_NAME, null, values)
    }

    fun deleteUserLoveStory(userId: Int, storyId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.UserLoveStory.TABLE_NAME,
            "${Contract.UserLoveStory.COLUMN_USER_ID_FK} = ? AND ${Contract.UserLoveStory.COLUMN_STORY_ID_FK} = ?",
            arrayOf(userId.toString(), storyId.toString())
        )
    }

    fun getAllUserLoveStories(): List<Pair<Int, Int>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.UserLoveStory.TABLE_NAME}", null)
        val userLoveStories = mutableListOf<Pair<Int, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.UserLoveStory.COLUMN_USER_ID_FK))
                val storyId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.UserLoveStory.COLUMN_STORY_ID_FK))
                userLoveStories.add(Pair(userId, storyId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userLoveStories
    }


    //////////////////////////
    ///----  Rate    -----////
    //////////////////////////

    fun insertRate(rate: Rate): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.RateEntry.COLUMN_USER_ID_FK, rate.userID)
            put(Contract.RateEntry.COLUMN_STORY_ID_FK, rate.storyID)
            put(Contract.RateEntry.COLUMN_RATE, rate.score)
        }
        return db.insert(Contract.RateEntry.TABLE_NAME, null, values)
    }

    fun deleteRate(rate: Rate): Int {
        val db = writableDatabase
        return db.delete(
            Contract.RateEntry.TABLE_NAME,
            "${Contract.RateEntry.COLUMN_USER_ID_FK} = ? AND ${Contract.RateEntry.COLUMN_STORY_ID_FK} = ?",
            arrayOf(rate.userID.toString(), rate.storyID.toString())
        )
    }

    fun updateRate(rate: Rate): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.RateEntry.COLUMN_RATE, rate.score)
        }
        return db.update(
            Contract.RateEntry.TABLE_NAME,
            values,
            "${Contract.RateEntry.COLUMN_USER_ID_FK} = ? AND ${Contract.RateEntry.COLUMN_STORY_ID_FK} = ?",
            arrayOf(rate.userID.toString(), rate.storyID.toString())
        )
    }

    fun getAllRates(): List<Rate> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.RateEntry.TABLE_NAME}", null)
        val rates = mutableListOf<Rate>()

        if (cursor.moveToFirst()) {
            do {
                val rateId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.RateEntry.COLUMN_USER_ID_FK))
                val storyId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.RateEntry.COLUMN_STORY_ID_FK))
                val score =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.RateEntry.COLUMN_RATE))
                rates.add(Rate(rateId, score, userId, storyId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return rates
    }


    //////////////////////////
    ///----  Comment -----////
    //////////////////////////

    fun insertComment(comment: Comment): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.CommentEntry.COLUMN_USER_ID_FK, comment.userId)
            put(Contract.CommentEntry.COLUMN_STORY_ID_FK, comment.storyId)
            put(Contract.CommentEntry.COLUMN_CONTENT, comment.content)
            put(Contract.CommentEntry.COLUMN_TIME, comment.time)
        }
        return db.insert(Contract.CommentEntry.TABLE_NAME, null, values)
    }

    fun deleteComment(commentId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.CommentEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(commentId.toString())
        )
    }

    fun updateComment(comment: Comment): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.CommentEntry.COLUMN_CONTENT, comment.content)
        }
        return db.update(
            Contract.CommentEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(comment.commentId.toString())
        )
    }

    fun getAllComments(): List<Comment> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.CommentEntry.TABLE_NAME}", null)
        val comments = mutableListOf<Comment>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val content =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.CommentEntry.COLUMN_CONTENT))
                val time =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.CommentEntry.COLUMN_TIME))
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.CommentEntry.COLUMN_USER_ID_FK))
                val storyId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.CommentEntry.COLUMN_STORY_ID_FK))
                comments.add(Comment(id, content, time, userId, storyId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return comments
    }


    //////////////////////////
    ///----ChapterMark-----////
    //////////////////////////
    fun insertChapterMark(userId: Int, chapterId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ChapterMarkEntry.COLUMN_USER_ID_FK, userId)
            put(Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK, chapterId)
        }
        return db.insert(Contract.ChapterMarkEntry.TABLE_NAME, null, values)
    }

    fun deleteChapterMark(userId: Int, chapterId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.ChapterMarkEntry.TABLE_NAME,
            "${Contract.ChapterMarkEntry.COLUMN_USER_ID_FK} = ? AND ${Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK} = ?",
            arrayOf(userId.toString(), chapterId.toString())
        )
    }

    fun getAllChapterMarks(): List<Pair<Int, Int>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ChapterMarkEntry.TABLE_NAME}", null)
        val chapterMarks = mutableListOf<Pair<Int, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterMarkEntry.COLUMN_USER_ID_FK))
                val chapterId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterMarkEntry.COLUMN_CHAPTER_ID_FK))
                chapterMarks.add(Pair(userId, chapterId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return chapterMarks
    }


    //////////////////////////
    ///----ChapterHistory-----////
    //////////////////////////

    fun insertChapterHistory(userId: Int, chapterId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK, userId)
            put(Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK, chapterId)
        }
        return db.insert(Contract.ChapterHistoryEntry.TABLE_NAME, null, values)
    }

    fun deleteChapterHistory(userId: Int, chapterId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.ChapterHistoryEntry.TABLE_NAME,
            "${Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK} = ? AND ${Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK} = ?",
            arrayOf(userId.toString(), chapterId.toString())
        )
    }

    fun getAllChapterHistories(): List<Pair<Int, Int>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ChapterHistoryEntry.TABLE_NAME}", null)
        val chapterHistories = mutableListOf<Pair<Int, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val userId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterHistoryEntry.COLUMN_USER_ID_FK))
                val chapterId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChapterHistoryEntry.COLUMN_CHAPTER_ID_FK))
                chapterHistories.add(Pair(userId, chapterId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return chapterHistories
    }

    //////////////////////////
    ///---- Image     -----////
    //////////////////////////

    fun insertImage(img: Image): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ImageEntry.COLUMN_CONTENT_FILE, img.imgFilePath)
            put(Contract.ImageEntry.COLUMN_NUMBER_ORDER, img.order)
            put(Contract.ImageEntry.COLUMN_CHAPTER_ID_FK, img.chapterID)
        }
        return db.insert(Contract.ImageEntry.TABLE_NAME, null, values)
    }

    fun deleteImage(ImageID: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.ImageEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(ImageID.toString())
        )
    }

    fun updateImage(img: Image): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.ParagraphEntry.COLUMN_CONTENT_FILE, img.imgFilePath)
        }
        return db.update(
            Contract.ImageEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(img.imgID.toString())
        )
    }

    fun getAllImage(): List<Image> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Contract.ImageEntry.TABLE_NAME}", null)
        val imgs = mutableListOf<Image>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val content =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ImageEntry.COLUMN_CONTENT_FILE))
                val order =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ImageEntry.COLUMN_NUMBER_ORDER))
                val chapterID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ImageEntry.COLUMN_CHAPTER_ID_FK))
                imgs.add(Image(id, content, order, chapterID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return imgs
    }


    //////////////////////////
    ///----StoryGenre-----////
    //////////////////////////
// Hàm thêm dữ liệu vào StoryGenreEntry
    fun insertStoryGenre(storyId: Int, genreId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Contract.StoryGenreEntry.COLUMN_STORY_ID_FK, storyId)
            put(Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK, genreId)
        }
        return db.insert(Contract.StoryGenreEntry.TABLE_NAME, null, values)
    }

    // Hàm xóa dữ liệu trong StoryGenreEntry
    fun deleteStoryGenre(storyId: Int, genreId: Int): Int {
        val db = writableDatabase
        return db.delete(
            Contract.StoryGenreEntry.TABLE_NAME,
            "${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK} = ? AND ${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK} = ?",
            arrayOf(storyId.toString(), genreId.toString())
        )
    }


    //////////////////////////
    ///----      -----////
    //////////////////////////

}
