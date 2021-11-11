package com.kdjj.presentation.model

import android.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.presentation.view.dialog.OnDialogConfirmListener

class ConfirmDialogModel(
    private val dialog: AlertDialog,
    title: String,
    content: String,
    private val listener: OnDialogConfirmListener
) {
    val liveTitle: LiveData<String> = MutableLiveData(title)
    val liveContent: LiveData<String> = MutableLiveData(content)

    fun onConfirm() {
        dialog.dismiss()
        listener.onConfirm()
    }
}