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
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.RecipeListItemModel
import com.kdjj.presentation.model.toRecipeListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val fetchRemoteLatestRecipeListUseCase: UseCase<FetchRemoteLatestRecipeListRequest, List<Recipe>>,
    private val fetchRemotePopularRecipeListUseCase: UseCase<FetchRemotePopularRecipeListRequest, List<Recipe>>,
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

    private var _eventNetworkFail = MutableLiveData<Event<Unit>>()
    val eventNetworkFail: LiveData<Event<Unit>> get() = _eventNetworkFail

    private var _eventApiFail = MutableLiveData<Event<Unit>>()
    val eventApiFail: LiveData<Event<Unit>> get() = _eventApiFail

    private var _eventSearchIconClicked = MutableLiveData<Event<Unit>>()
    val eventSearchIconClicked: LiveData<Event<Unit>> get() = _eventSearchIconClicked

    private var _eventRecipeItemClicked = MutableLiveData<Event<RecipeListItemModel>>()
    val eventRecipeItemClicked: LiveData<Event<RecipeListItemModel>> get() = _eventRecipeItemClicked

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
                    _eventNetworkFail.value = Event(Unit)
                }
                is ApiException -> {
                    _eventApiFail.value = Event(Unit)
                }
            }
        }
    }

    fun moveToRecipeSearchFragment() {
        _eventSearchIconClicked.value = Event(Unit)
    }

    fun recipeItemClick(recipeModel: RecipeListItemModel) {
        _eventRecipeItemClicked.value = Event(recipeModel)
    }

    enum class OthersSortType {
        POPULAR, LATEST
    }
}