package com.kdjj.presentation.viewmodel.others

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.domain.model.request.FetchRemoteLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchRemotePopularRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception
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

    private var _liveRecipeList = MutableLiveData<List<Recipe>>()
    val liveRecipeList: LiveData<List<Recipe>> get() = _liveRecipeList

    private var fetchingJob: Job? = null

    val setFetchEnabled = Runnable {
        _liveFetchLock.value = false
    }

    private var _eventException = MutableLiveData<Event<Throwable>>()
    val eventException: LiveData<Event<Throwable>> get() = _eventException

    private var _eventSearchIconClicked = MutableLiveData<Event<Unit>>()
    val eventSearchIconClicked: LiveData<Event<Unit>> get() = _eventSearchIconClicked

    init {
        Log.d("Test", "OthersViewModel init")
        setChecked(OthersSortType.LATEST)
    }

    fun setChecked(othersSortType: OthersSortType) {
        if (_liveSortType.value != othersSortType) {
            Log.d("Test", "OthersViewModel setChecked")
            // sort type 바뀔 때마다 리스트 초기화 후, 바뀐 type 으로 받아오기
            _liveSortType.value = othersSortType
            initFetching()
            fetchNextRecipeListPage()
        }
    }

    // 리스트 Fetch 관련 모든 설정을 초기화
    private fun initFetching() {
        fetchingJob?.cancel()
        _liveFetchLock.value = false
        _liveRecipeList.value = listOf()
    }

    fun refreshList() {
        Log.d("Test", "OthersViewModel refreshList")
        initFetching()
        fetchNextRecipeListPage()
    }

    fun fetchNextRecipeListPage() {
        if (liveFetchLock.value == true) return

        _liveSortType.value?.let {

            _liveFetchLock.value = true

            fetchingJob = viewModelScope.launch {
                if (it == OthersSortType.LATEST) {
                    fetchRemoteLatestRecipeList()
                } else {
                    fetchRemotePopularRecipeList()
                }
            }
        }
    }

    private suspend fun fetchRemoteLatestRecipeList() {
        val lastVisibleCreateTime = _liveRecipeList.value?.let {
            if (it.isEmpty()) Long.MAX_VALUE
            else it.last().createTime
        } ?: Long.MAX_VALUE

        onRecipeListFetched(
            fetchRemoteLatestRecipeListUseCase(
                FetchRemoteLatestRecipeListRequest(lastVisibleCreateTime)
            )
        )
    }

    private suspend fun fetchRemotePopularRecipeList() {
        val lastVisibleViewCount = _liveRecipeList.value?.let {
            if (it.isEmpty()) Int.MAX_VALUE
            else it.last().viewCount
        } ?: Int.MAX_VALUE

        onRecipeListFetched(
            fetchRemotePopularRecipeListUseCase(
                FetchRemotePopularRecipeListRequest(lastVisibleViewCount)
            )
        )
    }

    private fun onRecipeListFetched(result: Result<List<Recipe>>) {
        result.onSuccess { list ->
            Log.d("Test", "onRecipeListFetched success")
            _liveRecipeList.value?.let {
                if (list.isEmpty()) {
                    _liveFetchLock.value = false
                    return
                }
                if (it.isEmpty()) _liveRecipeList.value = list
                else _liveRecipeList.value = it.plus(list)
            }
        }.onFailure {
            // view 에게 알리기
            _liveFetchLock.value = false
            _eventException.value = Event(it)
            Log.d("Test", "onRecipeListFetched fail ${it.message}")
        }
    }

    fun moveToRecipeSearchFragment() {
        _eventSearchIconClicked.value = Event(Unit)
    }

    enum class OthersSortType {
        POPULAR, LATEST
    }
}