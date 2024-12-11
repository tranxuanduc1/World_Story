package com.example.worldstory.duc.ducdialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.DialogRequestLoginBinding
import com.example.worldstory.duc.ducactivity.DucLoginActivity
import com.example.worldstory.duc.ducutils.toActivity


class DucLoginDialogFragment: DialogFragment() {
    interface DialogListener {
        fun onDialogSubmit(input: String)
    }

    private var listener: DialogListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Gắn listener vào activity hoặc fragment
        listener = when {
            parentFragment is DialogListener -> parentFragment as DialogListener
            context is DialogListener -> context
            else -> null
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null // Xóa listener để tránh memory leaks
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder= AlertDialog.Builder(requireContext())

        val binding= DialogRequestLoginBinding.inflate(layoutInflater)
        val view = binding.root
        binding.btnLoginDialogComfirmLogin.setOnClickListener{
            var context=when {
                parentFragment is DialogListener -> requireContext()
                context is DialogListener -> context
                else -> null
            }
            context?.toActivity(DucLoginActivity::class.java)
        }
        binding.btnBackDialogComfirmLogin.setOnClickListener{
            dismiss()
        }
        builder.setView(view)

        return builder.create()
    }

}