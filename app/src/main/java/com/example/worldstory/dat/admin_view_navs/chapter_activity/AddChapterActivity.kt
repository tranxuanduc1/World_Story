package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddChapterBinding
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
import java.io.InputStream
import java.util.Collections

class AddChapterActivity : AppCompatActivity() {
    private lateinit var pickFolderLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityAddChapterBinding
    private val chapterViewModel: ChapterViewModel by viewModels {
        ChapterViewModelFactory(DatabaseHelper(this))
    }
    private lateinit var googleDriveService: Drive
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uploadImageToDrive(uri)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddChapterBinding.inflate(layoutInflater)
        binding.chapterViewModel=chapterViewModel
        binding.lifecycleOwner=this
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.uploadImgSrc.setOnClickListener{
            openImagePicker()
        }

    }

    fun openImagePicker() {
        pickImageLauncher.launch("image/jpeg")
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

    fun uploadImageToDrive(uri: Uri) {
        val driveService = getDriveService(this)
        val mediaContent =
            InputStreamContent("image/jpeg", contentResolver.openInputStream(uri))

        val fileMetadata = File()
        fileMetadata.name = "UploadedImage.jpg" // Tên file sẽ lưu trên Drive
        fileMetadata.parents = listOf("1oGZqzVFyIrvOIXB36ybsJuW81-ppjIsp")  // Tải lên thư mục gốc của Drive

        // Upload file lên Google Drive
        Thread {
            try {
                val file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webViewLink")
                    .execute()

                val fileId = file.id
                val webViewLink = file.webViewLink

                runOnUiThread() {
                    // Hiển thị link chia sẻ
                    Log.i("Link", webViewLink)
                    Log.i("id", fileId)
                    Toast.makeText(
                        this,
                        "File uploaded. Link: $webViewLink \nid : $fileId",
                        Toast.LENGTH_LONG
                    ).show()

                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread() {
                    Toast.makeText(this, "Error uploading file", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }.start()
    }

}