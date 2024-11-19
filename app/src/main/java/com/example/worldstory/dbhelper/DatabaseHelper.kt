package com.example.worldstory.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.room.util.getColumnIndexOrThrow
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.User
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story


object Contract {
    //Story table
    object StoryEntry : BaseColumns {
        const val TABLE_NAME = "story_table"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CREATED_DATE = "created_date"
        const val COLUMN_IS_TEXT_STORY = "is_text_story"

        // Foreign key
        const val COLUMN_USER_ID_FK = "owner_id"
        const val COLUMN_GENRE_ID_FK = "genre_id"
    }

    //Story_Genre table
    object StoryGenreEntry : BaseColumns {
        const val TABLE_NAME = "story_genre_table"

        //Foreign key
        const val COLUMN_GENRE_ID_FK = "genre_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Genre table
    object GenreEntry : BaseColumns {
        const val TABLE_NAME = "genre_table"
        const val COLUMN_NAME = "name"

        //Foreign key
        const val COLUMN_USER_ID_FK = "user_id"
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

    //Rate table
    object RateEntry : BaseColumns {
        const val TABLE_RATE = "rate_table"
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
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_STORY_ID_FK = "story_id"
    }

    //Paragraph table
    object ParagraphEntry : BaseColumns {
        const val TABLE_NAME = "paragraph_table"
        const val COLUMN_CONTENT_FILE = "text_url"
        const val COLUMN_NUMBER_ORDER = "num_order"

        //Foreign key
        const val COLUMN_STORY_ID_FK = "story_id"
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
        const val DATABASE_NAME = "app_doc_truyen_db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Tạo bảng user
        val createUserTable = "CREATE TABLE ${Contract.UserEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.UserEntry.COLUMN_CREATED_DATE} TEXT," +
                "${Contract.UserEntry.COLUMN_NICKNAME} TEXT," +
                "${Contract.UserEntry.COLUMN_PASSWORD} TEXT NOT NULL," +
                "${Contract.UserEntry.COLUMN_USERNAME} TEXT NOT NULL UNIQUE," +
                "${Contract.UserEntry.COLUMN_ROLE_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.UserEntry.COLUMN_ROLE_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID})) "
        // Tạo bảng role
        val createRoleTable = "CREATE TABLE ${Contract.RoleEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.RoleEntry.COLUMN_NAME} TEXT NOT NULL)"
        // Tạo bảng story
        val createStoryTable = "CREATE TABLE ${Contract.StoryEntry.TABLE_NAME}(" +
                "${Contract.StoryEntry.COLUMN_TITLE} TEXT," +
                "${Contract.StoryEntry.COLUMN_AUTHOR} TEXT," +
                "${Contract.StoryEntry.COLUMN_CREATED_DATE} TEXT," +
                "${Contract.StoryEntry.COLUMN_DESCRIPTION} TEXT," +
                "${Contract.StoryEntry.COLUMN_IMAGE_URL} TEXT," +
                "${Contract.StoryEntry.COLUMN_IS_TEXT_STORY} INTEGER NOT NULL," +
                "${Contract.StoryEntry.COLUMN_GENRE_ID_FK} INTEGER NOT NULL," +
                "${Contract.StoryEntry.COLUMN_GENRE_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.StoryEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.GenreEntry.TABLE_NAME}(${BaseColumns._ID}), " +
                "FOREIGN KEY(${Contract.StoryEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID})) "
        // Tạo bảng genre
        val createGenreTable = "CREATE TABLE ${Contract.GenreEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.GenreEntry.COLUMN_NAME} TEXT," +
                "${Contract.GenreEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.GenreEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID}))"
        // Tạo bảng chapter
        val createChapterTable = "CREATE TABLE ${Contract.ChapterEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.ChapterEntry.COLUMN_TITLE} TEXT," +
                "${Contract.ChapterEntry.COLUMN_USER_ID_FK} INTEGER NOT NULL," +
                "${Contract.ChapterEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.ChapterEntry.COLUMN_USER_ID_FK}) REFERENCES ${Contract.UserEntry.TABLE_NAME}(${BaseColumns._ID})," +
                "FOREIGN KEY(${Contract.ChapterEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID}) ON DELETE CASCADE)"
        // Tạo bảng paragraph
        val createParagraphTable = "CREATE TABLE ${Contract.ParagraphEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.ParagraphEntry.COLUMN_CONTENT_FILE} TEXT," +
                "${Contract.ParagraphEntry.COLUMN_NUMBER_ORDER} INTEGER," +
                "${Contract.ParagraphEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.ParagraphEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID}) ON DELETE CASCADE)"
        // Tạo bảng image
        val createImageTable = "CREATE TABLE ${Contract.ParagraphEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.ImageEntry.COLUMN_CONTENT_FILE} TEXT," +
                "${Contract.ImageEntry.COLUMN_NUMBER_ORDER} INTEGER," +
                "${Contract.ImageEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.ImageEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID}) ON DELETE CASCADE)"
        // Tạo bảng story_genre
        val createStoryGenre = "CREATE TABLE ${Contract.StoryGenreEntry.TABLE_NAME}(" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK} INTEGER NOT NULL," +
                "${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK} INTEGER NOT NULL," +

                "FOREIGN KEY(${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK}) REFERENCES ${Contract.GenreEntry.TABLE_NAME}(${BaseColumns._ID})," +
                "FOREIGN KEY(${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK}) REFERENCES ${Contract.StoryEntry.TABLE_NAME}(${BaseColumns._ID}) ON DELETE CASCADE)"
        db?.execSQL(createUserTable)
        db?.execSQL(createGenreTable)
        db?.execSQL(createRoleTable)
        db?.execSQL(createStoryTable)
        db?.execSQL(createChapterTable)
        db?.execSQL(createParagraphTable)
        db?.execSQL(createImageTable)
        db?.execSQL(createStoryGenre)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

// các hàm thêm hàng

    fun insertRole(roles: Role): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.RoleEntry.COLUMN_NAME, roles.roleName)
        }
        return db.insert(Contract.RoleEntry.TABLE_NAME, null, values)
    }

    fun insertUser(users: User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.UserEntry.COLUMN_USERNAME, users.userName)
            put(Contract.UserEntry.COLUMN_PASSWORD, users.hashedPw)
            put(Contract.UserEntry.COLUMN_NICKNAME, users.nickName)
            put(Contract.UserEntry.COLUMN_CREATED_DATE, users.createdDate)
        }
        return db.insert(Contract.UserEntry.TABLE_NAME, null, values)
    }

    fun insertGenre(genre: Genre): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.GenreEntry.COLUMN_NAME, genre.genreName)
            put(Contract.GenreEntry.COLUMN_USER_ID_FK, genre.userID)
        }
        return db.insert(Contract.GenreEntry.TABLE_NAME, null, values)
    }

    fun insertStory(story: Story): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.StoryEntry.COLUMN_TITLE, story.title)
            put(Contract.StoryEntry.COLUMN_AUTHOR, story.tacGia)
            put(Contract.StoryEntry.COLUMN_IS_TEXT_STORY, story.isTextStory)
            put(Contract.StoryEntry.COLUMN_DESCRIPTION, story.description)
            put(Contract.StoryEntry.COLUMN_IMAGE_URL, story.imgbg)
            put(Contract.StoryEntry.COLUMN_GENRE_ID_FK, story.genreID)
            put(Contract.StoryEntry.COLUMN_USER_ID_FK, story.userID)
        }
        return db.insert(Contract.StoryEntry.TABLE_NAME, null, values)
    }

    fun insertChapter(chapters: Chapter): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.ChapterEntry.COLUMN_TITLE, chapters.title)
            put(Contract.ChapterEntry.COLUMN_STORY_ID_FK, chapters.storyID)
            put(Contract.ChapterEntry.COLUMN_USER_ID_FK, chapters.userID)
        }
        return db.insert(Contract.UserEntry.TABLE_NAME, null, values)
    }

    fun insertImage(img: Image): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.ImageEntry.COLUMN_CONTENT_FILE, img.imgFilePath)
            put(Contract.ImageEntry.COLUMN_NUMBER_ORDER, img.order)
            put(Contract.ImageEntry.COLUMN_STORY_ID_FK, img.storyID)
        }
        return db.insert(Contract.ImageEntry.TABLE_NAME, null, values)
    }

    fun insertParagraph(p: Paragraph): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.ParagraphEntry.COLUMN_CONTENT_FILE, p.content)
            put(Contract.ParagraphEntry.COLUMN_NUMBER_ORDER, p.order)
            put(Contract.ParagraphEntry.COLUMN_STORY_ID_FK, p.storyID)
        }
        return db.insert(Contract.ParagraphEntry.TABLE_NAME, null, values)
    }

    fun insertStoryGenre(storyID: Int, genreID: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Contract.StoryGenreEntry.COLUMN_STORY_ID_FK, storyID)
            put(Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK, genreID)
        }
        return db.insert(Contract.StoryGenreEntry.TABLE_NAME, null, values)
    }

    // các hàm xóa
    fun delRole(roles: Role) {

        val db = this.writableDatabase
    }

    fun delUser(vararg users: User) {
        val db = this.writableDatabase
    }

    fun delGenre(vararg genre: Genre) {

    }

    fun delStory(vararg story: Story) {

    }

    fun delChapter(vararg chapters: Chapter) {

    }

    fun delImage(vararg imgs: Image) {

    }

    fun delParagraph(vararg p: Paragraph) {

    }

    // các hàm get
    fun readAllStories(): List<Story> {
        val storyList = mutableListOf<Story>()
        val db = readableDatabase
        val query = "SELECT * FROM ${Contract.StoryEntry.TABLE_NAME}"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_TITLE))
                val author =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_AUTHOR))
                val createdDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_CREATED_DATE))
                val description =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_DESCRIPTION))
                val imageUrl =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_IMAGE_URL))
                val isTextStory =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_IS_TEXT_STORY))
                val userID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_USER_ID_FK))
                val genreID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.StoryEntry.COLUMN_GENRE_ID_FK))

                val story = Story(
                    title = title,
                    tacGia = author,
                    createdDate = createdDate,
                    description = description,
                    imgbg = imageUrl,
                    isTextStory = isTextStory,
                    userID = userID,
                    genreID = genreID
                )
                storyList.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return storyList
    }

    fun readAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = readableDatabase
        val query = "SELECT * FROM ${Contract.UserEntry.TABLE_NAME}"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val userName =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_USERNAME))
                val hashedpw =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_PASSWORD))
                val nickname =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_NICKNAME))
                val createdDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_CREATED_DATE))
                val roleID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.UserEntry.COLUMN_ROLE_ID_FK))


                val user = User(
                    userName = userName,
                    hashedPw = hashedpw,
                    nickName = nickname,
                    createdDate = createdDate,
                    roleID = roleID
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userList
    }

    fun readAllGenres(): List<Genre> {
        val genreList = mutableListOf<Genre>()
        val db = readableDatabase
        val query = "SELECT * FROM ${Contract.GenreEntry.TABLE_NAME}"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val genreName =
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.GenreEntry.COLUMN_NAME))
                val userID =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.GenreEntry.COLUMN_USER_ID_FK))

                val genre = Genre(
                    genreName = genreName,

                    userID = userID
                )
                genreList.add(genre)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return genreList
    }
}