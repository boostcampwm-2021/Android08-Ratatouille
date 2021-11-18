package com.kdjj.presentation.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.model.request.FetchLocalSearchRecipeListRequest
import com.kdjj.domain.model.request.FetchRemoteSearchRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.OthersRecipeModel
import com.kdjj.presentation.model.SearchTabState
import com.kdjj.presentation.model.toOthersRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchLocalSearchUseCase: UseCase<FetchLocalSearchRecipeListRequest, List<Recipe>>,
    private val fetchRemoteSearchUseCase: UseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>,
    private val getRecipeUpdateStateUseCase: UseCase<EmptyRequest, Flow<Int>>
) : ViewModel() {
    private val _liveTabState = MutableLiveData(SearchTabState.OTHERS_RECIPE)
    val liveTabState: LiveData<SearchTabState> get() = _liveTabState

    private val _liveResultList = MutableLiveData<List<OthersRecipeModel>>(listOf())
    val liveResultList: LiveData<List<OthersRecipeModel>> get() = _liveResultList

    private val _eventException = MutableLiveData<Event<Unit>>()
    val eventException: LiveData<Event<Unit>> get() = _eventException

    val liveKeyword = MutableLiveData("")

    private var _eventSummary = MutableLiveData<Event<OthersRecipeModel>>()
    val eventSummary: LiveData<Event<OthersRecipeModel>> get() = _eventSummary

    private var fetchingJob: Job? = null
    private var isFetching = false

    init {
        viewModelScope.launch {
            getRecipeUpdateStateUseCase(EmptyRequest).onSuccess {
                it.collect {
                    if (liveTabState.value == SearchTabState.MY_RECIPE) {
                        updateSearchKeyword()
                    }
                }
            }
        }
    }

    fun setTabState(tabState: SearchTabState) {
        if (_liveTabState.value != tabState) {
            fetchingJob?.cancel()
            isFetching = false
            _liveTabState.value = tabState
        }
    }

    fun updateSearchKeyword() {
        if (isFetching) {
            fetchingJob?.cancel()
        }
        isFetching = true

        _liveResultList.value = listOf()
        if (liveKeyword.value?.isNotBlank() != true) {
            isFetching = false
            return
        }

        fetchingJob = viewModelScope.launch {
            when (liveTabState.value) {
                SearchTabState.OTHERS_RECIPE -> {
                    fetchRemoteSearchUseCase(FetchRemoteSearchRecipeListRequest(liveKeyword.value ?: "", ""))
                        .onSuccess {
                            _liveResultList.value = it.map(Recipe::toOthersRecipeModel)
                        }
                        .onFailure {
                            if (it !is CancellationException) {
                                _eventException.value = Event(Unit)
                            }
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(FetchLocalSearchRecipeListRequest(liveKeyword.value ?: "", 0))
                        .onSuccess {
                            _liveResultList.value = it.map(Recipe::toOthersRecipeModel)
                        }
                        .onFailure {
                            if (it !is CancellationException) {
                                _eventException.value = Event(Unit)
                            }
                        }
                }
            }
            isFetching = false
        }
    }

    fun loadMoreRecipe() {
        if (isFetching) return
        isFetching = true

        if (liveKeyword.value?.isNotBlank() != true) {
            _liveResultList.value = listOf()
            isFetching = false
            return
        }

        fetchingJob = viewModelScope.launch {
            when (liveTabState.value) {
                SearchTabState.OTHERS_RECIPE -> {
                    fetchRemoteSearchUseCase(FetchRemoteSearchRecipeListRequest(liveKeyword.value ?: "", _liveResultList.value?.lastOrNull()?.title ?: ""))
                        .onSuccess { recipeList ->
                            _liveResultList.value = _liveResultList.value
                                ?.let { it + recipeList.map(Recipe::toOthersRecipeModel) }
                                ?: recipeList.map(Recipe::toOthersRecipeModel)
                        }
                        .onFailure {
                            if (it !is CancellationException) {
                                _eventException.value = Event(Unit)
                            }
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(FetchLocalSearchRecipeListRequest(liveKeyword.value ?: "", _liveResultList.value?.size ?: 0))
                        .onSuccess { recipeList ->
                            _liveResultList.value = _liveResultList.value
                                ?.let { it + recipeList.map(Recipe::toOthersRecipeModel) }
                                ?: recipeList.map(Recipe::toOthersRecipeModel)
                        }
                        .onFailure {
                            if (it !is CancellationException) {
                                _eventException.value = Event(Unit)
                            }
                        }
                }
            }
            isFetching = false
        }
    }

    fun moveToSummary(recipeModel: OthersRecipeModel) {
        _eventSummary.value = Event(recipeModel)
    }
}