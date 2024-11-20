package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R

class AddStoryDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.add_story_dialog, null)
            val nameStory: TextView = view.findViewById(R.id.ten_truyen)

            builder.setView(view)
                .setPositiveButton("Next") { dialog, _ ->
                    if (nameStory.text.isNullOrEmpty()) {

                    } else
                        PickCateNewStoryDialog().show(parentFragmentManager, "PickCategories")
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