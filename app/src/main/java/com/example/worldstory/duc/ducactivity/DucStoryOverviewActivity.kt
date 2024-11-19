package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucStoryOverviewBinding
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducutils.changeBackgroundTintColorByScore
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKey_chapterInfo
import com.example.worldstory.duc.ducutils.getKey_mainChapter
import com.example.worldstory.duc.ducutils.getKey_nextChapter
import com.example.worldstory.duc.ducutils.getKey_previousChapter
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.toActivity
import com.example.worldstory.duc.ducutils.toActivityStoriesByGenre
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucGenreViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucGenreViewModelFactory

class DucStoryOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucStoryOverviewBinding
    private lateinit var storyInfo: DucStoryDataClass
    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }
    private val ducGenreViewModel: DucGenreViewModel by viewModels {
        DucGenreViewModelFactory(this)
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
        } else {
            Toast.makeText(this, resources.getString(R.string.storyDataNotFound), Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun setGenreButton() {
        var listGenres = ducGenreViewModel.getGenresByStory(storyInfo)
        for (genre in listGenres) {
            var genreButton = AppCompatButton(this)
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
                text = genre.title
            }

            genreButton.setOnClickListener {
                this.toActivityStoriesByGenre(storyInfo.isComic,genre)
            }


            binding.flexboxContainerGenreButtonStoryOverview.addView(genreButton)

        }
    }

    fun checkloadInfoStory(key: String): Boolean {
        return intent.hasExtra(key)
    }

    fun loadInfoStory(key: String) {
        storyInfo = intent.getParcelableExtra<DucStoryDataClass>(key) as DucStoryDataClass
        binding.txtTitleStoryStoryOverview.text = storyInfo.title
        binding.txtAuthorStoryStoryOverview.text = storyInfo.author
        binding.txtDescriptionStoryStoryOverview.text = storyInfo.description
        binding.imgStoryStoryOverview.loadImgURL(this,storyInfo.imgURL)
        binding.imgBackgroundStoryStoryOverview.loadImgURL(this,storyInfo.backgroundImageURL)
        binding.txtScoreStoryStoryOverview.text = storyInfo.score.toString()
        binding.txtScoreStoryStoryOverview.changeBackgroundTintColorByScore(storyInfo.score)
        generateChapter(storyInfo)
    }

    fun generateChapter(story: DucStoryDataClass) {

        for (item in ducChapterViewModel.getAllChaptersByStory(story)) {
            // Inflate each item view
            val itemView = LayoutInflater.from(this)
                .inflate(
                    R.layout.list_item_chapter_story_overview_layout,
                    binding.lineaerlistChapterStoryOverview,
                    false
                )

            // Set up itemView data if needed
            val titleTextView =
                itemView.findViewById<TextView>(R.id.txtTitleChapter_listItemStoryOverview_layout)
            val idChapterTextView =
                itemView.findViewById<TextView>(R.id.txtIDChapter_listItemStoryOverview_layout)
            val dateCreatedTextView =
                itemView.findViewById<TextView>(R.id.txtDateCreatedChapter_listItemStoryOverview_layout)
            val btn = itemView.findViewById<LinearLayout>(R.id.btn_listItemStoryOverview_layout)
            titleTextView.text = item.title
            idChapterTextView.text = item.idChapter.toString()
            dateCreatedTextView.text = item.dateCreated.toString()
            btn.setOnClickListener {
                // tao key de chuyen cac chuong truoc, sau ,hien tai cho chapter activity
                toChapterActivity(item)

            }
            // Add itemView to the container
            binding.lineaerlistChapterStoryOverview.addView(itemView)
        }
    }

    private fun toChapterActivity(chapter: DucChapterDataClass) {
        var key = getKey_chapterInfo(this)

        var key_mainChapter = getKey_mainChapter(this)
        var key_nextChapter = getKey_nextChapter(this)
        var key_previousChapter = getKey_previousChapter(this)
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

        this.toActivity(DucChapterActivity::class.java, key, bundle)
    }

    fun setButtonWithOutData() {
        binding.btnBackSotryOverview.setOnClickListener {
            finish()
        }
    }
}