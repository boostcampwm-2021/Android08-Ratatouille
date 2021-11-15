package com.kdjj.presentation.viewmodel.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchLocalFavoriteRecipeListRequest
import com.kdjj.domain.model.request.FetchLocalLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchLocalTitleRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.model.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyRecipeViewModel @Inject constructor(
    private val latestRecipeUseCase: UseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>,
    private val favoriteRecipeUseCase: UseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>,
    private val titleRecipeUseCase: UseCase<FetchLocalTitleRecipeListRequest, List<Recipe>>
) : ViewModel() {

    private val _liveSortType = MutableLiveData<SortType>()
    val liveSortType: LiveData<SortType> get() = _liveSortType

    private val _liveAddRecipeHasPressed = MutableLiveData<Event<Unit>>()
    val liveAddRecipeHasPressed: LiveData<Event<Unit>> get() = _liveAddRecipeHasPressed

    private val _liveRecipeItemList = MutableLiveData<List<MyRecipeItem>>(listOf())
    val liveRecipeItemList: LiveData<List<MyRecipeItem>> get() = _liveRecipeItemList

    init {
        setSortType(SortType.SORT_BY_TIME)
    }

    fun fetchMoreRecipeData(page: Int) {
        when (_liveSortType.value) {
            SortType.SORT_BY_TIME -> fetchLocalLatestRecipeList(page)
            SortType.SORT_BY_FAVORITE -> fetchLocalFavoriteRecipeList(page)
            else -> fetchLocalTitleRecipeList(page)
        }
    }

    private fun fetchLocalLatestRecipeList(page: Int) {
        viewModelScope.launch {
            latestRecipeUseCase(FetchLocalLatestRecipeListRequest(page))
                .onSuccess { latestRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_TIME) {
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_TIME
                    }
                }
                .onFailure {
                    //TODO 데이터 로드에 실패했을 경우 처리
                }
        }
    }

    private fun fetchLocalFavoriteRecipeList(page: Int) {
        viewModelScope.launch {
            favoriteRecipeUseCase(FetchLocalFavoriteRecipeListRequest(page))
                .onSuccess { favoriteRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_FAVORITE) {
                        val myRecipeList = favoriteRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = favoriteRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_FAVORITE
                    }
                }
                .onFailure {
                    //TODO 데이터 로드에 실패했을 경우 처리
                }
        }
    }

    private fun fetchLocalTitleRecipeList(page: Int) {
        viewModelScope.launch {
            titleRecipeUseCase(FetchLocalTitleRecipeListRequest(page))
                .onSuccess { titleRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_NAME) {
                        val myRecipeList = titleRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = titleRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_NAME
                    }
                }
                .onFailure {
                    //TODO 데이터 로드에 실패했을 경우 처리
                }
        }
    }

    fun moveToRecipeEditorActivity() {
        _liveAddRecipeHasPressed.value = Event(Unit)
    }

    fun setSortType(sortType: SortType) {
        if (_liveSortType.value != sortType) {
            when (sortType) {
                SortType.SORT_BY_TIME -> fetchLocalLatestRecipeList(0)
                SortType.SORT_BY_FAVORITE -> fetchLocalFavoriteRecipeList(0)
                else -> fetchLocalTitleRecipeList(0)
            }
        }
    }
}
