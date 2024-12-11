package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucStoryOverviewBinding
import com.example.worldstory.duc.ducdialog.DucLoginDialogFragment
import com.example.worldstory.duc.ducutils.changeBackgroundTintColorByScore
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKeyChapterInfo
import com.example.worldstory.duc.ducutils.getKeyMainChapter
import com.example.worldstory.duc.ducutils.getKeyNextChapter
import com.example.worldstory.duc.ducutils.getKeyPreviousChapter
import com.example.worldstory.duc.ducutils.isUserCurrentGuest
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.duc.ducviewmodel.DucChapterHistoryViewModel
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodel.DucRateViewModel
import com.example.worldstory.duc.ducviewmodel.DucSwipeRefreshViewModel
import com.example.worldstory.duc.ducviewmodel.DucUserLoveStoryViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterHistoryViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucRateViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucSwipeRefreshViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucUserLoveStoryViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Story

class DucStoryOverviewActivity : AppCompatActivity(), DucLoginDialogFragment.DialogListener {
    private lateinit var binding: ActivityDucStoryOverviewBinding
    private lateinit var storyInfo: Story
    private lateinit var dialogRequestLogin: DucLoginDialogFragment

    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }
    private val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(this)
    }
    private val ducRateViewModel: DucRateViewModel by viewModels {
        DucRateViewModelFactory(this)
    }
    private val ducUserLoveStoryViewModel: DucUserLoveStoryViewModel by viewModels {
        DucUserLoveStoryViewModelFactory(this)
    }
    private val ducChapterHistoryViewModel: DucChapterHistoryViewModel by viewModels {
        DucChapterHistoryViewModelFactory(this)
    }
    private val ducSwipeRefreshViewModel: DucSwipeRefreshViewModel by viewModels {
        DucSwipeRefreshViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucStoryOverviewBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        setButtonWithOutData()
        //-------------


            var key = getKeyStoryInfo(this)

            if (checkloadInfoStory(key)) {
                loadInfoStory(key)
                setGenreButton()
                setRatingBar()
                setUserSessionLoveStory()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.storyDataNotFound),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        setDialogRequestLogin()
        setSwipeRefresh(key)
    }



    private fun setGenreButton() {
        ducGenreViewModel.fetchGenresByStory(storyInfo.storyID ?: numDef)
        ducGenreViewModel.genresByStory.observe(this, Observer { genresByStory ->
            binding.flexboxContainerGenreButtonStoryOverview.removeAllViews()
            for (genre in genresByStory) {
                var genreButton = AppCompatButton(this)
                setStyleGenreButton(genreButton, genre)

                genreButton.setOnClickListener {
                    this.toActivityStoriesByGenre(storyInfo.isTextStory.toBoolean(), genre)
                }


                binding.flexboxContainerGenreButtonStoryOverview.addView(genreButton)

            }
        })

    }

    private fun setStyleGenreButton(genreButton: AppCompatButton, genre: Genre) {
        genreButton.apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            }
            setPadding(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            setTextColor(
                ContextCompat.getColorStateList(
                    context,
                    R.color.selector_button_text_color_primary
                )
            )
            background = ContextCompat.getDrawable(context, R.drawable.shape_button_primary)
            text = genre.genreName
        }
    }

    fun checkloadInfoStory(key: String): Boolean {
        return intent.hasExtra(key)
    }

    fun loadInfoStory(key: String) {
        storyInfo = intent.getParcelableExtra<Story>(key) as Story
        binding.txtTitleStoryStoryOverview.text = storyInfo.title
        binding.txtAuthorStoryStoryOverview.text = storyInfo.author
        binding.txtDescriptionStoryStoryOverview.text = storyInfo.description
        binding.imgStoryStoryOverview.loadImgURL(this, storyInfo.imgUrl)
        binding.imgBackgroundStoryStoryOverview.loadImgURL(this, storyInfo.bgImgUrl)
        binding.txtScoreStoryStoryOverview.text = storyInfo.score.toString()
        generateChapter(storyInfo)

    }

    fun generateChapter(story: Story) {
        ducChapterViewModel.fetchChaptersByStory(storyInfo.storyID ?: 1)
        ducChapterViewModel.chaptersByStory.observe(this, Observer() { chapters ->


            storyInfo?.let {
                setChapterHistoryAndChapterNotRead(
                    it.storyID ?: return@let,
                    chapters
                )
            }

        })

    }

    private fun setItemViewChapter(itemView: View, chapter: Chapter, isRead: Boolean) {
        val titleTextView =
            itemView.findViewById<TextView>(R.id.txtTitleChapter_listItemStoryOverview_layout)
        val idChapterTextView =
            itemView.findViewById<TextView>(R.id.txtIDChapter_listItemStoryOverview_layout)
        val dateCreatedTextView =
            itemView.findViewById<TextView>(R.id.txtDateCreatedChapter_listItemStoryOverview_layout)
        val btn = itemView.findViewById<LinearLayout>(R.id.btn_listItemStoryOverview_layout)
        titleTextView.text = chapter.title
        idChapterTextView.text = chapter.chapterID.toString()
        dateCreatedTextView.text = chapter.dateCreated.toString()
        if (isRead) {
            itemView.setBackgroundResource(R.color.duc_skin)
        }

        btn.setOnClickListener {
            // tao key de chuyen cac chuong truoc, sau ,hien tai cho chapter activity
            toChapterActivity(chapter)

        }
    }

    private fun toChapterActivity(chapter: Chapter) {
        var key = getKeyChapterInfo(this)

        var key_mainChapter = getKeyMainChapter(this)
        var key_nextChapter = getKeyNextChapter(this)
        var key_previousChapter = getKeyPreviousChapter(this)
        var key_storyInfo = getKeyStoryInfo(this)

        var bundle = Bundle()
        bundle.putParcelable(key_mainChapter, chapter)
        bundle.putParcelable(
            key_nextChapter,
            ducChapterViewModel.getNextChapterByCurrentChapter(chapter)
        )
        bundle.putParcelable(
            key_previousChapter,
            ducChapterViewModel.getPreviousChapterByCurrentChapter(chapter)
        )
        bundle.putParcelable(
            key_storyInfo,
            storyInfo
        )
        this.toActivity(DucChapterActivity::class.java, key, bundle)
    }

    private fun setRatingBar() {
        ducRateViewModel.setRateByStory(storyInfo.storyID ?: numDef)
        ducRateViewModel.ratingsByStory.observe(this, Observer { ratings ->
            var averageScore: Float
            if (ratings.isEmpty()) {
                averageScore = 5.0f
            } else {
                ratings.stream()
                averageScore = ratings.map { it.score }.average().toFloat()

            }
            binding.txtScoreStoryStoryOverview.text = String.format("%.1f", averageScore)
            binding.txtScoreStoryStoryOverview.changeBackgroundTintColorByScore(averageScore)

            var scoreUserSessionRated = ducRateViewModel.getScoreRateByUserSession()
            if (scoreUserSessionRated < 0f) {
                //user hien tai chua danh gia
                binding.rateBarStoryOverview.rating = 0f

            } else {
                binding.rateBarStoryOverview.rating = scoreUserSessionRated

            }
        })

        binding.rateBarStoryOverview.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            //kiem tra co phai gest user
            if (fromUser && !checkGuestUser()) {
                ducRateViewModel.ratingStoryByCurrentUser(
                    storyInfo.storyID ?: numDef,
                    rating.toInt()
                )

            }
        }


    }

    private fun setUserSessionLoveStory() {

        ducUserLoveStoryViewModel.userSessionLoveStories.observe(this, Observer { stories ->
            var isLike = false
            if (stories != null) {
                var loveStories = stories.filter { it.storyID == storyInfo.storyID }
                if (loveStories.isNotEmpty()) {
                    // neu nhu user da bam thich story nay
                    isLike = true
                    setStyleButtonLoveStory(isLike)


                }
                //  neu user bam nut lan nua
                binding.btnLoveStoryStoryOverview.setOnClickListener {
                    if (checkThenHandleGuestUser())return@setOnClickListener
                    isLike = !isLike
                    setStyleButtonLoveStory(isLike)
                    updateDataUserSessionLoveStory(isLike)
                }

            }
        })

    }


    fun setStyleButtonLoveStory(isLove: Boolean) {
        if (isLove) {
            binding.btnLoveStoryStoryOverview.apply {
                setTextColor(ContextCompat.getColor(context, R.color.color_test_user_loved_story))
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_heart_fill, 0)
                setBackgroundResource(R.drawable.shape_button_user_loved_story)
            }
        } else {
            binding.btnLoveStoryStoryOverview.apply {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_text_user_not_love_story
                    )
                )
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_heart_outline, 0)
                setBackgroundResource(R.drawable.shape_button_user_not_love_story)
            }
        }
    }

    private fun updateDataUserSessionLoveStory(isLove: Boolean) {
        if (isLove) {
            if (storyInfo != null) {
                ducUserLoveStoryViewModel.setUserSessionLovedStory(storyInfo.storyID ?: numDef)

            }
        } else {
            if (storyInfo.storyID != null) {
                ducUserLoveStoryViewModel.deleteUserSessionLovedStory(storyInfo.storyID ?: numDef)

            }
        }
    }

    private fun setChapterHistoryAndChapterNotRead(storyId: Int, notReadChapers: List<Chapter>) {
        //set button read first chapter
        binding.btnReadFirstChapterStoryOverview.setOnClickListener{
            if (notReadChapers.isNotEmpty())
            toChapterActivity(notReadChapers.first())
        }

        ducChapterHistoryViewModel.fetchChaptersHistoryByStory(storyId)
        ducChapterHistoryViewModel.chaptersHistoryByStory.observe(
            this,
            Observer { chaptersHistory ->
                binding.lineaerlistChapterStoryOverview.removeAllViews()
                for (item in notReadChapers) {
                    // Inflate each item view
                    val itemView = LayoutInflater.from(this)
                        .inflate(
                            R.layout.list_item_chapter_story_overview_layout,
                            binding.lineaerlistChapterStoryOverview,
                            false
                        )
                    var isRead = false
                    var listChapterHis = chaptersHistory.filter { it.chapterID == item.chapterID }
                    //neu truyen nay da duoc user hien tai doc qua
                    if (listChapterHis.isNotEmpty()) isRead = true



                    // Set up itemView data if needed
                    setItemViewChapter(itemView, item, isRead)
                    // Add itemView to the container
                    binding.lineaerlistChapterStoryOverview.addView(itemView)


                }

            })
    }

    fun setButtonWithOutData() {
        binding.btnBackSotryOverview.setOnClickListener {
            finish()
        }
        binding.swipeRefreshStoryOverview.setOnRefreshListener {

            ducSwipeRefreshViewModel.fetchRefreshView()
        }
    }
    private fun setSwipeRefresh(key: String) {
        ducSwipeRefreshViewModel.refreshView.observe(this, Observer {
                refresh->
            //tat hieu ung load
            binding.swipeRefreshStoryOverview.isRefreshing = false
            setGenreButton()
            loadInfoStory(key)
            setRatingBar()
        })
    }
    private fun setDialogRequestLogin() {
        dialogRequestLogin= DucLoginDialogFragment()

    }
    private fun checkThenHandleGuestUser(): Boolean {
        if(isUserCurrentGuest()){
            dialogRequestLogin.show(supportFragmentManager,"login")

            return true
        }else{
            return false
        }
    }
    private fun checkGuestUser(): Boolean {
        if(isUserCurrentGuest()){

            return true
        }else{
            return false
        }
    }
    override fun onDialogSubmit(input: String) {
        TODO("Not yet implemented")
    }
}


