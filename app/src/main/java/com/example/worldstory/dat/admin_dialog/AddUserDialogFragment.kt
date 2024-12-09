package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.AddUserDialogBinding
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Collections

class AddUserDialogFragment : DialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: AddUserDialogBinding

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
        binding = AddUserDialogBinding.inflate(layoutInflater)
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this
        driveService = getDriveService(requireContext())
        return activity?.let {

            val builder = Builder(it)
            val arr = (roleViewModel.roles.map { it.roleName }).toTypedArray()

            builder.setView(binding.root)
                .setSingleChoiceItems(arr, -1) { dialog, which ->
                    userViewModel.roleID = which+1
                }
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
                    if (userViewModel.roleID<1 || userViewModel.roleID>4) {
                        Toast.makeText(
                            requireContext(),
                            "Chọn 1 vai trò cho user",
                            Toast.LENGTH_LONG
                        ).show()
                        isValid = false
                    }
                    if (binding.newUserName.text.isNullOrEmpty()) {
                        binding.newUserName.error = "Username không được để trống"
                        isValid = false
                    }

                    if (binding.newPassword.text.isNullOrEmpty()) {
                        binding.newPassword.error = "Password không được để trống"
                        isValid = false
                    }
                    if (binding.email.text.isNullOrEmpty()) {
                        binding.email.error = "Password không được để trống"
                        isValid = false
                    }
                    if (!isValidEmail(binding.email.text.toString())) {
                        binding.email.error = "Sai định dạng email"
                        isValid = false
                    }
                    if (binding.cfNewPassword.text.isNullOrEmpty()) {
                        binding.cfNewPassword.error = "Password không được để trống"
                        isValid = false
                    }
                    if (binding.newNickname.text.isNullOrEmpty()) {
                        binding.newNickname.error = "Vui lòng không bỏ trống nickname"
                        isValid = false
                    }
                    if (!binding.newPassword.text.toString()
                            .equals(binding.cfNewPassword.text.toString())
                    ) {
                        binding.cfNewPassword.error = "Không khớp mật khẩu"
                        isValid = false
                    }
                    if (isValid) {
                        lifecycleScope.launch {
                            try {
                                val isUploadAvt = uploadImageAsynce(uriAvt, userViewModel.avtId)
                                if (isUploadAvt) {
                                    userViewModel.onAddUser()
                                    binding.cfNewPassword.text?.clear()
                                    Picasso.get().load(R.drawable.cat).into(binding.avtUserByAdmin)
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Không tạo user được",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
            }

            binding.pickImgBtn.setOnClickListener {
                pickImageLauncher.launch(arrayOf("image/jpeg"))
            }

            binding.removeAvtBtn.setOnClickListener {
                Picasso.get().load(R.drawable.cat).into(binding.avtUserByAdmin)
                uriAvt = "".toUri()
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

    fun uploadImageToDrive(uri: Uri, idAvt:MutableList<String>): Boolean {

        val mediaContent =
            InputStreamContent("image/jpeg", context?.contentResolver?.openInputStream(uri))

        val fileMetadata = File()
        fileMetadata.name = userViewModel.userName.value.toString() // Tên file sẽ lưu trên Drive

        fileMetadata.parents =
            listOf("1HEIAysZ_8pFCRNBsQGbDm0XDDXKdLyJn")  // Tải lên thư mục gốc của Drive


        try {
            val file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute()
            idAvt.add( file.id)
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
}