package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class AddCategoryDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.add_category_dialog, null)
            val cate = view.findViewById<EditText>(R.id.ed_add_cate)
            builder.setView(view)
                .setPositiveButton("Add", null) // Hành động sẽ được xử lý trong setOnShowListener
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val dialog = builder.create()
            dialog.setOnShowListener {
                // Lấy nút Add sau khi dialog hiển thị
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    // Kiểm tra các trường khi nhấn nút Add
                    var isValid = true
                    if (cate.text.isNullOrEmpty()) {
                        cate.error = "Không được để trống"
                        isValid = false
                    }
                    if (isValid) {
                        dialog.dismiss()
                    }
                }
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
