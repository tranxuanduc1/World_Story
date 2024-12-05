package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.ChangePwDialogBinding
import com.example.myapplication.databinding.ChangeUserInforDialogBinding
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class ChangeUserPasswordDialog : DialogFragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }

    private lateinit var binding: ChangePwDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ChangePwDialogBinding.inflate(layoutInflater)
        binding.userViewModel = userViewModel
        val id = arguments?.getInt("id") ?: -1
        val user = userViewModel.getUser(id)

        return activity?.let {
            val builder = Builder(it)
            builder.setTitle("Thay đổi mật khẩu của ${user?.nickName}")
                .setView(binding.root)
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .setPositiveButton("Accept") { dialog, _ -> }
            val dialog = builder.create()
            dialog?.setOnShowListener {
                val acceptBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                acceptBtn.setOnClickListener {
                    var isValid = true
                    if (!userViewModel.checkPassword(
                            binding.oldPw.text.toString(),
                            user?.hashedPw ?: ""
                        )
                    ) {
                        isValid = false
                        binding.oldPw.error="Sai mật khẩu"
                        userViewModel.resetValue()
                    }
                    else if (!binding.cfPw.text.toString().equals(binding.newPw.text.toString())) {
                        isValid = false
                        binding.cfPw.error = "Không khớp"
                        userViewModel.resetValue()
                    }

                    else if (isValid) {
                        userViewModel.passWord.value = binding.newPw.text.toString()
                        userViewModel.updatetUserPw(user)
                        dialog.dismiss()
                    }
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    companion object {
        fun newInstance(userId: Int?): ChangeUserPasswordDialog {
            val args = Bundle()
            args.putInt("id", userId ?: -1)
            val fragment = ChangeUserPasswordDialog()
            fragment.arguments = args
            return fragment
        }
    }

}