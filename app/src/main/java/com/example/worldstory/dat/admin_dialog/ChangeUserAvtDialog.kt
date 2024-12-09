package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ChangeAvtDialogBinding
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.User
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


class ChangeUserAvtDialog : DialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: ChangeAvtDialogBinding
    private var id = -1

    private lateinit var driveService: Drive

    private lateinit var uriAvt: Uri
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                uriAvt = uri
                Picasso.get().load(uri).into(binding.avtUserByAdmin)
            }
        }

    private lateinit var user: User
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        driveService = getDriveService(requireContext())
        binding = ChangeAvtDialogBinding.inflate(layoutInflater)
        id = arguments?.getInt("id") ?: -1
        user = userViewModel.getUser(id)!!
        Picasso.get().load(user?.imgAvatar).into(binding.avtUserByAdmin)
        return activity?.let {
            val builder = Builder(it)

            builder.setTitle("Đổi avatar của ${user?.nickName}").setView(binding.root)
                .setPositiveButton("Accept") { dialog, _ ->
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            binding.pickImgBtn.setOnClickListener {
                pickImageLauncher.launch(arrayOf("image/jpeg"))
            }
            binding.removeAvtBtn.setOnClickListener {
                Picasso.get().load(user?.imgAvatar).into(binding.avtUserByAdmin)
                uriAvt = "".toUri()
            }
            val dialog = builder.create()


            val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)

            dialog.setOnShowListener {
                val acceptButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                acceptButton.setOnClickListener {
                    if (uriAvt != "".toUri()) {
                        lifecycleScope.launch {
                            try {
                                val isUploadAvt = uploadImageAsynce(uriAvt, userViewModel.avtId)
                                if (isUploadAvt) {
                                    userViewModel.updateAvt(user)
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Không tạo user được",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                    userViewModel.resetValue()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(id: Int?): ChangeUserAvtDialog {
            val args = Bundle()
            args.putInt("id", id ?: -1)
            val fragment = ChangeUserAvtDialog()
            fragment.arguments = args
            return fragment
        }
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
        fileMetadata.name = user.userName  // Tên file sẽ lưu trên Drive

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
}