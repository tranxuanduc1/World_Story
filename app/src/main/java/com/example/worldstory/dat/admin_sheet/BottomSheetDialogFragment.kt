package com.example.worldstory.dat.admin_sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.worldstory.dat.admin_viewmodels.SharedViewModel

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.view.LayoutInflater as LayoutInflater1

class MyBottomSheetFragment : BottomSheetDialogFragment() {
    var i = 1
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater1,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //Chip

        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
        addChipFromTemplate("Kinh dị", chipGroup)
        addChipFromTemplate("Hài hước", chipGroup)
        addChipFromTemplate("Trinh thám", chipGroup)
        addChipFromTemplate("Phiêu lưu", chipGroup)
        addChipFromTemplate("Kịch tính", chipGroup)
        addChipFromTemplate("Kỳ quái", chipGroup)
        addChipFromTemplate("Học đường", chipGroup)
        addChipFromTemplate("Lãng mạn", chipGroup)
        addChipFromTemplate("Bạo lực", chipGroup)
        addChipFromTemplate("Viễn tưởng", chipGroup)

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
            behavior.peekHeight = 300 // Chiều cao khi ở trạng thái collapsed
            behavior.isFitToContents = true // Điều chỉnh theo nội dung
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        sharedViewModel.onFilterBtnClicked()
        super.onDismiss(dialog)
    }

    private fun addChipFromTemplate(text: String, chipGroup: ChipGroup) {

        val chip = layoutInflater.inflate(R.layout.chip, chipGroup, false) as Chip
        chip.text = text
        chip.id = i++

        // Thêm Chip vào ChipGroup
        chip.isChecked = sharedViewModel.isContainChip(text)
        chipGroup.addView(chip)
        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedViewModel.addSeclectedChip(text)
                chip.isChecked = true
            } else {
                sharedViewModel.delSelectedChip(text)
                chip.isChecked = false
            }
        }
    }
}