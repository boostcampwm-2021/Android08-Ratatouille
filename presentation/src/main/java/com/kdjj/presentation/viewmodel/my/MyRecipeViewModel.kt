package com.kdjj.presentation.viewmodel.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.*
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.MyRecipeItem
import com.kdjj.presentation.model.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyRecipeViewModel @Inject constructor(
    private val latestRecipeUseCase: UseCase<FetchLocalLatestRecipeListRequest, List<Recipe>>,
    private val favoriteRecipeUseCase: UseCase<FetchLocalFavoriteRecipeListRequest, List<Recipe>>,
    private val titleRecipeUseCase: UseCase<FetchLocalTitleRecipeListRequest, List<Recipe>>,
    private val getRecipeUpdateStateRequest: UseCase<EmptyRequest, Flow<Int>>
) : ViewModel() {

    private val _liveSortType = MutableLiveData<SortType>()
    val liveSortType: LiveData<SortType> get() = _liveSortType

    private val _liveRecipeItemList = MutableLiveData<List<MyRecipeItem>>(listOf())
    val liveRecipeItemList: LiveData<List<MyRecipeItem>> get() = _liveRecipeItemList

    private val _liveRecipeItemSelected = MutableLiveData<MyRecipeItem.MyRecipe?>()
    val liveRecipeItemSelected: LiveData<MyRecipeItem.MyRecipe?> get() = _liveRecipeItemSelected

    private val _eventItemDoubleClicked = MutableLiveData<Event<MyRecipeItem.MyRecipe>>()
    val eventItemDoubleClicked: LiveData<Event<MyRecipeItem.MyRecipe>> get() = _eventItemDoubleClicked

    private val _eventSearchIconClicked = MutableLiveData<Event<Unit>>()
    val eventSearchIconClicked: LiveData<Event<Unit>> get() = _eventSearchIconClicked

    private val _eventAddRecipeHasPressed = MutableLiveData<Event<Unit>>()
    val eventAddRecipeHasPressed: LiveData<Event<Unit>> get() = _eventAddRecipeHasPressed

    private val _eventDataLoadFailed = MutableLiveData<Event<Unit>>()
    val eventDataLoadFailed: LiveData<Event<Unit>> get() = _eventDataLoadFailed

    init {
        setSortType(SortType.SORT_BY_TIME)

        viewModelScope.launch {
            getRecipeUpdateStateRequest(EmptyRequest)
                .onSuccess {
                    it.collect {
                        refreshRecipeList()
                    }
                }
        }
    }

    fun recipeItemSelected(selectedRecipe: MyRecipeItem.MyRecipe) {
        if (_liveRecipeItemSelected.value != selectedRecipe) {
            _liveRecipeItemSelected.value = selectedRecipe
        } else {
            _eventItemDoubleClicked.value = Event(selectedRecipe)
        }
    }

    private fun fetchLocalLatestRecipeList(page: Int) {
        viewModelScope.launch {
            latestRecipeUseCase(FetchLocalLatestRecipeListRequest(page))
                .onSuccess { latestRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_TIME && page > 0) {
                        hideProgress()
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = latestRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_TIME
                        _liveRecipeItemSelected.value = null
                    }
                }
                .onFailure {
                    hideProgress()
                    _liveSortType.value = SortType.SORT_BY_TIME
                    _eventDataLoadFailed.value = Event(Unit)
                }
        }
    }

    private fun fetchLocalFavoriteRecipeList(page: Int) {
        viewModelScope.launch {
            favoriteRecipeUseCase(FetchLocalFavoriteRecipeListRequest(page))
                .onSuccess { favoriteRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_FAVORITE && page > 0) {
                        hideProgress()
                        val myRecipeList = favoriteRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = favoriteRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_FAVORITE
                        _liveRecipeItemSelected.value = null
                    }
                }
                .onFailure {
                    hideProgress()
                    _liveSortType.value = SortType.SORT_BY_FAVORITE
                    _eventDataLoadFailed.value = Event(Unit)
                }
        }
    }

    private fun fetchLocalTitleRecipeList(page: Int) {
        viewModelScope.launch {
            titleRecipeUseCase(FetchLocalTitleRecipeListRequest(page))
                .onSuccess { titleRecipeList ->
                    if (_liveRecipeItemList.value?.isNotEmpty() == true && _liveSortType.value == SortType.SORT_BY_NAME && page > 0) {
                        hideProgress()
                        val myRecipeList = titleRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(myRecipeList)
                    } else {
                        val myRecipeList = titleRecipeList.map { MyRecipeItem.MyRecipe(it) }
                        _liveRecipeItemList.value = listOf(MyRecipeItem.PlusButton) + myRecipeList
                        _liveSortType.value = SortType.SORT_BY_NAME
                        _liveRecipeItemSelected.value = null
                    }
                }
                .onFailure {
                    hideProgress()
                    _liveSortType.value = SortType.SORT_BY_NAME
                    _eventDataLoadFailed.value = Event(Unit)
                }
        }
    }

    fun moveToRecipeEditorActivity() {
        _eventAddRecipeHasPressed.value = Event(Unit)
    }

    fun moveToRecipeSearchFragment() {
        _eventSearchIconClicked.value = Event(Unit)
    }

    fun fetchMoreRecipeData(page: Int) {
        showProgress()
        when (_liveSortType.value) {
            SortType.SORT_BY_TIME -> fetchLocalLatestRecipeList(page)
            SortType.SORT_BY_FAVORITE -> fetchLocalFavoriteRecipeList(page)
            SortType.SORT_BY_NAME -> fetchLocalTitleRecipeList(page)
        }
    }

    fun refreshRecipeList() {
        _liveSortType.value?.let {
            when (it) {
                SortType.SORT_BY_TIME -> fetchLocalLatestRecipeList(0)
                SortType.SORT_BY_FAVORITE -> fetchLocalFavoriteRecipeList(0)
                SortType.SORT_BY_NAME -> fetchLocalTitleRecipeList(0)
            }
        }
    }

    fun setSortType(sortType: SortType) {
        if (_liveSortType.value != sortType) {
            when (sortType) {
                SortType.SORT_BY_TIME -> fetchLocalLatestRecipeList(0)
                SortType.SORT_BY_FAVORITE -> fetchLocalFavoriteRecipeList(0)
                SortType.SORT_BY_NAME -> fetchLocalTitleRecipeList(0)
            }
        }
    }

    private fun showProgress() {
        _liveRecipeItemList.value = _liveRecipeItemList.value?.plus(MyRecipeItem.Progress)
    }

    private fun hideProgress() {
        _liveRecipeItemList.value?.let {
            if (it.isNotEmpty()) {
                _liveRecipeItemList.value = it.subList(0, it.size - 1)
            }
        }
    }
}
