package com.kdjj.presentation.viewmodel.others

import androidx.lifecycle.ViewModel
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.request.FetchRemoteLatestRecipeListRequest
import com.kdjj.domain.request.FetchRemotePopularRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val fetchRemoteLatestRecipeListUseCase: UseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>,
    private val fetchRemotePopularRecipeListUseCase: UseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>,
) : ViewModel() {

    fun fetchRemoteLatestRecipeList() {
        // todo : fetchRemoteLatestRecipeList
    }

    fun fetchRemotePopularRecipeList() {
        // todo : fetchRemotePopularRecipeList
    }
}