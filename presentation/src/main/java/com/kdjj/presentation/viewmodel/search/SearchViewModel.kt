package com.kdjj.presentation.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchLocalSearchRecipeListRequest
import com.kdjj.domain.model.request.FetchRemoteSearchRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.SearchTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchLocalSearchUseCase: UseCase<FetchLocalSearchRecipeListRequest, List<Recipe>>,
    private val fetchRemoteSearchUseCase: UseCase<FetchRemoteSearchRecipeListRequest, List<Recipe>>
) : ViewModel() {
    private val _liveTabState = MutableLiveData(SearchTabState.OTHERS_RECIPE)
    val liveTabState: LiveData<SearchTabState> get() = _liveTabState

    private val _liveResultList = MutableLiveData<List<Recipe>>(listOf())
    val liveResultList: LiveData<List<Recipe>> get() = _liveResultList

    private val _eventException = MutableLiveData<Event<Unit>>()
    val eventException: LiveData<Event<Unit>> get() = _eventException

    val liveKeyword = MutableLiveData("")

    fun setTabState(tabState: SearchTabState) {
        if (_liveTabState.value != tabState) {
            _liveTabState.value = tabState
        }
    }

    fun updateSearchKeyword() {
        viewModelScope.launch {
            when (liveTabState.value) {
                SearchTabState.OTHERS_RECIPE -> {
                    fetchRemoteSearchUseCase(FetchRemoteSearchRecipeListRequest(liveKeyword.value ?: "", ""))
                        .onSuccess {
                            println("local ${it.size}")
                            _liveResultList.value = it
                        }
                        .onFailure {
                            _eventException.value = Event(Unit)
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(FetchLocalSearchRecipeListRequest(liveKeyword.value ?: "", 0))
                        .onSuccess {
                            println("local ${it.size}")
                            _liveResultList.value = it
                        }
                        .onFailure {
                            _eventException.value = Event(Unit)
                        }
                }
            }
        }
    }

    fun loadMoreRecipe() {
        viewModelScope.launch {
            when (liveTabState.value) {
                SearchTabState.OTHERS_RECIPE -> {
                    fetchRemoteSearchUseCase(FetchRemoteSearchRecipeListRequest(liveKeyword.value ?: "", _liveResultList.value?.lastOrNull()?.title ?: ""))
                        .onSuccess {
                            _liveResultList.value = it
                        }
                        .onFailure {
                            _eventException.value = Event(Unit)
                        }
                }
                SearchTabState.MY_RECIPE -> {
                    fetchLocalSearchUseCase(FetchLocalSearchRecipeListRequest(liveKeyword.value ?: "", _liveResultList.value?.size ?: 0))
                        .onSuccess {
                            _liveResultList.value = it
                        }
                        .onFailure {
                            _eventException.value = Event(Unit)
                        }
                }
            }
        }
    }
}