package com.kdjj.presentation.viewmodel.others

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
) : ViewModel() {

    fun fetchRemoteLatestRecipeList() {
        // todo : fetchRemoteLatestRecipeList
    }

    fun fetchRemotePopularRecipeList() {
        // todo : fetchRemotePopularRecipeList
    }
}