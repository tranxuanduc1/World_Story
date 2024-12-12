package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddChapterBinding
import com.example.worldstory.dat.admin_adapter.PreviewUploadedAdapter
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Chapter
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlinx.coroutines.awaitAll
import java.util.Collections

class EditChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChapterBinding
    private val tempImgs = mutableMapOf<Int, Uri>()
    private lateinit var prvImgAdapter: PreviewUploadedAdapter
    private var index = 0
    private val chapterViewModel: ChapterViewModel by viewModels {
        ChapterViewModelFactory(DatabaseHelper(this))
//        ChapterViewModelFactory(DatabaseHelper.getInstance(this))
    }
    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(DatabaseHelper(this))
    }

    private lateinit var driveService: Drive

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            uris.let {
                for (i in uris) {
                    tempImgs[index++] = i
                    Log.w("size", tempImgs.size.toString())
                }
                prvImgAdapter.updateMap(tempImgs)
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val chapter = intent.getParcelableExtra("chapter", Chapter::class.java)

        binding = ActivityAddChapterBinding.inflate(layoutInflater)


        binding.headerChange.visibility = View.VISIBLE


        binding.chapterViewModel = chapterViewModel
        chapterViewModel.name.value = chapter?.title

        binding.lifecycleOwner = this
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //sự kiến nhấn upload
        binding.uploadImgSrc.setOnClickListener {
            openImagePicker()
        }


        ////recycleview
        prvImgAdapter = PreviewUploadedAdapter(tempImgs)
        binding.prevUploaded.layoutManager = LinearLayoutManager(this)
        binding.prevUploaded.adapter = prvImgAdapter

        //remove
        binding.removeUploaded.setOnClickListener {
            chapterViewModel.arrID.clear()
            chapterViewModel.imgMap.clear()
            tempImgs.clear()
            prvImgAdapter.updateMap(tempImgs)
            index = 0
        }

        //accept
        binding.acceptAddChapter.setOnClickListener {


            val storyID = intent.getIntExtra("storyID", -1)

            if (binding.tenChap.text.isNullOrEmpty()) {
                chapterViewModel.name.value = chapter?.title
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.VISIBLE
                        disableMainScreenInteraction()
                    }
                    try {
                        val deferredTasks =
                            tempImgs.map { (k, v) ->
                                async {
                                    uploadImageToDrive(k, v, chapterViewModel.arrID)
                                }
                            }
                        val rs = deferredTasks.awaitAll()

                        withContext(Dispatchers.Main) {
                            if (rs.all { it }) {
                                chapterViewModel.setImgs()
                                chapterViewModel.arrID.clear()
                                chapterViewModel.updateContent(
                                    chapterID = chapter?.chapterID ?: -1,
                                    storyID = storyID
                                )
                            } else {
                                Log.w("khoong thanh cong", "that bai")

                            }
                            prvImgAdapter.updateMap(emptyMap())
                            tempImgs.clear()
                            index = 0
                            Toast.makeText(
                                this@EditChapterActivity,
                                "Đã cập nhật",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditChapterActivity,
                            e.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {

                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = View.GONE
                            enableMainScreenInteraction()
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.VISIBLE
                        disableMainScreenInteraction()
                    }
                    try {
                        val deferredTasks =
                            tempImgs.map { (k, v) ->
                                async {
                                    uploadImageToDrive(k, v, chapterViewModel.arrID)
                                }
                            }
                        val rs = deferredTasks.awaitAll()

                        withContext(Dispatchers.Main) {
                            if (rs.all { it }) {
                                chapterViewModel.setImgs()
                                chapterViewModel.arrID.clear()
                                chapterViewModel.updateContent(
                                    chapterID = chapter?.chapterID ?: -1,
                                    storyID = storyID
                                )
                            } else {
                                Log.w("khoong thanh cong", "that bai")

                            }
                            prvImgAdapter.updateMap(emptyMap())
                            tempImgs.clear()
                            index = 0
                            Toast.makeText(
                                this@EditChapterActivity,
                                "Đã cập nhật",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditChapterActivity,
                            e.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {

                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = View.GONE
                            enableMainScreenInteraction()
                        }
                    }
                }
            }
        }
        //lấy drive
        driveService = getDriveService(this)

        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    hideKeyboard(this)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        chapterViewModel.name.value = ""
        chapterViewModel.arrID.clear()
        chapterViewModel.imgMap.clear()
    }

    fun openImagePicker() {
        pickImageLauncher.launch(arrayOf("image/jpeg"))
    }


    fun getDriveService(context: Context): Drive {
        // Khởi tạo HTTP transport và JSON factory
        val transport: HttpTransport = NetHttpTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        // Đọc tệp credentials (service account)
        val serviceAccountStream: InputStream = resources.openRawResource(R.raw.cred)

        // Tạo xác thực với tệp credentials JSON
        val credentials = GoogleCredential.fromStream(serviceAccountStream)
            .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE))

        // Khởi tạo Google Drive service với thông tin xác thực từ tài khoản dịch vụ
        return Drive.Builder(transport, jsonFactory, credentials)
            .setApplicationName("World Story")
            .build()
    }

    fun uploadImageToDrive(order: Int, uri: Uri, arrID: MutableMap<Int, String>): Boolean {


        val mediaContent =
            InputStreamContent("image/jpeg", contentResolver.openInputStream(uri))

        val fileMetadata = File()
        fileMetadata.name = "$order" // Tên file sẽ lưu trên Drive

        fileMetadata.parents =
            listOf("1oGZqzVFyIrvOIXB36ybsJuW81-ppjIsp")  // Tải lên thư mục gốc của Drive


        try {
            val file = driveService.files().create(fileMetadata, mediaContent).apply {
                fields = "id, webViewLink"
                mediaHttpUploader.apply {
                    isDirectUploadEnabled = false // Tải lên theo từng phần
                    chunkSize = MediaHttpUploader.MINIMUM_CHUNK_SIZE * 2 // Kích thước mỗi phần
                }
            }
            val uploadedFile = file.execute()
            arrID[order] = uploadedFile.id
            mediaContent.closeInputStream
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("k up dc", "hythtyht")
            mediaContent.closeInputStream
            return false
        } finally {

        }


    }

    @SuppressLint("ClickableViewAccessibility")
    fun disableMainScreenInteraction() {
        val overlay = View(this)
        overlay.setBackgroundColor(Color.parseColor("#80000000")) // Màu đen trong suốt
        overlay.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        // Thêm lớp phủ vào root view
        val rootLayout = findViewById<FrameLayout>(android.R.id.content)
        rootLayout.addView(overlay)

        overlay.setOnTouchListener { v, event ->
            true
        }
    }

    fun enableMainScreenInteraction() {
        // Loại bỏ overlay khi tải lên hoàn tất
        val rootLayout = findViewById<FrameLayout>(android.R.id.content)
        val overlay = rootLayout.getChildAt(rootLayout.childCount - 1)
        if (overlay != null) {
            rootLayout.removeView(overlay)
        }
    }


    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}