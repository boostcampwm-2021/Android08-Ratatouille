package com.kdjj.presentation.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.model.request.FetchLocalSearchRecipeListRequest
import com.kdjj.domain.model.request.FetchRemoteSearchRecipeListRequest
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.RecipeListItemModel
import com.kdjj.presentation.model.ResponseError
import com.kdjj.presentation.model.SearchTabState
import com.kdjj.presentation.model.toRecipeListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchLocalSearchUseCase: ResultUseCase<FetchLocalSearchRecipeListRequest, List<Recipe>>,
    private val fetchRemoteSearchUseCase: ResultUseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>,
    private val getRecipeUpdateFlowUseCase: FlowUseCase<EmptyRequest, Int>
) : ViewModel() {
    private val _liveTabState = MutableLiveData(SearchTabState.OTHERS_RECIPE)
    val liveTabState: LiveData<SearchTabState> get() = _liveTabState

    private val _liveResultList = MutableLiveData<List<RecipeListItemModel>>(listOf())
    val liveResultList: LiveData<List<RecipeListItemModel>> get() = _liveResultList

    val liveKeyword = MutableLiveData("")

    private var fetchingJob: Job? = null

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private var isFetching = false
        set(value) {
            field = value
            _liveLoading.value = isFetching
        }

    private val _eventSearchRecipe = MutableLiveData<Event<SearchRecipeEvent>>()
    val eventSearchRecipe: LiveData<Event<SearchRecipeEvent>> get() = _eventSearchRecipe

    private val _liveNoResult = MutableLiveData(false)
    val liveNoResult: LiveData<Boolean> get() = _liveNoResult

    val searchSubject: PublishSubject<ButtonClick> = PublishSubject.create()

    sealed class SearchRecipeEvent {
        class Exception(val error: ResponseError) : SearchRecipeEvent()
    }

    sealed class ButtonClick {
        class Summary(val item: RecipeListItemModel) : ButtonClick()
    }

    init {
        viewModelScope.launch {
            val updateFlow = getRecipeUpdateFlowUseCase(EmptyRequest)
            updateFlow.collect {
                if (liveTabState.value == SearchTabState.MY_RECIPE) {
                    updateSearchKeyword()
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
        _liveNoResult.value = false
        if (liveKeyword.value?.isNotBlank() != true) {
            isFetching = false
            return
        }

        fetchingJob = viewModelScope.launch {
            when (liveTabState.value) {
                SearchTabState.OTHERS_RECIPE -> {
                    fetchRemoteSearchUseCase(
                        FetchRemoteSearchRecipeListRequest(
                            liveKeyword.value
                                ?: "", true
                        )
                    )
                        .onSuccess {
                            if (it.isEmpty()) {
                                _liveNoResult.value = true
                            }
                            _liveResultList.value = it.map(Recipe::toRecipeListItemModel)
                        }
                        .onFailure { t ->
                            setException(t)
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(
                        FetchLocalSearchRecipeListRequest(
                            liveKeyword.value
                                ?: "", 0
                        )
                    )
                        .onSuccess {
                            if (it.isEmpty()) {
                                _liveNoResult.value = true
                            }
                            _liveResultList.value = it.map(Recipe::toRecipeListItemModel)
                        }
                        .onFailure { t ->
                            setException(t)
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
                    fetchRemoteSearchUseCase(
                        FetchRemoteSearchRecipeListRequest(
                            liveKeyword.value
                                ?: "", false
                        )
                    )
                        .onSuccess { recipeList ->
                            _liveResultList.value = _liveResultList.value
                                ?.let { it + recipeList.map(Recipe::toRecipeListItemModel) }
                                ?: recipeList.map(Recipe::toRecipeListItemModel)
                        }
                        .onFailure { t ->
                            setException(t)
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(
                        FetchLocalSearchRecipeListRequest(
                            liveKeyword.value
                                ?: "", _liveResultList.value?.size ?: 0
                        )
                    )
                        .onSuccess { recipeList ->
                            _liveResultList.value = _liveResultList.value
                                ?.let { it + recipeList.map(Recipe::toRecipeListItemModel) }
                                ?: recipeList.map(Recipe::toRecipeListItemModel)
                        }
                        .onFailure { t ->
                            setException(t)
                        }
                }
            }
            isFetching = false
        }
    }

    fun moveToSummary(recipeModel: RecipeListItemModel) {
        searchSubject.onNext(ButtonClick.Summary(recipeModel))
    }


    private fun setException(throwable: Throwable) {
        when (throwable) {
            is NetworkException -> {
                _eventSearchRecipe.value = Event(SearchRecipeEvent.Exception(ResponseError.NETWORK_CONNECTION))
            }
            is ApiException -> {
                _eventSearchRecipe.value = Event(SearchRecipeEvent.Exception(ResponseError.SERVER))
            }
            is CancellationException -> {
            }
            else -> _eventSearchRecipe.value = Event(SearchRecipeEvent.Exception(ResponseError.UNKNOWN))
        }
    }
}

