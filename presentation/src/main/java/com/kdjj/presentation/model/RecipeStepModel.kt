package com.kdjj.presentation.model

import androidx.lifecycle.MutableLiveData

data class RecipeStepModel(
    val liveName: MutableLiveData<String> = MutableLiveData(""),
    val liveType: MutableLiveData<Int> = MutableLiveData(0),
    val liveDescription: MutableLiveData<String> = MutableLiveData(""),
    val liveImgPath: MutableLiveData<String> = MutableLiveData(""),
    val liveTimerMin: MutableLiveData<Int> = MutableLiveData(0),
    val liveTimerSec: MutableLiveData<Int> = MutableLiveData(0),
)