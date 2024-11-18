package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class PickCateNewStoryDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            val items = arrayOf("Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường","Kinh Dị", "Trinh Thám", "Phiêu lưu", "Đời thường")
            val checkedItems = booleanArrayOf(false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false,false, false, false, false)
            //
            //Cài các option
            //
            builder.setTitle("Chọn thể loại cho truyện")
                .setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                }

            builder.setPositiveButton("Add") { dialog, _ ->
                    dialog.cancel() }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}