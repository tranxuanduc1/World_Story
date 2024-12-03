package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChapterBinding
import com.example.worldstory.dat.admin_adapter.ChapterAdapter
import com.example.worldstory.dat.admin_dialog.EditTitleDialog
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Collections


class ChapterFragment() : Fragment() {
    private var idStory: Int? = 0

    companion object {
        fun newInstance(idStory: Int): ChapterFragment {
            val fragment = ChapterFragment()
            val args = Bundle()
            args.putInt("idStory", idStory)
            fragment.arguments = args
            return fragment
        }
    }

    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }

    private val chapterViewModel: ChapterViewModel by viewModels {
        StoryViewModelFactory(DatabaseHelper(requireContext()))
    }
    private lateinit var chapterAdapter: ChapterAdapter

    private lateinit var binding: FragmentChapterBinding

    private lateinit var driveService: Drive

    private lateinit var uribg: Uri
    private lateinit var uriav: Uri

    private val pickImageForBgLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uribg = uri
                Picasso.get().load(uri).into(binding.imgBground)
            }


        }
    private val pickImageForAvtLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uriav = uri
                Picasso.get().load(uri).into(binding.avtStory)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChapterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStory = arguments?.getInt("idStory")


        binding.storyViewModel = storyViewModel
        binding.lifecycleOwner = this
        storyViewModel.setStoryByID(idStory)

        binding.addChapter.setOnClickListener {
            onClickAddChapter()
        }

        Picasso.get().load(storyViewModel.storyBgImg.value).into(binding.imgBground)
        Picasso.get().load(storyViewModel.storyImg.value).into(binding.avtStory)


        chapterAdapter = ChapterAdapter(storyViewModel.chapterListByStory.value)

        binding.chapterList.layoutManager = LinearLayoutManager(requireContext())
        binding.chapterList.adapter = chapterAdapter

        storyViewModel.chapterListByStory.observe(viewLifecycleOwner){newChapterList->
            chapterAdapter.updateList(newChapterList)
        }

        storyViewModel.setIDStory(idStory)


        binding.editTitleBtn.setOnClickListener {
            EditTitleDialog().show(parentFragmentManager, "Edit")
        }


        binding.upBackgroundBtn.setOnClickListener {
            pickImageForBgLauncher.launch("image/*")

        }

        binding.upAvtStoryBtn.setOnClickListener {
            pickImageForAvtLauncher.launch("image/*")
        }

        driveService = getDriveService(requireContext())

    }


    fun onClickAddChapter() {

        storyViewModel.setIDStory(idStory)

        val intent = if (storyViewModel.isText.value == true)
            Intent(requireContext(), AddTextChapterActivity::class.java)
        else
            Intent(requireContext(), AddChapterActivity::class.java)
        intent.putExtra("storyID", idStory)
        startActivity(intent)
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


    fun uploadImageToDrive(uri: Uri, id: MutableLiveData<String>): Boolean {

        val mediaContent =
            InputStreamContent("image/jpeg", context?.contentResolver?.openInputStream(uri))

        val fileMetadata = File()
        // Tên file sẽ lưu trên Drive

        fileMetadata.parents =
            listOf("1oGZqzVFyIrvOIXB36ybsJuW81-ppjIsp")  // Tải lên thư mục gốc của Drive


        try {
            val file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            id.value = file.id
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("k up dc", "hythtyht")
            return false
        }
    }
}