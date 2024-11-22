package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.PickingCategoriesForNewStoryBinding
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.google.android.material.snackbar.Snackbar

class PickCateNewStoryDialog : DialogFragment() {
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: PickingCategoriesForNewStoryBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = Builder(it)
            //
            //Cài các option
            //
            val falseArr = BooleanArray(genreViewModel.genres.value?.size ?: 0) { false }
            val genreList: List<String> =
                genreViewModel.genres.value?.map { it.genreName } ?: emptyList()
            val checkedGenres = mutableListOf<Int>()
            builder.setTitle("Chọn thể loại cho truyện")
                .setMultiChoiceItems(genreList.toTypedArray(), falseArr) { _, which, ischecked ->
                    checkedGenres.add(genreViewModel.getIDbyName(genreList.get(which)))
                }
            builder.setPositiveButton("Add", null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.setOnShowListener {
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    var isValid = true
                    if (genreList.isEmpty()) {
                        Snackbar.make(
                            binding.root.findViewById(android.R.id.content),
                            "Vui lòng chọn ít nhất 1 thể loại!",
                            Snackbar.LENGTH_LONG
                        ).show()
                        isValid = false
                    } else {
                        storyViewModel.onAddNewStory()
                        dialog.dismiss()
                    }
                }

            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}