package com.example.worldstory.view.admin_dialog

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.AddCategoryDialogBinding
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModel
import com.example.worldstory.view_models.admin_viewmodels.GenreViewModelFactory
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.ducutils.getUserIdSession

class AddCategoryDialog : DialogFragment() {

    private lateinit var binding:AddCategoryDialogBinding

    private val genreViewModel: GenreViewModel by activityViewModels {
        GenreViewModelFactory(DatabaseHelper(requireActivity()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding=AddCategoryDialogBinding.inflate(layoutInflater)
            binding.genreViewModel=genreViewModel
            binding.lifecycleOwner=this
            val builder = Builder(it)
            builder.setView(binding.root)
                .setPositiveButton("Add", null) // Hành động sẽ được xử lý trong setOnShowListener
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val dialog = builder.create()
            dialog.setOnShowListener {
                // Lấy nút Add sau khi dialog hiển thị
                val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                addButton.setOnClickListener {
                    // Kiểm tra các trường khi nhấn nút Add
                    var isValid = true
                    if (binding.edAddCate.text.isNullOrEmpty()) {
                        binding.edAddCate.error = "Không được để trống"
                        isValid = false
                    }
                    if(genreViewModel.checkExits(binding.edAddCate.text.toString())==1){
                        binding.edAddCate.error="Nhập tên khác!"
                        isValid=false
                    }
                    if (isValid) {
                        genreViewModel.onAddNewGern(requireContext().getUserIdSession())
                        dialog.dismiss()
                    }
                }
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
