package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.AddUserDialogBinding
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.model.User

class AddUserDialogFragment : DialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: AddUserDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AddUserDialogBinding.inflate(LayoutInflater.from(context))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.userViewModel=userViewModel
        binding.lifecycleOwner  =this
        return activity?.let {

            val builder = Builder(it)
            val arr = (roleViewModel.roles.map { it.roleName }).toTypedArray()

            builder.setView(binding.root)
                .setSingleChoiceItems(arr, -1) { dialog, which ->
                    userViewModel.roleID = which
                }
                .setPositiveButton("Add") { dialog, _ ->
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.setOnShowListener{
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener{
                    // Kiểm tra các trường khi nhấn nút Add
                    var isValid = true

                    if (binding.newUserName.text.isNullOrEmpty()) {
                        binding.newUserName.error = "Username không được để trống"
                        isValid = false
                    }

                    if (binding.newPassword.text.isNullOrEmpty()) {
                        binding.newPassword.error = "Password không được để trống"
                        isValid = false
                    }

                    if (binding.newNickname.text.isNullOrEmpty()) {
                        binding.newNickname.error = "Vui lòng không bỏ trống nickname"
                        isValid = false
                    }

                    if (isValid) {
                        userViewModel.onAddUser()
                        Log.i("insert","da insert")
                        Toast.makeText(requireContext(),"Quan sat",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
            binding.cancelAddNewUser.setOnClickListener {
                dialog.dismiss()
            }
            binding.addUserBtn.setOnClickListener {
                userViewModel.onAddUser()
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}