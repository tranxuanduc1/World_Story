package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddChapterBinding
import com.example.worldstory.dat.admin_adapter.PreviewUploadedAdapter
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlinx.coroutines.awaitAll
import java.util.Collections

class AddChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChapterBinding
    private val tempImgs = mutableMapOf<Int, Uri>()
    private lateinit var prvImgAdapter: PreviewUploadedAdapter
    private var index = 0
    private val chapterViewModel: ChapterViewModel by viewModels {
        ChapterViewModelFactory(DatabaseHelper(this))
//        ChapterViewModelFactory(DatabaseHelper.getInstance(this))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddChapterBinding.inflate(layoutInflater)
        binding.chapterViewModel = chapterViewModel
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
                binding.tenChap.error = "Không được bỏ trống"
            } else {
                CoroutineScope(Dispatchers.IO).launch {

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
                            chapterViewModel.onAddChapter(storyID = storyID)
                        } else {
                            Log.w("khoong thanh cong", "that bai")
                        }
                        tempImgs.clear()
                        index=0
                    }

                }

            }

        }

        //lấy drive
        driveService = getDriveService(this)

        binding.topAppBar.setNavigationOnClickListener{onBackPressedDispatcher.onBackPressed()}

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
            val file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            arrID[order] = file.id
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("k up dc", "hythtyht")
            return false
        }


    }


}