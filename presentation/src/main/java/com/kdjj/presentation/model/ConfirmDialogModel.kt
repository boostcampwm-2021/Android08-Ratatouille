package com.kdjj.presentation.model

import android.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

internal class ConfirmDialogModel(
    private val dialog: AlertDialog,
    title: String,
    content: String,
    private val onConfirmListener: () -> Unit,
    val showCancel: Boolean,
    private val onCancelListener: (() -> Unit)?
) {
    val liveTitle: LiveData<String> = MutableLiveData(title)
    val liveContent: LiveData<String> = MutableLiveData(content)

    fun onConfirm() {
        dialog.dismiss()
        onConfirmListener()
    }

    fun onCancel() {
        dialog.dismiss()
        onCancelListener?.invoke()
    }
}