package com.example.worldstory.dat.admin_sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMyBottomSheetBinding
import com.example.worldstory.dat.admin_viewmodels.GenreViewModel
import com.example.worldstory.dat.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel
import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.model.Genre

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.view.LayoutInflater as LayoutInflater1

class MyBottomSheetFragment : BottomSheetDialogFragment() {
    var i = 1
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentMyBottomSheetBinding
    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateView(
        inflater: LayoutInflater1,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //Chip

        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
        addChipFromTemplate(genreViewModel.genres.value ?: emptyList(), chipGroup)

        val filterBtn = view.findViewById<Button>(R.id.filter_btn)
        filterBtn.setOnClickListener {
            sharedViewModel.onFilterBtnClicked()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED // Trạng thái ban đầu
            behavior.peekHeight = 300
            behavior.isFitToContents = true
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        sharedViewModel.onFilterBtnClicked()
        super.onDismiss(dialog)
    }

    private fun addChipFromTemplate(gerns: List<Genre>, chipGroup: ChipGroup): Boolean {

        if (gerns.isEmpty()) {
            return false
        } else {
            for (i in gerns) {
                val chip = layoutInflater.inflate(R.layout.chip, chipGroup, false) as Chip
                chip.text = i.genreName
                chip.id = i.genreID!!
                // Thêm Chip vào ChipGroup
                chip.isChecked = sharedViewModel.isContainChip(i.genreID)
                chipGroup.addView(chip)
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        sharedViewModel.addSeclectedChip(i.genreID,i.genreName)
                        chip.isChecked = true
                    } else {
                        sharedViewModel.delSelectedChip(i.genreID)
                        chip.isChecked = false
                    }
                }
            }
        }
        return true
    }
}