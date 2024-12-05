package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.ChangeUserInforDialogBinding
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class ChangeUserInforDialog:DialogFragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }

    private lateinit var binding:ChangeUserInforDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding=ChangeUserInforDialogBinding.inflate(layoutInflater)
        binding.userViewModel=userViewModel
        val id = arguments?.getInt("id") ?: -1
        val user = userViewModel.getUser(id)

        return activity?.let {
            val builder = Builder(it)
            builder.setTitle("Thay đổi thông tin của ${user?.nickName}")
                .setView(binding.root)
                .setNegativeButton("Cancel" ){dialog,which->dialog.dismiss()}
                .setPositiveButton("Accept"){dialog,_->}
            val dialog = builder.create()
            dialog?.setOnShowListener {
                val acceptBtn=dialog.getButton( AlertDialog.BUTTON_POSITIVE)
                acceptBtn.setOnClickListener{
                  var isValid=true
                    if(!isValidEmail(userViewModel.email.value.toString())){
                        isValid=false
                        binding.chgEmail.error="Sai định dạng email"
                        userViewModel.resetValue()
                    }
                    if(isValid){
                        userViewModel.updateIn4(user)
                        dialog.dismiss()
                    }
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    companion object {
        fun newInstance(userId: Int?): ChangeUserInforDialog {
            val args = Bundle()
            args.putInt("id", userId?:-1)
            val fragment = ChangeUserInforDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}