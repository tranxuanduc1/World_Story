package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddChapterBinding
import com.example.myapplication.databinding.ActivityAddTextChapterBinding
import com.example.worldstory.dat.admin_adapter.PreviewTextFileAdapter
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Collections

class AddTextChapterActivity : AppCompatActivity() {

    private val chapterViewModel: ChapterViewModel by viewModels {
        ChapterViewModelFactory(DatabaseHelper(this))
    }
    private lateinit var prvTxtAdapter: PreviewTextFileAdapter
    private lateinit var openDocumentLauncher: ActivityResultLauncher<Array<String>>
    private var index = 0
    private val tempFile = mutableMapOf<Int, Uri>()
    private val contentMap = mutableMapOf<Int, String>()
    private lateinit var binding: ActivityAddTextChapterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTextChapterBinding.inflate(layoutInflater)
        binding.chapterViewModel = chapterViewModel
        binding.lifecycleOwner = this

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        openDocumentLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri != null) {
                    // Upload file lên Drive tại đây
                    tempFile[index++] = uri
                }
                prvTxtAdapter.update(tempFile)
            }

        prvTxtAdapter = PreviewTextFileAdapter(tempFile)
        binding.prevUploadedTxt.layoutManager = LinearLayoutManager(this)
        binding.prevUploadedTxt.adapter = prvTxtAdapter

        //remove all
        binding.removeUploadedTxt.setOnClickListener {
            index = 0
            tempFile.clear()
            prvTxtAdapter.update(tempFile)
        }

        //accept
        binding.acceptAddChapterTxt.setOnClickListener {
            if (binding.tenChap.text.isNullOrEmpty()) {
                binding.tenChap.error = "Không được bỏ trống"
            } else {
                val storyID = intent.getIntExtra("storyID", -1)
                for (s in tempFile) {
                    contentMap[s.key] = readTextFromUri(s.value, contentResolver) ?: "empty !!"
                    chapterViewModel.onAddTextChapter(storyID, contentMap)
                }
                contentMap.clear()
                tempFile.clear()
                index = 0
            }
        }

        //sk upload
        binding.uploadTextSrc.setOnClickListener {
            Log.w("open", "ooo")
            try {
                openDocumentLauncher.launch(arrayOf("text/plain"))
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("open", e.message.toString())
            }
        }

        binding.topAppBar.setNavigationOnClickListener{onBackPressedDispatcher.onBackPressed()}

    }

    fun readTextFromUri(uri: Uri, contentResolver: ContentResolver): String? {
        var result: String? = null
        try {
            // Mở InputStream từ Uri
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            inputStream?.let {
                // Đọc dữ liệu từ InputStream
                val bufferedReader = BufferedReader(InputStreamReader(it))
                val stringBuilder = StringBuilder()
                var line: String?

                // Đọc từng dòng và ghép lại thành String
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }

                result = stringBuilder.toString()
                bufferedReader.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ReadUri", "Error reading file", e)
        }

        return result
    }


    // Khởi tạo Google Drive API client
    fun initDriveService(credential: GoogleCredential): Drive {
        val transport = NetHttpTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

        return Drive.Builder(transport, jsonFactory, credential)
            .setApplicationName("YourApp")
            .build()
    }
}