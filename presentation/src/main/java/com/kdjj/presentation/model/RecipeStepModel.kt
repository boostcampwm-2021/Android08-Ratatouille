package com.kdjj.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class RecipeStepModel(
    val liveName: MutableLiveData<String>,
    val liveType: MutableLiveData<Int> = MutableLiveData(0),
    val liveDescription: MutableLiveData<String>,
    val liveImgPath: MutableLiveData<String> = MutableLiveData(""),
    val liveTimerMin: MutableLiveData<Int>,
    val liveTimerSec: MutableLiveData<Int>,

    val liveNameState: LiveData<Boolean>,
    val liveDescriptionState: LiveData<Boolean>,
    val liveTimerMinState: LiveData<Boolean>,
    val liveTimerSecState: LiveData<Boolean>
)