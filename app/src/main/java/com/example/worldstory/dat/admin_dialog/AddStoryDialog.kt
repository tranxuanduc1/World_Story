package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.AddStoryDialogBinding
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class AddStoryDialog : DialogFragment() {
    private lateinit var binding: AddStoryDialogBinding
    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = AddStoryDialogBinding.inflate(layoutInflater)
            binding.storyViewModel=storyViewModel
            binding.lifecycleOwner=this
            val builder = Builder(it)

            builder.setView(binding.root)
                .setPositiveButton("Next",null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.setOnShowListener {
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener{
                    var isValid=true

                    if(binding.tenTruyen.text.isNullOrEmpty()){
                        binding.tenTruyen.error="Vui lòng không bỏ trống"
                        isValid=false
                    }
                    if(binding.tenTg.text.isNullOrEmpty()){
                        binding.tenTg.error="Vui lòng không bỏ trống"
                        isValid=false
                    }
                    if(binding.moTa.text.isNullOrEmpty()){
                        binding.moTa.error="Vui lòng không bỏ trống"
                        isValid=false
                    }
                    if(isValid){
                        PickCateNewStoryDialog().show(parentFragmentManager,"PickGenreFragment")
                        dialog.dismiss()
                    }
                }

            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}