package com.example.worldstory.admin_dialog

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class EditTitleDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.edit_title_dialog, null)


            builder.setView(view)
                .setPositiveButton("Accept") { dialog, _ ->
                   dialog.cancel()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}