package com.example.worldstory.duc.ducdialog

import android.annotation.SuppressLint
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil3.load
import com.example.myapplication.R
import com.example.myapplication.databinding.AddUserDialogBinding
import com.example.myapplication.databinding.DialogEditInfoUserLayoutBinding
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.duc.ducviewmodel.DucAccountManagerViewModel
import com.example.worldstory.duc.ducviewmodel.DucUserViewModel
import com.example.worldstory.duc.ducviewmodelfactory.DucAccountManagerViewModelFactory
import com.example.worldstory.duc.ducviewmodelfactory.DucUserViewModelFactory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Collections
import kotlin.getValue

class DucEditInfoUserDialogFragment: DialogFragment() {
    private val ducUserViewModel: DucUserViewModel by viewModels {
        DucUserViewModelFactory(requireActivity())
    }
    private lateinit var binding: DialogEditInfoUserLayoutBinding

    private lateinit var driveService: Drive

    private lateinit var uriAvt: Uri

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                uriAvt = uri
                Picasso.get().load(uri).into(binding.avtUserByAdmin)
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //dat hinh anh cho dialog avt

        binding = DialogEditInfoUserLayoutBinding.inflate(layoutInflater)
        driveService = getDriveService(requireContext())
        return activity?.let {
            ducUserViewModel.userLiveData.observe(this, Observer{
                    userLive->
                userLive?.let {
                    binding.avtUserByAdmin.loadImgURL(requireContext(),it.imgAvatar)

                }
            })
            val builder = Builder(it)

            builder.setView(binding.root)
                .setPositiveButton("Add") { dialog, _ ->
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.setOnShowListener {
                val addButton = dialog.getButton(BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    // Kiểm tra các trường khi nhấn nút Add
                    var isValid = true

                    if (::uriAvt.isInitialized == false) {
                        Toast.makeText(
                            requireContext(),
                            "Vui lòng thêm hình đại diện",
                            Toast.LENGTH_SHORT
                        ).show()
                        isValid=false
                    }
                    if (isValid) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                binding.progressBar.visibility = View.VISIBLE
                                addButton.isEnabled=false
                            }
                            try {
                                val isUploadAvt = uploadImageAsynce(uriAvt, ducUserViewModel.avtIdLiveData)
                                if (isUploadAvt) {
                                    ducUserViewModel.updateAvatar()
                                    ducUserViewModel.fetchUserSession()
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Lỗi tải ảnh",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                withContext(Dispatchers.Main) {
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                        }
                    }

                }
            }

            binding.pickImgBtn.setOnClickListener {
                pickImageLauncher.launch(arrayOf("image/jpeg"))
            }


            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
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

    fun uploadImageToDrive(uri: Uri, idAvt: MutableList<String>): Boolean {

        val mediaContent =
            InputStreamContent("image/jpeg", context?.contentResolver?.openInputStream(uri))

        val fileMetadata = File()
        fileMetadata.name = ducUserViewModel.userLiveData.value.toString() // Tên file sẽ lưu trên Drive

        fileMetadata.parents =
            listOf("1HEIAysZ_8pFCRNBsQGbDm0XDDXKdLyJn")  // Tải lên thư mục gốc của Drive


        try {
            val file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            idAvt.add(file.id)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("k up dc", "hythtyht")
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


    @SuppressLint("ClickableViewAccessibility")
    fun disableMainScreenInteraction() {
        val overlay = View(requireContext())
        overlay.setBackgroundColor(Color.parseColor("#80000000")) // Màu đen trong suốt
        overlay.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        // Thêm lớp phủ vào root view
        val rootLayout = view?.findViewById<FrameLayout>(android.R.id.content)
        rootLayout?.addView(overlay)

        overlay.setOnTouchListener { v, event ->
            true
        }
    }

    fun enableMainScreenInteraction() {
        // Loại bỏ overlay khi tải lên hoàn tất
        val rootLayout = view?.findViewById<FrameLayout>(android.R.id.content)
        val overlay = rootLayout?.getChildAt(rootLayout?.childCount?.minus(1) ?: 0)
        if (overlay != null) {
            rootLayout.removeView(overlay)
        }
    }


}