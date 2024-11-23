package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.AddChapterDialogBinding
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModel
import com.example.worldstory.dat.admin_viewmodels.ChapterViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class AddChapterDialog : DialogFragment() {
    private lateinit var binding: AddChapterDialogBinding

    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val chapterViewModel: ChapterViewModel by activityViewModels {
        ChapterViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = AddChapterDialogBinding.inflate(layoutInflater)
            binding.chapterViewModel = chapterViewModel
            binding.lifecycleOwner = this
            binding.scrollView.visibility =View.GONE
            val builder = Builder(it)
            builder.setView(binding.root)
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel") {dialog,_->
                    dialog.dismiss()
                }
            val dialog=builder.create()
            dialog.setOnShowListener {
                val positiveBtn=dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveBtn.setOnClickListener{
                    var isValid=true
                    if(binding.tenChap.text.isNullOrEmpty()){
                        binding.tenChap.error="Không được bỏ trống"
                        isValid=false
                    }
                    if (isValid){
                        dialog.dismiss()
                    }
                }
            }
            dialog
        }?:throw IllegalStateException("Activity cannot be null")
    }
    fun onClickUpLoad(){

    }
}