package com.kdjj.presentation.viewmodel.others

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.FetchRemoteLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchRemotePopularRecipeListRequest
import com.kdjj.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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

    private var _liveRecipeList = MutableLiveData<List<Recipe>>()
    val liveRecipeList: LiveData<List<Recipe>> get() = _liveRecipeList

    private var fetchingJob: Job? = null

    val setFetchEnabled = Runnable {
        _liveFetchLock.value = false
    }

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
            _liveRecipeList.value?.plus(list) ?: run {
                _liveRecipeList.value = list
            }
        }.onFailure {
            // view 에게 알리기
            _liveFetchLock.value = false
        }
    }

    override fun onCleared() {
        Log.d("Test", "OthersViewModel onCleared")
        super.onCleared()
    }

    enum class OthersSortType {
        POPULAR, LATEST
    }
}