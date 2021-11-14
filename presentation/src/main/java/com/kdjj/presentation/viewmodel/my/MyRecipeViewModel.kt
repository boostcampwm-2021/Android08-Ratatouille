package com.kdjj.presentation.viewmodel.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.request.FetchLocalFavoriteRecipeListRequest
import com.kdjj.domain.request.FetchLocalLatestRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.view.home.my.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyRecipeViewModel @Inject constructor(
    private val latestRecipeUseCase: UseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>,
    private val favoriteRecipeUseCase: UseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>
) : ViewModel() {

    private val _liveSortType = MutableLiveData(SortType.SORT_BY_TIME)
    val liveSortType: LiveData<SortType> get() = _liveSortType

    private val _liveAddRecipeHasPressed = MutableLiveData<Boolean>()
    val liveAddRecipeHasPressed: LiveData<Boolean> get() = _liveAddRecipeHasPressed

    private val _liveRecipeItemList = MutableLiveData<List<MyRecipeItem>>()
    val liveRecipeItemList: LiveData<List<MyRecipeItem>> get() = _liveRecipeItemList

    fun fetchLocalLatestRecipeList() {
        viewModelScope.launch {
            latestRecipeUseCase(FetchLocalLatestRecipeListRequest(0))
                .onSuccess { latestRecipeList ->
                    Log.d("aaa", latestRecipeList.toString())
                    val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                    _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                }
                .onFailure {
                    //TODO 데이터 로드에 실패했을 경우 처리
                }
        }
    }

    fun fetchLocalFavoriteRecipeList() {

    }

    fun moveToRecipeEditorActivity() {
        _liveAddRecipeHasPressed.value = true
    }

    fun setSortType(sortType: SortType) {
        if (_liveSortType.value != sortType) {
            _liveSortType.value = sortType
        }
    }
}