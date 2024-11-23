package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivityDucChapterBinding
import com.example.myapplication.databinding.CommentOppositeLayoutBinding
import com.example.myapplication.databinding.CommentSelfLayoutBinding
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducadapter.DucViewPaperPhotoViewAdapter
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKey_chapterInfo
import com.example.worldstory.duc.ducutils.getKey_mainChapter
import com.example.worldstory.duc.ducutils.getKey_nextChapter
import com.example.worldstory.duc.ducutils.getKey_previousChapter
import com.example.worldstory.duc.ducutils.hideKeyboard
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.getTextDataNotFound
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.scrollToBottom
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucCommentViewModel
import com.example.worldstory.duc.ducviewmodel.DucImageViewModel
import com.example.worldstory.duc.ducviewmodel.DucParagraphViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucCommentViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucImageViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucParagraphViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Image
import com.example.worldstory.model.Story


class DucChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucChapterBinding


    private var mainChapter: Chapter? = null
    private var nextChapter: Chapter? = null
    private var previousChapter: Chapter? = null

    private val ducParagraphViewModel: DucParagraphViewModel by viewModels {
        DucParagraphViewModelFactory(this)
    }
    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }
    private val ducCommentViewModel: DucCommentViewModel by viewModels {
        DucCommentViewModelFactory(this)
    }
    private val ducImageViewModel: DucImageViewModel by viewModels{
        DucImageViewModelFactory(this)
    }
    private var isTopFrameVisible = true
    private var isBottomFrameVisible = true
    private var storyInfo: Story? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucChapterBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)

        var key: String = getKey_chapterInfo(this)




        if (isCheckLoadData(key)) {

            loadData(key)
            //

            loadInfoChpater()
            ducChapterViewModel.chaptersByStory.observe(this, Observer {
                setViewButtonChapter()
                setConfigButtonChapter()
            })

            setupViewImageOrParagraph()
            prepareDataContenForChapter()
            loadContent()

        }
//        loadComment()
//        setConfigButtonComment()

        //
        setConfigCommentDialog()
        setConfigButton()
    }

    private fun isCheckLoadData(key: String): Boolean {
        return (intent.hasExtra(key))
    }

    private fun loadData(key: String) {
        var bundle = intent.getBundleExtra(key)
        if (bundle is Bundle) {
            mainChapter = bundle.getParcelable(getKey_mainChapter(this))
            nextChapter = bundle.getParcelable(getKey_nextChapter(this))
            previousChapter =
                bundle.getParcelable(getKey_previousChapter(this))
            storyInfo = bundle.getParcelable(getKeyStoryInfo(this))
            ducChapterViewModel.setPreMainNextChapter(mainChapter, previousChapter, nextChapter)
            ducChapterViewModel.setChaptersByStory(storyInfo?.storyID ?: numDef)

        }
    }

    fun loadInfoChpater() {
        binding.txtTitleChapterChapter.text = mainChapter?.title ?: getTextDataNotFound(this)
    }

    private fun setConfigButtonChapter(
    ) {
        // kiem tra cac nut chuong truoc va chuong sau
        if (previousChapter != null) {
            binding.btnPreviousChapterChapter.setOnClickListener {
                // thiet lap lai cac chuong khi nguoi dung bam chuong truoc
                ducChapterViewModel.setPreMainNextChapter(
                    previousChapter,
                    ducChapterViewModel.getPreviousChapterByCurrentChapter(previousChapter),
                    mainChapter
                )
                resetData()
            }
        }
        if (nextChapter != null) {
            binding.btnNextChapterChapter.setOnClickListener {
                // thiet lap lai cac chuong khi nguoi dung bam chuong sau
                ducChapterViewModel.setPreMainNextChapter(
                    nextChapter,
                    mainChapter,
                    ducChapterViewModel.getNextChapterByCurrentChapter(nextChapter)
                )

                resetData()
            }
        }

    }

    private fun setViewButtonChapter(
    ) {
        // kiem tra cac nut chuong truoc va chuong sau
        if (previousChapter == null) {
            binding.btnPreviousChapterChapter.isEnabled = false
        } else {
            binding.btnPreviousChapterChapter.isEnabled = true

        }
        if (nextChapter == null) {
            binding.btnNextChapterChapter.isEnabled = false
        } else {
            binding.btnNextChapterChapter.isEnabled = true

        }

    }

    private fun resetData() {
        setPreMainNextChapter()
        loadInfoChpater()
        setViewButtonChapter()
        setConfigButtonChapter()
        prepareDataContenForChapter()


    }

    fun setPreMainNextChapter() {

        mainChapter = ducChapterViewModel.mainChapter
        previousChapter = ducChapterViewModel.previousChapter
        nextChapter = ducChapterViewModel.nextChapter

    }

    private fun setupViewImageOrParagraph() {

        if (storyInfo?.isTextStory?.toBoolean() == true) {

            // disable view container image
            binding.scrollParagraphChapter.visibility = View.VISIBLE
            binding.linearContainViewpaperChapter.visibility = View.GONE


        } else {
            // disable view container paragraph
            binding.scrollParagraphChapter.visibility = View.GONE
            binding.linearContainViewpaperChapter.visibility = View.VISIBLE


        }
    }
    private fun prepareDataContenForChapter(){
        if (storyInfo?.isTextStory?.toBoolean() == true) {
            // prepare  paragraphs from view model
            ducParagraphViewModel.setParagraphsByChapter(mainChapter?.chapterID ?: numDef)


        } else {

            // prepare images from view model
            ducImageViewModel.setImagesByChapter(mainChapter?.chapterID?:numDef)

        }
    }
    private fun loadContent() {


        if (storyInfo?.isTextStory?.toBoolean() == true) {

            loadParagraph()

        } else {

            loadImage()

        }

    }

    private fun loadParagraph() {


        ducParagraphViewModel.paragraphsByChapter.observe(this, Observer { paragraphs ->

            // xoa nhung doan van cu da duoc nap truoc do
            binding.linearContainerContentChapter.removeAllViews()

            // sap sep lai thu tu cua doan van
            var listOfParagraphs = paragraphs.sortedBy { it.paragraphID }

            listOfParagraphs.forEach {
                binding.linearContainerContentChapter.addView(
                    createContentTextView("idPa : ${it.paragraphID},idChap: ${it.chapterID} --\n ${it.content}")
                )
            }
            // cuon ve vi tri dau
            binding.scrollParagraphChapter.scrollTo(0, 0)
        })

    }
    private fun loadImage() {
        ducImageViewModel.imagesByChapter.observe(this, Observer{
            images->
            var adapterViewPage2 = DucViewPaperPhotoViewAdapter(
                this,
                images
            )

            binding.viewPaper2ContainerImageChapter.apply {
                adapter = adapterViewPage2
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        })

    }



    fun createContentTextView(textContent: String?): TextView {
        var textView = TextView(this)
        textView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 20f
            text = (textContent ?: getLoremIpsumLong(context))
            setPadding(20.dpToPx(), 20.dpToPx(), 20.dpToPx(), 20.dpToPx())
        }
        return textView
    }

    //--------------Comment---------------------

    private fun loadComment() {
        //lam moi hop thoai scroll view chua cac comment
        binding.linearContainerCommentChapter.removeAllViews()

        var listComments = ducCommentViewModel.getCommentsByStory(
            mainChapter?.storyID ?: ducChapterViewModel.getOneExampleChapter().storyID
        )

        for (comment in listComments) {
            var commentLayoutBinding: ViewBinding

            if (ducCommentViewModel.checkCommentFromUser(comment)) {
                commentLayoutBinding = CommentSelfLayoutBinding.inflate(layoutInflater)
                setCommentSelf(commentLayoutBinding, comment)
            } else {
                commentLayoutBinding = CommentOppositeLayoutBinding.inflate(layoutInflater)
                setCommentOpposite(commentLayoutBinding, comment)
            }
            binding.linearContainerCommentChapter.addView(commentLayoutBinding.root)
        }
        binding.scrollViewMainCommentDialogChapter.scrollToBottom()
    }
    private fun setConfigButtonComment() {
        binding.btnSendCommentUserChapter.setOnClickListener {
            var content = binding.etxtCommentUserChapter.text.toString()
            if (content.isEmpty()) {
                return@setOnClickListener
            }

            ducCommentViewModel.createUserCommnet(
                mainChapter?.storyID ?: ducChapterViewModel.getOneExampleChapter().storyID, content
            )

            //xoa trang editText de nhap comment moi
            binding.etxtCommentUserChapter.setText("")
            // chay lai comment dialog
            loadComment()
        }
    }
    private fun setCommentSelf(
        commentLayoutBinding: CommentSelfLayoutBinding, comment: DucCommentDataClass
    ) {
        setCommentSelfLayoutParams((commentLayoutBinding.root))

        commentLayoutBinding.txtDisplayNameCommentSelfLayout.text =
            ducCommentViewModel.getDisplayNameUserByComment(comment)
        commentLayoutBinding.txtContentCommentSelfLayout.text = comment.content
        commentLayoutBinding.imgAvatarUserCommentSelfLayout.setImageResource(
            ducCommentViewModel.getAvatarUserByComment(comment)
        )
        commentLayoutBinding.txtDateCreatedCommentSelfLayout.text = comment.date
    }

    private fun setCommentOpposite(
        commentLayoutBinding: CommentOppositeLayoutBinding, comment: DucCommentDataClass
    ) {
        setCommentOppositeLayoutParams(commentLayoutBinding.root)

        commentLayoutBinding.txtDisplayNameCommentOppositeLayout.text =
            ducCommentViewModel.getDisplayNameUserByComment(comment)
        commentLayoutBinding.txtContentCommentOppositeLayout.text = comment.content
        commentLayoutBinding.imgAvatarUserCommentOppositeLayout.setImageResource(
            ducCommentViewModel.getAvatarUserByComment(comment)
        )
        commentLayoutBinding.txtDateCreatedCommentOppositeLayout.text = comment.date
    }
    fun setCommentSelfLayoutParams(view: View) {
        view.apply {
            //android:layout_width="wrap_content"
            //android:layout_height="wrap_content"
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )

            //android:layout_margin="10dp"
            layoutParams = ViewGroup.MarginLayoutParams(layoutParams).apply {
                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            }

            //android:layout_gravity="end"
            layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                gravity = Gravity.END
            }
            //android:orientation="vertical"
            if (view is LinearLayout) {
                (view as LinearLayout).orientation = LinearLayout.VERTICAL
            }

        }
    }

    fun setCommentOppositeLayoutParams(view: View) {
        view.apply {
            //android:layout_width="wrap_content"
            //android:layout_height="wrap_content"
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )

            //android:layout_margin="10dp"
            layoutParams = ViewGroup.MarginLayoutParams(layoutParams).apply {
                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
            }

            //android:layout_gravity="start"
            layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                gravity = Gravity.START
            }

            //android:orientation="vertical"
            if (view is LinearLayout) {
                (view as LinearLayout).orientation = LinearLayout.VERTICAL
            }

        }
    }

    private fun setConfigCommentDialog() {
        binding.frameContainerCommentDialogChapter.visibility = View.GONE
        var viewBackground = binding.viewBackgroundCommentDialogChapter
        var btnBack = binding.btnBackCommentDialogChaper

        btnBack.setOnClickListener {
            binding.frameContainerCommentDialogChapter.visibility = View.GONE
            btnBack.hideKeyboard(this)
        }
        viewBackground.setOnClickListener {
            viewBackground.hideKeyboard(this)
            binding.frameContainerCommentDialogChapter.visibility = View.GONE
        }

    }

    //--------------button---------------------
    private fun setConfigButton() {
        var btnOpenCommentDialog = binding.btnOpenCommentDialogChapter
        var btnBackActivity = binding.btnBackChapter
        btnBackActivity.setOnClickListener {
            finish()
        }
        btnOpenCommentDialog.setOnClickListener {
            binding.frameContainerCommentDialogChapter.visibility = View.VISIBLE
            binding.scrollViewMainCommentDialogChapter.scrollToBottom()
        }

    }
}