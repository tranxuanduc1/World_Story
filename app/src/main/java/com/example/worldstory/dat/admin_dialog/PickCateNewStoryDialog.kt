package com.example.worldstory.dat.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.PickingCategoriesForNewStoryBinding
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.StoryViewModel
import com.example.worldstory.dat.admin_viewmodels.StoryViewModelFactory
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.getUserIdSession
import com.google.android.material.snackbar.Snackbar

class PickCateNewStoryDialog : DialogFragment() {
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private val storyViewModel: StoryViewModel by activityViewModels {
        StoryViewModelFactory(DatabaseHelper(requireActivity()))
    }
    private lateinit var binding: PickingCategoriesForNewStoryBinding
    private val checkedGenreId= mutableListOf<Int>()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding=PickingCategoriesForNewStoryBinding.inflate(layoutInflater)

        return activity?.let {
            val builder = Builder(it)
            //
            //Cài các option
            //
            val falseArr = BooleanArray(genreViewModel.genres.value?.size ?: 0) { false }
            val genreList: List<String> =
                genreViewModel.genres.value?.map { it.genreName } ?: emptyList()
            val checkedGenres = mutableListOf<String>()
            builder.setTitle("Chọn thể loại cho truyện")
                .setMultiChoiceItems(genreList.toTypedArray(), falseArr) { _, which, ischecked ->
                    if(ischecked){
                        checkedGenres.add(genreList.get(which))
                    }else
                    {
                        checkedGenres.remove(genreList.get(which))
                    }
                }
            builder.setPositiveButton("Add", null)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.setOnShowListener {
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    storyViewModel.genreIDList.value = setIdList(checkedGenres)

                    if (storyViewModel.genreIDList.value?.isEmpty() == true) {
                        val sb=Snackbar.make(
                            dialog?.window?.decorView?.rootView ?: binding.root,
                            "Vui lòng chọn ít nhất 1 thể loại!",
                            Snackbar.LENGTH_LONG
                        )
                        sb.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.flame)) // Màu nền
                        sb.duration=500
                        sb.show()
                    } else {
                        storyViewModel.onAddNewStory(requireContext().getUserIdSession())
                        dialog.dismiss()
                    }
                }

            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun setIdList(genreNameList:List<String>):List<Int>{
        val genreIds= mutableListOf<Int>()
        genreNameList.forEach{
            g->genreIds.add(genreViewModel.genres.value?.filter {it.genreName==g  }?.first()?.genreID?:-1)
        }
        return genreIds
    }
}