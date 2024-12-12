package com.example.worldstory.dat.admin_view_navs.chapter_activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
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
import com.example.worldstory.model.Story
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
        fun newInstance(idStory: Int, type: Int): ChapterFragment {
            val fragment = ChapterFragment()
            val args = Bundle()
            args.putInt("idStory", idStory)
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }

    private var type: Int = 0
    private lateinit var storyViewModel: StoryViewModel


    private lateinit var chapterAdapter: ChapterAdapter

    private lateinit var binding: FragmentChapterBinding

    private lateinit var driveService: Drive

    private lateinit var uribg: Uri
    private lateinit var uriav: Uri

    private lateinit var story: Story

    // chọn hình bground cho truyện
    private val pickImageForBgLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uribg = uri
                Picasso.get().load(uri).into(binding.imgBground)
            }


        }

    // Chọn hình đại dện cho truyện
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
        type = arguments?.getInt("type") ?: 0
        storyViewModel = StoryViewModelFactory(
            DatabaseHelper(requireContext()),
            type
        ).create(StoryViewModel::class.java)

        binding = FragmentChapterBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.storyViewModel = storyViewModel
        binding.lifecycleOwner = viewLifecycleOwner


        // gán thuộc tính truyện hiện tại
        idStory = arguments?.getInt("idStory")
        storyViewModel.setStoryByID(idStory)
        storyViewModel.setIDStory(idStory)

        story = storyViewModel.getStoryById(idStory ?: -1)!!
        // sk thêm truyện
        binding.addChapter.setOnClickListener {
            onClickAddChapter()
        }

        // load hình
        Picasso.get().load(storyViewModel.storyBgImg.first()).into(binding.imgBground)
        Picasso.get().load(storyViewModel.storyImg.first()).into(binding.avtStory)


        chapterAdapter =
            ChapterAdapter(storyViewModel.chapterListByStory.value?.toMutableList(), storyViewModel)

        binding.chapterList.layoutManager = LinearLayoutManager(requireContext())
        binding.chapterList.adapter = chapterAdapter

        storyViewModel.chapterListByStory.observe(viewLifecycleOwner) { newChapterList ->
            chapterAdapter.updateList(newChapterList)
        }
// view detail
        binding.viewDetails.setOnClickListener {
            if (binding.tomTat.isSingleLine()) {
                binding.tomTat.setSingleLine(false)
            } else
                binding.tomTat.setSingleLine(true)
        }


        // edit description
        binding.editDeBtn.setOnClickListener {
            if (binding.tomTat.visibility == View.VISIBLE) {
                binding.tomTat.visibility = View.GONE
                binding.editDes.visibility = View.VISIBLE
            } else {
                binding.tomTat.visibility = View.VISIBLE
                binding.editDes.visibility = View.GONE
            }

        }

        // edit tên tryện
        binding.editTitleBtn.setOnClickListener {
            if (binding.titleStory.visibility == View.VISIBLE) {
                binding.titleStory.visibility = View.GONE
                binding.editTitle.visibility = View.VISIBLE
            } else {
                binding.editTitle.visibility = View.GONE
                binding.titleStory.visibility = View.VISIBLE
            }

        }
        // edit author
        binding.editAuthorBtn.setOnClickListener {
            if (binding.authorName.visibility == View.VISIBLE) {
                binding.authorName.visibility = View.GONE
                binding.editAuthor.visibility = View.VISIBLE
            } else {
                binding.editAuthor.visibility = View.GONE
                binding.authorName.visibility = View.VISIBLE
            }
        }



        binding.saveChanges.setOnClickListener {
            storyViewModel.storyBgImg.clear()
            storyViewModel.storyImg.clear()
            var uploadAvt = false
            var upload = false
            lifecycleScope.launch {
                try {
                    if (::uriav.isInitialized)
                        uploadAvt = uploadImageAsynce(uriav, storyViewModel.storyImg)
                    if (::uribg.isInitialized)
                        upload = uploadImageAsynce(uribg, storyViewModel.storyBgImg)


                    if (upload && uploadAvt) {
                        storyViewModel.updateStory(story)
                        Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_LONG)
                            .show()
                        storyViewModel.setStoryByID(idStory)
                    } else if (upload) {
                        storyViewModel.updateBackGroundStory(story)
                        Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_LONG)
                            .show()
                        storyViewModel.setStoryByID(idStory)
                    } else if (uploadAvt) {
                        storyViewModel.updateFaceStory(story)
                        Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_LONG)
                            .show()
                        storyViewModel.setStoryByID(idStory)
                    } else {
                        storyViewModel.updateInforStory(story)
                        Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_LONG)
                            .show()
                        storyViewModel.setStoryByID(idStory)
                        storyViewModel.resetValue()
                        storyViewModel.setStoryByID(idStory)
                    }

                } catch (e: Exception) {
                    storyViewModel.setStoryByID(idStory)
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Đã xảy ra lỗi", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        //
        binding.upBackgroundBtn.setOnClickListener {
            pickImageForBgLauncher.launch("image/*")

        }

        binding.upAvtStoryBtn.setOnClickListener {
            pickImageForAvtLauncher.launch("image/*")
        }

        driveService = getDriveService(requireContext())


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        Log.w("start", "yes")
        storyViewModel.fetchAllChaptersAsynce()
//        storyViewModel.chapterListByStory.value = storyViewModel.chapterListByStory.value
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


    fun uploadImageToDrive(uri: Uri, idImg: MutableList<String>): Boolean {

        val mediaContent =
            InputStreamContent("image/jpeg", context?.contentResolver?.openInputStream(uri))

        val fileMetadata = File()
        fileMetadata.name = storyViewModel.title.value.toString() // Tên file sẽ lưu trên Drive

        fileMetadata.parents =
            listOf("1HEIAysZ_8pFCRNBsQGbDm0XDDXKdLyJn")  // Tải lên thư mục gốc của Drive


        try {
            val file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            idImg.add(file.id)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }


    }

    suspend fun uploadImageAsynce(uri: Uri, idAvt: MutableList<String>): Boolean {

        return withContext(Dispatchers.IO) {
            try {
                uploadImageToDrive(uri, idAvt)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}