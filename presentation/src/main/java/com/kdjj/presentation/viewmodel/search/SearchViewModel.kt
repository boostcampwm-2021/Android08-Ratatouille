package com.kdjj.presentation.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdjj.presentation.model.SearchTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _liveTabState = MutableLiveData<SearchTabState>()
    val liveTabState: LiveData<SearchTabState> get() = _liveTabState

    fun setTabState(tabState: SearchTabState) {
        println(11111)
        if (_liveTabState.value != tabState) {
            _liveTabState.value = tabState
        }
    }
}