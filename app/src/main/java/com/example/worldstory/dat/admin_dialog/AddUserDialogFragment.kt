package com.example.worldstory.dat.admin_dialog
import android.app.AlertDialog
import android.app.AlertDialog.*
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class AddUserDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.add_user_dialog, null)
            val userName=view.findViewById<AppCompatEditText>(R.id.et1)
            val pw=view.findViewById<AppCompatEditText>(R.id.et2)
            builder.setView(view)
                .setPositiveButton("Add", null) // Hành động sẽ được xử lý trong setOnShowListener
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
            val dialog= builder.create()
            dialog.setOnShowListener {
                // Lấy nút Add sau khi dialog hiển thị
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    // Kiểm tra các trường khi nhấn nút Add
                    var isValid = true
                    if (userName.text.isNullOrEmpty()) {
                        userName.error = "Username không được để trống"
                        isValid = false
                    }

                    if (pw.text.isNullOrEmpty()) {
                        pw.error = "Password không được để trống"
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