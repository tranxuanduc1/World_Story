package com.example.worldstory

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityStartBinding
import android.os.Looper
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducactivity.DucUserHomeActivity
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

class StartActivity : AppCompatActivity() {


    private lateinit var binding: ActivityStartBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityStartBinding.inflate(layoutInflater)
        val view =binding.root
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(view)
        var isCheck =isCheckUserSession()
        //testDatabase()
        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(isCheck==false){
                //chua dang nhap ,tao tai khoan guest


            }
            // vi du tao tai khioan admin
            var sharePref=this.getSharedPreferences(getString(R.string.key_user_session), Context.MODE_PRIVATE)
            with(sharePref.edit()){
                putInt(getString(R.string.key_user_id_session),1)//id admin
                putInt(getString(R.string.key_user_role_session),1)//role user, nho thao luan id guest la gi
                apply()
            }

            //--------------------
            toActivity(DucUserHomeActivity::class.java)
            finish()
        },2000)


    }
    fun isCheckUserSession(): Boolean{
        var sharePref=this.getSharedPreferences(getString(R.string.key_user_session), Context.MODE_PRIVATE)
        val userId=sharePref.getInt(getString(R.string.key_user_id_session),-1)
        return userId != -1

    }
    private fun testDatabase() {
        var dataHelper: DatabaseHelper = DatabaseHelper(this)
        dataHelper.insertRole(Role(null, "Admin"))
        dataHelper.insertRole(Role(null, "Moderator"))
        dataHelper.insertRole(Role(null, "Member"))
        dataHelper.insertRole(Role(null, "Guest"))

        dataHelper.insertUser(
            User(
                null,
                "user1",
                "hashedPassword1",
                SampleDataStory.getExampleImgURL(),
                "nickname1",
                1,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user2",
                "hashedPassword2",
                SampleDataStory.getExampleImgURL(),
                "nickname2",
                2,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user3",
                "hashedPassword3",
                SampleDataStory.getExampleImgURL(),
                "nickname3",
                3,
                dateTimeNow
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user4",
                "hashedPassword4",
                SampleDataStory.getExampleImgURL(),
                "nickname4",
                4,
                dateTimeNow
            )
        )
        callLog("++++","Action")
        dataHelper.insertGenre(Genre(null, "Action", 1))
        callLog("++++","Romance")

        dataHelper.insertGenre(Genre(null, "Romance", 1))
        callLog("++++","Horror")
        dataHelper.insertGenre(Genre(null, "Horror", 1))
        callLog("++++","Fantasy")
        dataHelper.insertGenre(Genre(null, "Fantasy", 1))


        val imgUrlListString = arrayOf(
            "https://drive.google.com/uc?id=14CThkQ3-7h5fTznj_YovcA7ZQenQgCvI",
            "https://drive.google.com/uc?id=1FYsPBUi7IDfwpWLLJPqPhcJkXwfFN8Q5",
            "https://drive.google.com/uc?id=1VpwrVTxgFL8wMuYu7MU9o8NiPDi27328",
            "https://drive.google.com/uc?id=1PHvkvvzXFZA2-yLv6zhhfjXPiO0iyKqd",
            "https://drive.google.com/uc?id=12OGxPSrunakkC_yxaUatNLw-Ml7NMziK",
            "https://drive.google.com/uc?id=1gco1xpquvGBnyVt3DRarVDcUjbvKjQDR",
            "https://drive.google.com/uc?id=1OZ0NxlZEq3x2OTgno6DJ8rH6lr1JXv8P",
            "https://drive.google.com/uc?id=1ri1jKn_2geJuzAm30iyQqr1vVUkJNo_C",

            )

        for (i in 1..5) {
            val story = Story(
                storyID = null,
                title = "Story $i",
                description = "Description of Story $i",
                imgUrl = imgUrlListString[i],
                bgImgUrl = imgUrlListString[i],
                author = "Author $i",
                createdDate = dateTimeNow,
                isTextStory = 0,
                score = 4.0f,
                userID = 1 // Assuming user ID 1 created the stories
            )
            addStoryWithDetails(story, dataHelper)
        }
        // Add 4 stories with details
        for (i in 6..10) {
            val story = Story(
                storyID = null,
                title = "Story $i",
                description = "Description of Story $i",
                imgUrl = SampleDataStory.getExampleImgURL(),
                bgImgUrl = SampleDataStory.getExampleImgURL(),
                author = "Author $i",
                createdDate = dateTimeNow,
                isTextStory = 1,
                score = 4.0f,
                userID = 1 // Assuming user ID 1 created the stories
            )
            addStoryWithDetails(story, dataHelper)
        }
        dataHelper.insertStoryGenre(storyId = 1, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 1, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 1, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 1, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 2, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 3, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 4, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 5, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 5, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 6, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 6, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 7, genreId = 1)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 2)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 3)
        dataHelper.insertStoryGenre(storyId = 8, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 9, genreId = 4)
        dataHelper.insertStoryGenre(storyId = 10, genreId = 2)
        // Insert 4 rows for the UserLoveStory table
        dataHelper.insertUserLoveStory(1, 1)
        dataHelper.insertUserLoveStory(2, 2)
        dataHelper.insertUserLoveStory(3, 3)
        dataHelper.insertUserLoveStory(4, 4)

        dataHelper.insertChapterMark(1, 1)
        dataHelper.insertChapterMark(2, 2)
        dataHelper.insertChapterMark(3, 3)
        dataHelper.insertChapterMark(4, 4)

        dataHelper.insertChapterHistory(1, 1)
        dataHelper.insertChapterHistory(2, 2)
        dataHelper.insertChapterHistory(3, 3)
        dataHelper.insertChapterHistory(4, 4)
    }

    fun addStoryWithDetails(story: Story, dbHelper: DatabaseHelper) {
        val p1 ="https://drive.google.com/uc?id=1eFvesLiPiREI8q8B2EAufHPWYv5D_jul"
        val p2 ="https://drive.google.com/uc?id=1qcKZ_12g0qdq1wO9kSAzjf3vb4geWepj"
        val p3 ="https://drive.google.com/uc?id=1UZH_gJMCENWPALjWC7--ZFvFoSZUINWZ"
        val p4 ="https://drive.google.com/uc?id=1Tuc_sSJgZqdT2ip_54xISNXS2xu3nVxv"
        val p5 ="https://drive.google.com/uc?id=1zuCJMczfObSTSb50tMXeyRJkYyTLEr4y"

        // Insert the story
        val storyId = dbHelper.insertStory(story)

        if (storyId != -1L) {
            // Add 4 chapters for the story
            for (i in 1..4) {
                val chapter =
                    Chapter(null, "Chapter $i of ${story.title}", dateTimeNow, storyId.toInt())
                val chapterId = dbHelper.insertChapter(chapter)

                if (chapterId != -1L) {
                    // Add 4 paragraphs for each chapter
                    if (story.isTextStory == 0) {

//                            var image = Image(
//                                null,
//                                SampleDataStory.getExampleImgURLParagraph(),
//                                j,
//                                chapterId.toInt()
//                            )
                        var image1 = Image(null, p1, 1, chapterId.toInt())
                        var image2 = Image(null, p2, 2, chapterId.toInt())
                        var image3 = Image(null, p3, 3, chapterId.toInt())
                        var image4 = Image(null, p4, 4, chapterId.toInt())
                        var image5 = Image(null, p5, 5, chapterId.toInt())

                        dbHelper.insertImage(image1)
                        dbHelper.insertImage(image2)
                        dbHelper.insertImage(image3)
                        dbHelper.insertImage(image4)
                        dbHelper.insertImage(image5)

                    }
                    for (j in 1..4) {

                        if (story.isTextStory == 0) {

//                            var image = Image(
//                                null,
//                                SampleDataStory.getExampleImgURLParagraph(),
//                                j,
//                                chapterId.toInt()
//                            )
//                            var image1 = Image(null, p1, 1,chapterId.toInt())
//                            var image2 = Image(null, p2, 2,chapterId.toInt())
//                            var image3 = Image(null, p3, 3,chapterId.toInt())
//                            var image4 = Image(null, p4, 4,chapterId.toInt())
//                            var image5 = Image(null, p5, 5,chapterId.toInt())
//
//                            dbHelper.insertImage(image1)
//                            dbHelper.insertImage(image2)
//                            dbHelper.insertImage(image3)
//                            dbHelper.insertImage(image4)
//                            dbHelper.insertImage(image5)

                        } else {
                            var paragraph = Paragraph(
                                null,
                                getLoremIpsumLong(),
                                j,
                                chapterId.toInt()
                            )
                            dbHelper.insertParagraph(paragraph)

                        }

                    }
                }
            }

            // Add 2 comments for the story
            for (i in 1..2) {
                val comment = Comment(
                    null,
                    "Comment $i on ${story.title}",
                    dateTimeNow,
                    userId = 1, // Assuming user ID 1 for simplicity
                    storyId = storyId.toInt()
                )
                dbHelper.insertComment(comment)
            }
        }
    }
    fun convertGoogleDriveLinkToDirectLink(driveLink: String): String {
        val idRegex = Regex("file/d/([a-zA-Z0-9_-]+)")
        val matchResult = idRegex.find(driveLink)

        return if (matchResult != null) {
            val fileId = matchResult.groupValues[1]
            "https://drive.google.com/uc?id=$fileId"
        } else {
            "Invalid Google Drive link"
        }
    }


}