package com.kdjj.presentation.viewmodel.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchLocalFavoriteRecipeListRequest
import com.kdjj.domain.model.request.FetchLocalLatestRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
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
        }
    }

    private fun fetchLocalLatestRecipeList(page: Int) {
        viewModelScope.launch {
            Log.d("aaa", "fetch Locla latest")
            latestRecipeUseCase(FetchLocalLatestRecipeListRequest(page))
                .onSuccess { latestRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_TIME) {
                        Log.d("aaa", "after")
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        Log.d("aaa", "first")
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                    }
                }
                .onFailure {
                    //TODO 데이터 로드에 실패했을 경우 처리
                }
        }
    }

    private fun fetchLocalFavoriteRecipeList(page: Int) {
        //TODO: Fetch Local Favorite Recipe List
        Log.d("aaa", "favorite")
    }

    private fun fetchLocalTitleRecipeList(page: Int) {
        //TODO: Fetch Local Title Recipe List
        Log.d("aaa", "title")
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
            _liveSortType.value = sortType
        }
    }
}
