package com.example.worldstory.dat.admin_dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.EditGenreOfStoryDialogBinding
import com.example.worldstory.dat.admin_adapter.GenrePickAdapter
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper

class EditGenredOfStoryDialog(
    private val genreViewModel: GenreViewModel,
    private val storyViewModel: StoryViewModel
) : DialogFragment() {

    private lateinit var binding: EditGenreOfStoryDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = EditGenreOfStoryDialogBinding.inflate(layoutInflater)


            val adapter = GenrePickAdapter(storyViewModel, genreViewModel)
            binding.genrePick.layoutManager = LinearLayoutManager(requireContext())
            binding.genrePick.adapter = adapter

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
                .setPositiveButton("Accept") { _, _ -> }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }


            val dialog = builder.create()

            dialog.setOnShowListener {
                val acceptBtn = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                acceptBtn.setOnClickListener {
                    if (storyViewModel.genreEditList.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Vui lòng chọn tối thiểu 1 thể loại cho truyện",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        storyViewModel.updateGenres()
                        dialog.dismiss()
                    }
                }

            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }




}