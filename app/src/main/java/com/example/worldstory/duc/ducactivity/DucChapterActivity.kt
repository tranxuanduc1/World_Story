package com.example.worldstory.duc.ducactivity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivityDucChapterBinding
import com.example.myapplication.databinding.CommentOppositeLayoutBinding
import com.example.myapplication.databinding.CommentSelfLayoutBinding
import com.example.worldstory.duc.ducadapter.DucViewPaperPhotoViewAdapter
import com.example.worldstory.duc.ducadapter.Duc_Comment_Adapter
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducdialog.DucLoginDialogFragment
import com.example.worldstory.duc.ducutils.callLog
import com.example.worldstory.duc.ducutils.dpToPx
import com.example.worldstory.duc.ducutils.getKeyStoryInfo
import com.example.worldstory.duc.ducutils.getKey_chapterInfo
import com.example.worldstory.duc.ducutils.getKey_mainChapter
import com.example.worldstory.duc.ducutils.getKey_nextChapter
import com.example.worldstory.duc.ducutils.getKey_previousChapter
import com.example.worldstory.duc.ducutils.hideKeyboard
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.duc.ducutils.getTextDataNotFound
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.example.worldstory.duc.ducutils.isUserCurrentGuest
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.scrollToBottom
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.duc.ducviewmodel.DucChapterHistoryViewModel
import com.example.worldstory.duc.ducviewmodel.DucChapterMarkViewModel
import com.example.worldstory.duc.ducviewmodel.DucChapterViewModel
import com.example.worldstory.duc.ducviewmodel.DucCommentViewModel
import com.example.worldstory.duc.ducviewmodel.DucImageViewModel
import com.example.worldstory.duc.ducviewmodel.DucParagraphViewModel
import com.example.worldstory.duc.ducviewmodel.DucSwipeRefreshViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterHistoryViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterMarkViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucChapterViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucCommentViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucImageViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucParagraphViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucSwipeRefreshViewModelFactory
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Story


class DucChapterActivity : AppCompatActivity(), DucLoginDialogFragment.DialogListener {
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
    private val ducImageViewModel: DucImageViewModel by viewModels {
        DucImageViewModelFactory(this)
    }
    private val ducChapterHistoryViewModel: DucChapterHistoryViewModel by viewModels {
        DucChapterHistoryViewModelFactory(this)
    }
    private val ducChapterMarkViewModel: DucChapterMarkViewModel by viewModels {
        DucChapterMarkViewModelFactory(this)
    }
    private val ducSwipeRefreshViewModel: DucSwipeRefreshViewModel by viewModels {
        DucSwipeRefreshViewModelFactory(this)
    }

    val checkReply: MutableList<Any> =mutableListOf(false,-1)
    private var commentReplyId=-1

    private var storyInfo: Story? = null
    private var listOfChapterMarks = mutableListOf<Chapter>()
    private var oldItemTouchHelper:ItemTouchHelper?=null
    private lateinit var dialogRequestLogin: DucLoginDialogFragment

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
            ducChapterViewModel.chaptersByStory.observe(this, Observer { chapters ->
                setViewButtonChapter()
                setConfigButtonChapter()

                //asign chapter history
                mainChapter?.let {
                    ducChapterHistoryViewModel.setChapterHistoryUserSession(
                        it.chapterID ?: return@let
                    )
                }

            })

            setupViewImageOrParagraph()
            prepareDataContenForChapter()
            loadContent()
        }
        // da lay duoc comment tu database
        ducCommentViewModel.commentsByStory.observe(this, Observer { comments ->
            loadComment(comments)
            setConfigButtonComment()
        })

        // da lay duoc chuong danh dau tu database
        ducChapterMarkViewModel.chaptersMarkedByStory.observe(this, Observer { chapterMarks ->
            setButtonChapterMark(chapterMarks)
        })
        //lam moi
        ducSwipeRefreshViewModel.refreshView.observe(this, Observer { refresh ->
            // xoa hieu ung load
            binding.swipeRefreshChapter.isRefreshing = false
            loadContent()
        })

        //
        setConfigCommentDialog()
        setConfigButton()
        setConfigView()
        setDialogRequestLogin()

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

            //lay data tu database
            storyInfo?.let {
                ducChapterViewModel.fetchChaptersByStory(it.storyID ?: numDef)
                //ducCommentViewModel.fetchCommentsByStory(it.storyID ?: numDef)
                ducChapterMarkViewModel.fetchChaptersMarkedByUserSessionAndStory(
                    it.storyID ?: numDef
                )
            }
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

        //reset chapter mark
        storyInfo?.let {
            ducChapterMarkViewModel.fetchChaptersMarkedByUserSessionAndStory(it.storyID ?: numDef)
        }
        //asign chapter history
        mainChapter?.let {
            ducChapterHistoryViewModel.setChapterHistoryUserSession(
                it.chapterID ?: return@let
            )
        }
    }

    fun setPreMainNextChapter() {

        mainChapter = ducChapterViewModel.mainChapter
        previousChapter = ducChapterViewModel.previousChapter
        nextChapter = ducChapterViewModel.nextChapter

    }

    private fun setButtonChapterMark(chapters: List<Chapter>) {

        var isMark = false
        //chapter is mark or not
        mainChapter?.let { main ->
            var chapterMark = chapters.find { it.chapterID == main.chapterID }
            if (chapterMark != null) {
                // co danh dau chuong
                isMark = true
                binding.btnMarkChapterChapter.setImageResource(com.example.myapplication.R.drawable.icon_mark_fill)

            } else {
                // chua danh dau
                isMark = false
                binding.btnMarkChapterChapter.setImageResource(com.example.myapplication.R.drawable.icon_mark_outline)
            }

        }
        // set even mark chapter button
        binding.btnMarkChapterChapter.setOnClickListener {
            //kiem tra co phai tai koan khach
            if (checkThenHandleGuestUser())return@setOnClickListener
            mainChapter?.let { main ->
                isMark = !isMark
                if (isMark) {
                    binding.btnMarkChapterChapter.setImageResource(com.example.myapplication.R.drawable.icon_mark_fill)
                    ducChapterMarkViewModel.addChapterMarkByUserSession(main.chapterID ?: numDef)
                } else {
                    binding.btnMarkChapterChapter.setImageResource(com.example.myapplication.R.drawable.icon_mark_outline)
                    ducChapterMarkViewModel.deleteChapterMarkByUserSession(main.chapterID ?: numDef)

                }

            }


        }
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

    private fun prepareDataContenForChapter() {
        if (storyInfo?.isTextStory?.toBoolean() == true) {
            // prepare  paragraphs from view model
            ducParagraphViewModel.setParagraphsByChapter(mainChapter?.chapterID ?: numDef)


        } else {

            // prepare images from view model
            ducImageViewModel.setImagesByChapter(mainChapter?.chapterID ?: numDef)

        }
    }

    private fun loadContent() {

        storyInfo?.let { storyReal ->
            if (storyReal.isTextStory.toBoolean() == true) {

                loadParagraph()

            } else {

                loadImage()

            }
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
        ducImageViewModel.imagesByChapter.observe(this, Observer { images ->
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
    private fun loadComment(listOfComments: List<DucCommentDataClass>) {
        callLog("Chapteraa",listOfComments.toString())

        var adapterComment = Duc_Comment_Adapter(
            this,
            listOfComments,
            binding,
            getUserIdSession(),
            checkReply
        )
        binding.recyclerContainerCommentChapter.apply {
            adapter = adapterComment
            layoutManager =
                LinearLayoutManager(this@DucChapterActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

        }
        // cuon den vi tri cuoi
        if(adapterComment.itemCount>1) {

            binding.recyclerContainerCommentChapter.smoothScrollToPosition(
                adapterComment.itemCount - 1
            )
        }
        //xoa cai itemtouch cu , vi no co the dan den xung dot
        oldItemTouchHelper?.attachToRecyclerView(null)

        val itemTouchHelper = ItemTouchHelper(adapterComment.getCommentSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(binding.recyclerContainerCommentChapter)

        //gan lai recyclerView vao itemtouche

        oldItemTouchHelper=itemTouchHelper

    }


    private fun setConfigButtonComment() {
        //nguoi dung bam gui
        binding.btnSendCommentUserChapter.setOnClickListener {

            var content = binding.etxtCommentUserChapter.text.toString()
            if (content.isEmpty()) {
                return@setOnClickListener
            }
            if(checkThenHandleGuestUser()) return@setOnClickListener
            // co noi dung
            mainChapter?.let {
                //neu co reply
                if((checkReply[0]as Boolean)==true){
                    // chuyen id cua comment reply
                    ducCommentViewModel.createUserCommnetWithReply(
                        it.storyID, content,checkReply[1]as Int
                    )
                }else{
                    ducCommentViewModel.createUserCommnet(
                        it.storyID, content
                    )
                }
                //khong reply comment
                checkReply[0]=false
                //tat commentReplyInKeyboard
                binding.frameContainerCommentReplyInInputKeyboardChapter.visibility=View.GONE
            }



            //xoa trang editText de nhap comment moi
            binding.etxtCommentUserChapter.setText("")
            // chay lai comment dialog
            storyInfo?.let { ducCommentViewModel.fetchCommentsByStory(it.storyID ?: numDef) }


        }
        binding.btnCancelCommentReplyInInputKeyboardChapter.setOnClickListener{
            //khong reply comment
            checkReply[0]=false
            //tat commentReplyInKeyboard
            binding.frameContainerCommentReplyInInputKeyboardChapter.visibility=View.GONE
        }
    }

    private fun checkThenHandleGuestUser(): Boolean {
        if(isUserCurrentGuest()){
            dialogRequestLogin.show(supportFragmentManager,"login")

            return true
        }else{
            return false
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
    private fun detachItemTouchHelper(itemTouch: ItemTouchHelper.SimpleCallback) {
        //itemTouch?.attachToRecyclerView(null)
    }
    private fun setConfigView() {
        //callLog("chapterAcivity",binding.frameContainerCommentReplyInInputKeyboardChapter.visibility.toString())
        binding.frameContainerCommentReplyInInputKeyboardChapter.visibility=View.GONE
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


            //lay comment tu database khi nut mo comment dialog duoc kich hoat
            storyInfo?.let {
                ducCommentViewModel.fetchCommentsByStory(it.storyID ?: numDef)
            }
//
        }
        binding.swipeRefreshChapter.setOnRefreshListener {
            ducSwipeRefreshViewModel.fetchRefreshView()
        }
    }
    private fun setDialogRequestLogin() {
        dialogRequestLogin= DucLoginDialogFragment()

    }
    override fun onDialogSubmit(input: String) {

    }
}
//    private fun setCommentSelf(
//        commentLayoutBinding: CommentSelfLayoutBinding, comment: DucCommentDataClass
//    ) {
//        setCommentSelfLayoutParams((commentLayoutBinding.root))
//
//        commentLayoutBinding.txtDisplayNameCommentSelfLayout.text =
//            comment.nameUser
//        commentLayoutBinding.txtContentCommentSelfLayout.text = comment.content
//        commentLayoutBinding.imgAvatarUserCommentSelfLayout.loadImgURL(this, comment.imgAvatarUrl)
//        commentLayoutBinding.txtDateCreatedCommentSelfLayout.text = comment.date
//    }
//
//    private fun setCommentOpposite(
//        commentLayoutBinding: CommentOppositeLayoutBinding, comment: DucCommentDataClass
//    ) {
//        setCommentOppositeLayoutParams(commentLayoutBinding.root)
//
//        commentLayoutBinding.txtDisplayNameCommentOppositeLayout.text =
//            comment.nameUser
//        commentLayoutBinding.txtContentCommentOppositeLayout.text = comment.content
//        commentLayoutBinding.imgAvatarUserCommentOppositeLayout.loadImgURL(
//            this,
//            comment.imgAvatarUrl
//        )
//        commentLayoutBinding.txtDateCreatedCommentOppositeLayout.text = comment.date
//    }

//    fun setCommentSelfLayoutParams(view: View) {
//        view.apply {
//            //android:layout_width="wrap_content"
//            //android:layout_height="wrap_content"
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//
//            //android:layout_margin="10dp"
//            layoutParams = ViewGroup.MarginLayoutParams(layoutParams).apply {
//                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
//            }
//
//            //android:layout_gravity="end"
//            layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
//                gravity = Gravity.END
//            }
//            //android:orientation="vertical"
//            if (view is LinearLayout) {
//                view.orientation = LinearLayout.VERTICAL
//            }
//
//        }
//    }
//
//    fun setCommentOppositeLayoutParams(view: View) {
//        view.apply {
//            //android:layout_width="wrap_content"
//            //android:layout_height="wrap_content"
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//
//            //android:layout_margin="10dp"
//            layoutParams = ViewGroup.MarginLayoutParams(layoutParams).apply {
//                setMargins(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
//            }
//
//            //android:layout_gravity="start"
//            layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
//                gravity = Gravity.START
//            }
//
//            //android:orientation="vertical"
//            if (view is LinearLayout) {
//                (view as LinearLayout).orientation = LinearLayout.VERTICAL
//            }
//
//        }
//    }