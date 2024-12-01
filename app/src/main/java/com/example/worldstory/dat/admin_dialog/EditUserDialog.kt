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
import com.example.myapplication.databinding.EditUserDialogBinding
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.User

class EditUserDialog : DialogFragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: EditUserDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=EditUserDialogBinding.inflate(layoutInflater)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            binding.userViewModel=userViewModel
            binding.lifecycleOwner  =this
            val userID=arguments?.getInt("userID")
            val user=userViewModel.getUser(userID!!)
            userViewModel.apply {
                userName.value=user.userName
                nickName.value=user.nickName
                roleID=user.roleID
            }

            binding.changePw.setOnClickListener{
                if(binding.changePw.isChecked){
                    binding.newPassword.visibility=View.VISIBLE
                    binding.newPassword.text?.clear()
                    binding.cfNewPassword.visibility=View.VISIBLE
                    binding.cfNewPassword.text?.clear()
                }
                else{
                    binding.newPassword.visibility=View.GONE
                    binding.cfNewPassword.visibility=View.GONE
                }
            }
        return activity?.let {

            val builder = Builder(it)
            val arr = (roleViewModel.roles.map { it.roleName }).toTypedArray()
            builder.setView(binding.root)
                .setSingleChoiceItems(arr, -1) { dialog, which ->
                    userViewModel.roleID = which
                }
                .setPositiveButton("Confirm") { dialog, _ ->
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
                    if(binding.changePw.isChecked){
                        if(!binding.newPassword.text?.equals(binding.cfNewPassword.text)!!){
                            binding.cfNewPassword.error="Không khớp"
                            isValid=false
                        }
                        if(binding.cfNewPassword.text.isNullOrEmpty()){
                            binding.cfNewPassword.error="Không được bỏ trống"
                            isValid=false
                        }
                        if(binding.newPassword.text.isNullOrEmpty()){
                            binding.newPassword.error="Không được bỏ trống"
                            isValid=false
                        }
                        if(isValid){
                            val newPassword=binding.cfNewPassword.text.toString()
//                            val newUsername = binding.new
                            userViewModel.passWord.value=binding.cfNewPassword.text.toString()

                        }
                    }
                    if(isValid){

//                        userViewModel.updatetUser()
                        dialog.dismiss()
                    }
                }
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}