package com.example.worldstory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityStartBinding
import android.os.Looper
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducactivity.DucUserHomeActivity
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.hashPassword
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucAccountManagerViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Rate
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User
import kotlin.getValue

class StartActivity : AppCompatActivity() {

    companion object {
         var isActivityRunning = false
    }
    private val ducAccountManagerViewModel: DucAccountManagerViewModel by viewModels {
        DucAccountManagerViewModelFactory(this)
    }
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isActivityRunning) {
            // Nếu activity đã chạy, kết thúc activity hiện tại
            finish()
            return
        }

        isActivityRunning = true
        enableEdgeToEdge()
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(view)
        //testDatabase()

        callLog("=========", "bat dau chay start activity")


        var isCheck = isCheckUserSession()
        if (isCheck == false) {
            //chua dang nhap ,tao tai khoan guest
            makeUserGuest()

        }else{
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
            // di chuyen toi trang chu
            var intent = Intent(this, DucUserHomeActivity::class.java)
            //xoa het activity cu
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)
                //isActivityRunning=false
            finish()
        }, 2000)
        }

        ducAccountManagerViewModel.newGuestUser.observe(this, Observer{
            user->
            ducAccountManagerViewModel.fetchUserSessionAndRoleByUsername(user.userName)
        })
        ducAccountManagerViewModel.userSessionAndRole.observe(this, Observer{
            userAndRole->
           waitThenSaveShareedPreference(userAndRole)
        })










//         vi du tao tai khioan admin
//            var sharePref =
//                this.getSharedPreferences(
//                    getString(R.string.key_user_session),
//                    Context.MODE_PRIVATE
//                )
//            with(sharePref.edit()) {
//                putInt(getString(R.string.key_user_id_session), 1)//id admin
//                putString(
//                    getString(R.string.key_user_role_session),
//                    RoleEnum.ADMIN.name
//                )//role user, nho thao luan id guest la gi
//
//                apply()
//            }
//
//            //--------------------
//
//            var intent = Intent(this, DucUserHomeActivity::class.java)
//            this.startActivity(intent)
//            finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("StartActivity", "onNewIntent called: ${intent?.action}")

    }

    fun isCheckUserSession(): Boolean {
        var sharePref =
            this.getSharedPreferences(getString(R.string.key_user_session), Context.MODE_PRIVATE)
        val userId = sharePref.getInt(getString(R.string.key_user_id_session), -1)
        return userId != -1

    }

    private fun testDatabase() {
        var dataHelper: DatabaseHelper = DatabaseHelper.getInstance(this)
        dataHelper.insertRole(Role(null, RoleEnum.ADMIN.name))
        dataHelper.insertRole(Role(null, RoleEnum.AUTHOR.name))
        dataHelper.insertRole(Role(null, RoleEnum.MEMBER.name))
        dataHelper.insertRole(Role(null, RoleEnum.GUEST.name))

        dataHelper.insertUser(
            User(
                null,
                "user1",
                hashPassword(SampleDataStory.getExamplePassword()),
                SampleDataStory.getExampleEmail(),
                "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV",
                "nickname1",
                1,
                dateTimeNow()
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user2",
                hashPassword(SampleDataStory.getExamplePassword()),
                SampleDataStory.getExampleEmail(),
                SampleDataStory.getExampleImgURL(),
                "nickname2",
                2,
                dateTimeNow()
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user3",
                hashPassword(SampleDataStory.getExamplePassword()),
                SampleDataStory.getExampleEmail(),
                SampleDataStory.getExampleImgURL(),
                "nickname3",
                3,
                dateTimeNow()
            )
        )
        dataHelper.insertUser(
            User(
                null,
                "user4",
                hashPassword(SampleDataStory.getExamplePassword()),
                SampleDataStory.getExampleEmail(),
                SampleDataStory.getExampleImgURL(),
                "nickname4",
                4,
                dateTimeNow()
            )
        )
        dataHelper.insertGenre(Genre(null, "Action", 1))

        dataHelper.insertGenre(Genre(null, "Romance", 1))
        dataHelper.insertGenre(Genre(null, "Horror", 1))
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
                createdDate = dateTimeNow(),
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
                createdDate = dateTimeNow(),
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


        dataHelper.insertRate(Rate(null, 3, 1, 1))
        dataHelper.insertRate(Rate(null, 5, 2, 1))
        dataHelper.insertRate(Rate(null, 2, 3, 1))
        dataHelper.insertRate(Rate(null, 1, 2, 2))
        dataHelper.insertRate(Rate(null, 3, 3, 2))
        dataHelper.insertRate(Rate(null, 4, 4, 2))


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
        val p1 = "https://drive.google.com/uc?id=1eFvesLiPiREI8q8B2EAufHPWYv5D_jul"
        val p2 = "https://drive.google.com/uc?id=1qcKZ_12g0qdq1wO9kSAzjf3vb4geWepj"
        val p3 = "https://drive.google.com/uc?id=1UZH_gJMCENWPALjWC7--ZFvFoSZUINWZ"
        val p4 = "https://drive.google.com/uc?id=1Tuc_sSJgZqdT2ip_54xISNXS2xu3nVxv"
        val p5 = "https://drive.google.com/uc?id=1zuCJMczfObSTSb50tMXeyRJkYyTLEr4y"

        // Insert the story
        val storyId = dbHelper.insertStory(story)

        if (storyId != -1L) {
            // Add 4 chapters for the story
            for (i in 1..4) {
                val chapter =
                    Chapter(null, "Chapter $i of ${story.title}", dateTimeNow(), storyId.toInt())
                val chapterId = dbHelper.insertChapter(chapter)

                if (chapterId != -1L) {
                    // Add 4 paragraphs for each chapter
                    if (story.isTextStory == 0) {
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
                    dateTimeNow(),
                    userId = 2, // Assuming user ID 1 for simplicity
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
    private fun makeUserGuest() {
        ducAccountManagerViewModel.fetchNewGuestAccount()
    }

    private fun waitThenSaveShareedPreference(userAndRole: Pair<User, Role>) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            var sharedPreferences =
                getSharedPreferences(getString(R.string.key_user_session), Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putInt(
                    getString(R.string.key_user_id_session),
                    userAndRole.first.userID ?: numDef
                )//kiem ko dc thi la guest
                putString(getString(R.string.key_user_role_session), userAndRole.second.roleName)
                apply()
            }
            // di chuyen toi trang chu
            var intent = Intent(this, DucUserHomeActivity::class.java)
            //xoa het activity cu
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK )
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)
            isActivityRunning=false
            finish()
        }, 2000)
    }
}



