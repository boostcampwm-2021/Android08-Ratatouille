package com.kdjj.presentation.viewmodel.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.domain.model.request.FetchRemoteLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchRemotePopularRecipeListRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.RecipeListItemModel
import com.kdjj.presentation.model.ResponseError
import com.kdjj.presentation.model.toRecipeListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val fetchRemoteLatestRecipeListUseCase: ResultUseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>,
    private val fetchRemotePopularRecipeListUseCase: ResultUseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>,
) : ViewModel() {

    private var _liveSortType = MutableLiveData<OthersSortType>()
    val liveSortType: LiveData<OthersSortType> get() = _liveSortType

    private var _liveFetchLock = MutableLiveData(false)
    val liveFetchLock: LiveData<Boolean> get() = _liveFetchLock

    private var _liveRecipeList = MutableLiveData<List<RecipeListItemModel>>()
    val liveRecipeList: LiveData<List<RecipeListItemModel>> get() = _liveRecipeList

    private var fetchingJob: Job? = null

    val setFetchEnabled = Runnable {
        _liveFetchLock.value = false
    }

    private var _eventOtherRecipe = MutableLiveData<Event<OtherRecipeEvent>>()
    val eventOtherRecipe: LiveData<Event<OtherRecipeEvent>> get() = _eventOtherRecipe

    sealed class OtherRecipeEvent {
        class ShowSnackBar(val error: ResponseError) : OtherRecipeEvent()
        object SearchIconClicked : OtherRecipeEvent()
        class RecipeItemClicked(val item: RecipeListItemModel) : OtherRecipeEvent()
    }

    init {
        setChecked(OthersSortType.LATEST)
    }

    fun setChecked(othersSortType: OthersSortType) {
        if (_liveSortType.value != othersSortType) {
            _liveSortType.value = othersSortType
            initFetching()
            fetchNextRecipeListPage(true)
        }
    }

    private fun initFetching() {
        fetchingJob?.cancel()
        _liveFetchLock.value = false
        _liveRecipeList.value = listOf()
    }

    fun refreshList() {
        initFetching()
        fetchNextRecipeListPage(true)
    }

    fun fetchNextRecipeListPage(isFirstPage: Boolean) {
        if (liveFetchLock.value == true) return

        _liveSortType.value?.let {

            _liveFetchLock.value = true

            fetchingJob = viewModelScope.launch {
                if (it == OthersSortType.LATEST) {
                    fetchRemoteLatestRecipeList(isFirstPage)
                } else {
                    fetchRemotePopularRecipeList(isFirstPage)
                }
            }
        }
    }

    private suspend fun fetchRemoteLatestRecipeList(isFirstPage: Boolean) {
        onRecipeListFetched(
            fetchRemoteLatestRecipeListUseCase(
                FetchRemoteLatestRecipeListRequest(isFirstPage)
            )
        )
    }

    private suspend fun fetchRemotePopularRecipeList(isFirstPage: Boolean) {
        onRecipeListFetched(
            fetchRemotePopularRecipeListUseCase(
                FetchRemotePopularRecipeListRequest(isFirstPage)
            )
        )
    }

    private fun onRecipeListFetched(result: Result<List<Recipe>>) {
        result.onSuccess { list ->
            _liveRecipeList.value?.let {
                if (list.isEmpty()) {
                    _liveFetchLock.value = false
                    return
                }
                val othersRecipeModelList = list.map { recipe -> recipe.toRecipeListItemModel() }
                if (it.isEmpty()) _liveRecipeList.value = othersRecipeModelList
                else _liveRecipeList.value = it.plus(othersRecipeModelList)
            }
        }.onFailure {
            _liveFetchLock.value = false
            when (it) {
                is NetworkException -> {
                    _eventOtherRecipe.value =
                        Event(OtherRecipeEvent.ShowSnackBar(ResponseError.NETWORK_CONNECTION))
                }
                is ApiException -> {
                    _eventOtherRecipe.value =
                        Event(OtherRecipeEvent.ShowSnackBar(ResponseError.SERVER))
                }
                is CancellationException -> {
                }
                else -> _eventOtherRecipe.value =
                    Event(OtherRecipeEvent.ShowSnackBar(ResponseError.UNKNOWN))
            }
        }
    }

    fun moveToRecipeSearchFragment() {
        _eventOtherRecipe.value = Event(OtherRecipeEvent.SearchIconClicked)
    }

    fun recipeItemClick(recipeModel: RecipeListItemModel) {
        _eventOtherRecipe.value = Event(OtherRecipeEvent.RecipeItemClicked(recipeModel))
    }

    enum class OthersSortType {
        POPULAR, LATEST
    }
}
