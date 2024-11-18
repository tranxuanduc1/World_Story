package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDucChapterBinding
import com.example.myapplication.databinding.CommentOppositeLayoutBinding
import com.example.myapplication.databinding.CommentSelfLayoutBinding
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKey_chapterInfo
import com.example.worldstory.duc.ducutils.getKey_mainChapter
import com.example.worldstory.duc.ducutils.getKey_nextChapter
import com.example.worldstory.duc.ducutils.getKey_previousChapter
import com.example.worldstory.duc.ducutils.getTextDataNotFound
import com.example.worldstory.duc.ducutils.hideKeyboard
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.scrollToBottom
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucCommentViewModel
import com.example.worldstory.duc.ducviewmodel.DucParagraphViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucCommentViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucParagraphViewModelFactory
import com.github.chrisbanes.photoview.PhotoView

class DucChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDucChapterBinding

    //    private lateinit var btnOpenCommentDialog: ImageButton
//    private lateinit var btnBackActivity: ImageButton
//    private lateinit var btnOpenCommentDialog: ImageButton
//    private lateinit var btnOpenCommentDialog: ImageButton
//    private lateinit var btnOpenCommentDialog: ImageButton
    private var mainChapter: DucChapterDataClass? = null
    private var nextChapter: DucChapterDataClass? = null
    private var previousChapter: DucChapterDataClass? = null

    private val ducParagraphViewModel: DucParagraphViewModel by viewModels {
        DucParagraphViewModelFactory(this)
    }
    private val ducChapterViewModel: DucChapterViewModel by viewModels {
        DucChapterViewModelFactory(this)
    }
    private val ducCommentViewModel: DucCommentViewModel by viewModels {
        DucCommentViewModelFactory(this)
    }

    private var isTopFrameVisible = true
    private var isBottomFrameVisible = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDucChapterBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)


        var scrollView = binding.scrollParagraphChapter
        var frameTop = binding.frameTopChapter
        var frameBottom = binding.frameBottomChapter
        var key: String = getKey_chapterInfo(this)
//        mainChapter=chapterViewModel.mainChapter
//        previousChapter=chapterViewModel.previousChapter
//        nextChapter=chapterViewModel.nextChapter
        if (ducChapterViewModel.isFirstLoadChapter) {

            ducChapterViewModel.isFirstLoadChapter = false
            if (isCheckLoadData(key)) {
                loadData(key)

            }
        }
        setData()
        loadParagraph()
        loadComment()
        setConfigButtonComment()
        setConfigButtonChapter()

        //-------------
        setAnimationScollView(binding)
        setConfigCommentDialog(binding)
        setConfigButton(binding)


    }

    private fun setConfigButtonComment() {
        binding.btnSendCommentUserChapter.setOnClickListener {
            var content = binding.etxtCommentUserChapter.text.toString()
            if (content.isEmpty()) {
                return@setOnClickListener
            }

            ducCommentViewModel.createUserCommnet(
                mainChapter?.idStory ?: ducChapterViewModel.getOneExampleChapter().idStory, content
            )

            //xoa trang editText de nhap comment moi
            binding.etxtCommentUserChapter.setText("")
            // chay lai comment dialog
            loadComment()
        }
    }

    private fun loadComment() {
        //lam moi hop thoai scroll view chua cac comment
        binding.linearContainerCommentChapter.removeAllViews()

        var listComments = ducCommentViewModel.getCommentsByStory(
            mainChapter?.idStory ?: ducChapterViewModel.getOneExampleChapter().idStory
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

    private fun loadParagraph() {

        var linearContainer = binding.linearContainerContentChapter
        // lam moi lai , xoa di nhung du lieu cu truoc do, dong thoi cuon ve diem ban dau
        linearContainer.removeAllViews()
        binding.scrollParagraphChapter.scrollTo(0, 0)

        var paragraphsList = ducParagraphViewModel.getAllParagraphsByChapter(mainChapter)
        if (paragraphsList.isEmpty()) {
            return
        }
        //phan loai kieu doan van,co hinh anh la comic, khong co anh la text
        for (item in paragraphsList) {
            if (item.isComic) {
                linearContainer.addView(createContentPhoToView(item.imgContent))

            } else {
                linearContainer.addView(createContentTextView(item.textContent))

            }
        }

//        linearContainer.addView(createContentPhoToView(R.drawable.pa2))
//        linearContainer.addView(createContentPhoToView(R.drawable.pa3))
//        linearContainer.addView(createContentPhoToView(R.drawable.pa4))

    }

    fun createContentPhoToView(imgSrc: Int?): PhotoView {
        var photoView = PhotoView(this)
        photoView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            adjustViewBounds = true
            setImageResource(imgSrc ?: R.drawable.pa1)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        return photoView
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

    private fun loadData(key: String) {
        var bundle = intent.getBundleExtra(key)
        if (bundle is Bundle) {
            mainChapter = bundle.getParcelable(getKey_mainChapter(this))
            nextChapter = bundle.getParcelable(getKey_nextChapter(this))
            previousChapter =
                bundle.getParcelable(getKey_previousChapter(this))
            ducChapterViewModel.setPreMainNextChapter(mainChapter, previousChapter, nextChapter)

        }
    }

    fun setData() {
        mainChapter = ducChapterViewModel.mainChapter
        previousChapter = ducChapterViewModel.previousChapter
        nextChapter = ducChapterViewModel.nextChapter
        binding.txtTitleChapterChapter.text = mainChapter?.title ?: getTextDataNotFound(this)

    }

    private fun setConfigButtonChapter(
    ) {
        // kiem tra cac nut chuong truoc va chuong sau
        if (previousChapter == null) {
            binding.btnPreviousChapterChapter.isEnabled = false
        } else {
            binding.btnPreviousChapterChapter.isEnabled = true
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
        if (nextChapter == null) {
            binding.btnNextChapterChapter.isEnabled = false
        } else {
            binding.btnNextChapterChapter.isEnabled = true
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

    private fun isCheckLoadData(key: String): Boolean {
        return (intent.hasExtra(key))
    }

    private fun setConfigButton(binding: ActivityDucChapterBinding) {
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

    private fun setConfigCommentDialog(binding: ActivityDucChapterBinding) {
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

    private fun setAnimationScollView(binding: ActivityDucChapterBinding) {
        var scrollView = binding.scrollParagraphChapter
        var frameTop = binding.frameTopChapter
        var frameBottom = binding.frameBottomChapter
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->

            if (scrollY > oldScrollY) {
                if (isTopFrameVisible) {
                    frameTop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
                    frameTop.visibility = View.GONE
                    isTopFrameVisible = false
                }
                if (isBottomFrameVisible) {
                    frameBottom.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.slide_down
                        )
                    )
                    frameBottom.visibility = View.GONE
                    isBottomFrameVisible = false
                }

            } else if (scrollY < oldScrollY) {
                // Cuộn lên, hiện các Frame
                if (!isTopFrameVisible) {
                    //frameTop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down))
                    frameTop.visibility = View.VISIBLE
                    isTopFrameVisible = true
                }
                if (!isBottomFrameVisible) {
                    //frameBottom.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
                    frameBottom.visibility = View.VISIBLE
                    isBottomFrameVisible = true
                }
            }
        })
    }

    private fun resetData() {
        setData()
        loadParagraph()
        setConfigButtonChapter()
    }
}