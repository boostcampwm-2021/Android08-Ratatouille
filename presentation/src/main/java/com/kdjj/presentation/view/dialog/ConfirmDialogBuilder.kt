package com.kdjj.presentation.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.kdjj.presentation.databinding.DialogConfirmBinding
import com.kdjj.presentation.model.ConfirmDialogModel

internal object ConfirmDialogBuilder {

    fun create(
        context: Context,
        title: String,
        content: String,
        onConfirmListener: () -> Unit
    ) {
        val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        binding.model = ConfirmDialogModel(dialog, title, content, onConfirmListener)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}