package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.worldstory.dat.admin_viewmodels.RoleViewModel
import com.example.worldstory.dat.admin_viewmodels.RoleViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.UserViewModel
import com.example.worldstory.dat.admin_viewmodels.UserViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class ChangeRole : DialogFragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val roleViewModel: RoleViewModel by activityViewModels {
        RoleViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val id = arguments?.getInt("id") ?: -1
        val user = userViewModel.getUser(id)

        val role = roleViewModel.getRole(id)
        return activity?.let {

            val builder = Builder(it)
            val arr = (roleViewModel.roles.map { it.roleName }).toTypedArray()
            var roleId=-1
            builder.setTitle("Thay đổi vai trò của ${user?.nickName}")
                .setSingleChoiceItems(arr, -1) { dialog, which ->
                    roleId = which+1
                }
                .setPositiveButton("Accept"){dialog,_->

                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }



            val dialog = builder.create()

            dialog.setOnShowListener {
                val acceptBtn=dialog.getButton( AlertDialog.BUTTON_POSITIVE)
                acceptBtn.setOnClickListener{
                    if(roleId==user?.roleID ||roleId==-1)
                        dialog.dismiss()
                    else{
                        userViewModel.roleID=roleId
                        userViewModel.updatetUserRole(user)
                        dialog.dismiss()
                    }
                }
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    companion object {
        fun newInstance(userId: Int?): ChangeRole {
            val args = Bundle()
            args.putInt("id", userId?:-1)
            val fragment = ChangeRole()
            fragment.arguments = args
            return fragment
        }
    }
}